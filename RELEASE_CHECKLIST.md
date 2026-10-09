# Armature 2.0.0 documentation release preparation

Maintainer document. It is intentionally excluded from `SUMMARY.md`.
Do not merge the draft PR or publish this revision before the server plugin
2.0.0 is published and final artifacts have passed the checks below.

## Baselines

- Wiki base: `28b4a5dfa7435a053d1504f742352379f3ad1e67` (`main`).
- Previous English documentation: Git tag `wiki-v1.3.0`, same base commit.
- Server sources: `bebc1e803779095fd4531fb1aa6b857d58f85696` (`dev`, 2.0.0).
- Server JAR SHA256: `C9671AE468FF5B5D9B99EEFED00761E4BFC236F3C958092698C99B4EA3EEF83A`.
- Standalone Fabric sources: `34b8bff9a6abea7e444e3783faa36f9842d95c06`.
- Blockbench extension: current local `0.3.0` distribution, strict native format v2.
- Blockbench JS SHA256: `D42F373C89B812D7411986D60310376F765A49304383F57B4EA20D684E67925C`.
- Server/client transport: protocol 9; public API contract major 1.

The source compatibility table is not a claim that all final release combinations
have been visually validated. Pin exact final JAR/JS versions and hashes below
before publication. French and Russian branches/spaces are outside this change.

## Automated preparation evidence

- Navigation preserves existing Markdown page paths and assets, with eight
  top-level sections and 53 pages, including a detailed Molang reference.
- GitBook configuration has 25 redirects for changed published routes; target
  files are syntax/structurally checked with the same validation workflow.
- Run `python tools/validate_wiki.py --output <scratch-directory>` to check
  navigation, local links/assets, headings and fences and extract examples.
- All documented YAML fences are syntax-loaded with Paper's `YamlConfiguration`.
  Complete profiles and rule fragments use the current Armature loaders with a
  scratch shared variable registry. Fragment fixtures supply missing model IDs
  and referenced static attachment definitions; they do not validate runtime
  geometry or create provider items.
- Java fences are compiled against the matching API/Paper classpath. Statement
  fragments are wrapped with their documented API/player context. Complete
  downloadable source is compiled unchanged.
- Native compilation includes the downloadable `wiki_player.armature`/`wiki_charm.armature`, the
  current v2 default rig and the bundled sword/empty-hand models. This proves
  compiler acceptance and named clips, not connected-client visuals.
- Blockbench screenshots show actual 5.2.2 controls and the current extension
  loaded in an isolated web editor harness. Cameras and the extension's real
  load hooks were invoked without changing their distributed source. Desktop
  remains required for supported installation and save/reopen acceptance.

Validation totals and the current GitBook head-preview status are recorded in
the draft PR. Two family-selector examples require live Paper item/block
registries and must be replayed on a server; a headless JVM cannot initialize
those registries. They are not counted as successful semantic parser checks.

Preparation totals: **67 YAML fences** syntax-loaded, **53 profile fixtures**
accepted by the Armature loaders, **2 additional family fixtures pending live
Paper**, **21 Java fences** compiled and **5 model sources** compiled natively.
Use the sources under `tools/` to repeat checks against the matching plugin
checkout; statement wrappers supply only the missing surrounding Java context.

The upgrade guide and profile migration commands are retained. Migration
settings are removed from the server defaults and existing configurations;
the local converter uses a fixed internal 256 KiB source/candidate limit.

## Before merging

- [ ] Publish server plugin 2.0.0 and confirm download URL.
- [ ] Pin final server, optional client, Blockbench, PacketEvents and provider versions/hashes.
- [ ] Confirm `armature-api:2.0.0` resolves from Maven Central; replace preparation caveats with final links.
- [ ] Recheck the exact supported server/client/version matrix against released artifacts.
- [ ] Rebase/synchronize the documentation branch with current `main` without replacing public docs early.
- [ ] Inspect the latest GitBook preview for navigation, code, tables, images and Mermaid.
- [ ] On a clean Paper/Folia test server, replay **Your First Presentation** with final pack delivery.
- [ ] Load both family-selector examples with the live Paper registries.
- [ ] Install final Blockbench Desktop/Cameras/Armature extension; save/reopen the tutorial model.
- [ ] Verify both hands, rapid equip/unequip, protected actions, variants, additives and nested accessories.
- [ ] Verify classic/slim skins, each armor slot, dye/trims and custom mapping assets.
- [ ] Verify camera/motion/physics on server rendering and matching optional client builds.
- [ ] Verify no-mod/disabled-client fallback and composite server fallback.
- [ ] Test final Sodium/Iris/shaderpack combinations; record supported/unsupported results.
- [ ] Generate/host/deliver the actual final ItemsAdder/Nexo/CraftEngine packs used in the release.
- [ ] Update all final release links and limitation statements, then merge for GitBook publication.

## URLs and review

Existing Markdown paths are retained. `.gitbook.yaml` redirects preserve old
published routes when the new navigation changes their URLs. New guides are
linked from `SUMMARY.md`.
No public archive section is added; use the Git tag to recover 1.3.0 content.
Do not merge, enable auto-merge or update the published English space during
preparation. Retrieve the rendered preview URL from the **current head commit's**
GitBook status, rather than constructing a URL or retaining an earlier revision.
