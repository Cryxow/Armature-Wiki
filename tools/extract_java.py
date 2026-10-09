import json,re,argparse
from pathlib import Path
parser=argparse.ArgumentParser(description='Wrap documented Java fragments with their required API context.')
parser.add_argument('input',type=Path)
parser.add_argument('output',type=Path)
args=parser.parse_args()
folder=args.output; folder.mkdir(parents=True,exist_ok=True)
common='''import com.armaturemc.api.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.ItemStack;
import java.util.*;
import java.util.logging.Logger;
'''
for index,fence in enumerate(json.loads(args.input.read_text(encoding='utf-8'))):
    code=fence['code']
    if code.startswith('package '):
        name=re.search(r'public final class (\w+)',code)[1]
        target=folder/(name+'.java')
    elif re.search(r'^(?:public )?final class ',code,re.M):
        name=re.search(r'^(?:public )?final class (\w+)',code,re.M)[1]
        code=common+code; target=folder/(name+'.java')
    else:
        fields='''ArmatureAnimationApi animations=ArmatureApi.animations();
UUID playerId=UUID.randomUUID(); Player player; JavaPlugin plugin;
Logger logger=Logger.getLogger("wiki");
Map<UUID,ArmaturePresentationHandle> presentationHandles=new HashMap<>();
ActionProvider provider;
'''
        if not re.search(r'var api\s*=',code): fields+='ArmatureAnimationApi api=ArmatureApi.animations();\n'
        if code.strip().startswith('new ItemIdentity('): code='var identity = '+code
        if code.startswith('public Optional<ItemStack> createFirstPersonMask'):
            content=code
        else: content='void example() {\n'+code+'\n}'
        name='WikiSnippet'+str(index)
        code=common+'final class '+name+' {\n'+fields+content+'\n}'
        target=folder/(name+'.java')
    target.write_text(code,encoding='utf-8')
print(f'Extracted {index+1} Java fences into {folder}')
