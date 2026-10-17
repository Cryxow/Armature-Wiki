"""Check GitBook navigation, local links, assets and extract source examples.

Python 3.10+, standard library only. Use --output for Armature parser fixtures.
"""
import argparse
import json
import re
from pathlib import Path
from urllib.parse import unquote


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--output', type=Path)
    args = parser.parse_args()
    root = Path(__file__).resolve().parents[1]
    summary = root / 'SUMMARY.md'
    paths = re.findall(r'\]\(([^)]+\.md)\)', summary.read_text(encoding='utf-8'))
    errors = []
    gitbook_config = root / '.gitbook.yaml'
    if not gitbook_config.is_file():
        errors.append('Missing .gitbook.yaml with redirects for existing published routes')
    else:
        redirects = gitbook_config.read_text(encoding='utf-8').split('redirects:\n', 1)[-1]
        for old_route, target in re.findall(r'^  ([^:]+): (.+\.md)$', redirects, re.M):
            if not (root / target).is_file():
                errors.append(f'Redirect {old_route}: missing target {target}')
    if len(paths) != len(set(paths)):
        errors.append('Duplicate SUMMARY.md entries')
    fences = {'yaml': [], 'java': []}
    for path in [summary] + [root / p for p in paths]:
        if not path.is_file():
            errors.append(f'Missing page: {path.relative_to(root)}')
            continue
        text = path.read_text(encoding='utf-8-sig')
        label = path.relative_to(root).as_posix()
        if text.count('```') % 2:
            errors.append(f'{label}: unbalanced code fences')
        if path != summary and len(re.findall(r'^# ', text, re.M)) != 1:
            errors.append(f'{label}: expected one page heading')
        prose = re.sub(r'^```.*?^```\s*$', '', text, flags=re.M | re.S)
        links = re.findall(r'\]\(([^)]+)\)|(?:src|href)="([^"]+)"', prose)
        for link in links:
            link = next(x for x in link if x).strip('<>').split(' "')[0]
            if link.startswith(('https:', 'http:', 'mailto:', '#', 'data:')):
                continue
            target = (path.parent / unquote(link.split('#')[0])).resolve()
            if not target.exists():
                errors.append(f'{label}: missing link/asset {link}')
        for index, match in enumerate(re.finditer(r'^```(\w*)\n(.*?)^```\s*$', text, re.M | re.S)):
            if match[1] in fences:
                fences[match[1]].append({'page': label, 'index': index, 'code': match[2]})
    if args.output:
        args.output.mkdir(parents=True, exist_ok=True)
        for language, content in fences.items():
            (args.output / f'wiki-{language}.json').write_text(json.dumps(content), encoding='utf-8')
    print(f'{len(paths)} pages; {len(fences["yaml"])} YAML fences; {len(fences["java"])} Java fences')
    for error in errors:
        print(error)
    if errors:
        raise SystemExit(1)
    print('Navigation, local links/assets, page headings and fences: OK')


if __name__ == '__main__':
    main()
