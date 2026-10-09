package com.armaturemc.plugin;

import com.google.gson.*;
import com.armaturemc.renderer.internal.asset.NativeModelPackCompiler;
import java.nio.file.*;
import java.util.*;
import org.bukkit.configuration.file.YamlConfiguration;

public final class WikiValidation {
    static Path scratch;
    static String variables;
    static int yamlCount, profiles, models;
    static final List<String> failures=new ArrayList<>();
    static final List<String> pending=new ArrayList<>();
    static Map<String,Object> map(Object v) { return (Map<String,Object>)v; }
    static void validateProfile(String label,Map<String,Object> input) throws Exception {
        var document=new LinkedHashMap<>(input);
        var dir=Files.createTempDirectory(scratch,"profile-");
        var folder=Files.createDirectories(dir.resolve("profiles"));
        Files.writeString(dir.resolve("variables.yml"),variables);
        // Referenced mounts may be defined in another documented file. Supply
        // static attachment definitions only when omitted from this fence.
        String text=new org.yaml.snakeyaml.Yaml().dump(document);
        var references=java.util.regex.Pattern.compile("(?m)^\\s*profile: ([a-zA-Z0-9_.-]+)\\s*$").matcher(text);
        while(references.find()) {
            String id=references.group(1);
            document.putIfAbsent(id, Map.of("type","attachment","model",Map.of("name","fixture")));
        }
        var yaml=new YamlConfiguration();
        document.forEach(yaml::set);
        Files.writeString(folder.resolve("example.yml"),yaml.saveToString());
        var result=new AnimationConfigLoader().loadLenient(folder);
        if(!result.warnings().isEmpty()) throw new IllegalArgumentException(String.join("; ",result.warnings()));
        profiles++;
    }
    static Map<String,Object> wrap(Map<String,Object> data) {
        if(data.values().stream().anyMatch(v -> v instanceof Map<?,?> m && (m.containsKey("match")||m.containsKey("model")||m.containsKey("type")&&"attachment".equals(m.get("type"))))) return data;
        var profile=new LinkedHashMap<String,Object>();
        profile.put("match",Map.of("item","minecraft:stick"));
        profile.put("model",Map.of("name","fixture"));
        if(data.containsKey("animations")||data.containsKey("match")||data.containsKey("model")||data.containsKey("integrations")||data.containsKey("attachments")||data.containsKey("variants")||data.containsKey("physics")) {
            profile.putAll(data);
            if(profile.get("model") instanceof Map<?,?> m && !m.containsKey("name")) {
                var md=new LinkedHashMap<>(map(m)); md.put("name","fixture"); profile.put("model",md);
            }
        } else if(data.containsKey("movement")||data.containsKey("actions")||data.containsKey("additives")) profile.put("animations",data);
        else return null;
        return Map.of("wiki_fixture",profile);
    }
    public static void main(String[] args) throws Exception {
        Path json=Path.of(args[0]); Path wiki=Path.of(args[1]);
        scratch=Files.createDirectories(Path.of(args[2]));
        var fences=JsonParser.parseString(Files.readString(json)).getAsJsonArray();
        String all=Files.readString(json);
        var names=new TreeSet<String>();
        var matcher=java.util.regex.Pattern.compile("variable\\.([a-zA-Z0-9_.-]+)").matcher(all);
        while(matcher.find()) names.add(matcher.group(1));
        var registry=new LinkedHashMap<String,Object>();
        for(String name:names) registry.put(name,Map.of("type","string","source","constant","value","fixture","default","fixture"));
        var registryYaml=new YamlConfiguration(); registryYaml.set("variables",registry); variables=registryYaml.saveToString();
        for(var f:fences) {
            var fence=f.getAsJsonObject();
            String label=fence.get("page").getAsString()+"#"+fence.get("index").getAsInt();
            String text=fence.get("code").getAsString();
            try {
                var config=new YamlConfiguration(); config.loadFromString(text); yamlCount++;
                if(label.startsWith("getting-started\\configuration.md") || label.startsWith("getting-started/configuration.md")) {
                    var report=new ArmatureConfigValidator().validate(config);
                    var errors=report.issues().stream().filter(i->i.severity()==ArmatureConfigValidator.Severity.ERROR).toList();
                    if(report.hasErrors()) throw new IllegalArgumentException(String.join("; ",report.render()));
                }
                Object parsed=new org.yaml.snakeyaml.Yaml().load(text);
                if(parsed instanceof Map<?,?> m) {
                    var data=map(m);
                    if(data.keySet().equals(Set.of("variables"))) {
                        var dir=Files.createTempDirectory(scratch,"variables-");
                        Files.writeString(dir.resolve("variables.yml"),text);
                        var folder=Files.createDirectories(dir.resolve("profiles"));
                        var result=new AnimationConfigLoader().loadLenient(folder);
                        if(!result.warnings().isEmpty()) throw new IllegalArgumentException(String.join("; ",result.warnings()));
                    } else {
                        var wrapped=wrap(data);
                        if(wrapped!=null) validateProfile(label,wrapped);
                    }
                }
            } catch(Throwable e) {
                if (label.contains("grouping-items.md") && (e instanceof ExceptionInInitializerError && e.getCause()!=null && String.valueOf(e.getCause().getMessage()).contains("No RegistryAccess implementation found") || e instanceof NoClassDefFoundError && e.getMessage().contains("org.bukkit.Registry"))) {
                    pending.add(label+": live Paper item/block registries required (headless JVM unavailable)");
                } else failures.add(label+": "+e);
            }
        }
        try(var paths=Files.list(wiki.resolve(".gitbook/assets/examples"))) {
            for(var path:paths.filter(p->p.toString().endsWith(".yml")).toList()) {
                try { validateProfile(path.getFileName().toString(),map(new org.yaml.snakeyaml.Yaml().load(Files.readString(path)))); }
                catch(Throwable e) { failures.add(path.getFileName()+": "+e); }
            }
        }
        for(int i=3;i<args.length;i++) {
            try {
                var result=new NativeModelPackCompiler().compile(Path.of(args[i]));
                System.out.println("MODEL "+result.modelId()+" bones="+result.model().bones().size()+" clips="+result.animations().stream().map(a->a.name()).toList()); models++;
            } catch(Throwable e) { failures.add("model "+args[i]+": "+e); }
        }
        System.out.println("YAML="+yamlCount+" profile fixtures="+profiles+" models="+models+" failures="+failures.size()+" pending live Paper="+pending.size());
        failures.forEach(System.out::println);
        pending.forEach(System.out::println);
        if(!failures.isEmpty()) System.exit(1);
    }
}
