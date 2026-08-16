# 👕 Custom Armors

## Custom textures for vanilla armors

Replace `armor.png` and `leggings.png` textures inside `plugins/BetterModel/armors/armors/<vanilla_type>/`  by your custom textures. Make sure to use the same file names.

## Custom armors added by a plugin

The followings steps are assuming you already have a custom armor set-up by a plugin as CraftEngine, ItemsAdder or Nexo.

Let's say you have a custom emerald armor set with a custom ID like `your_namespace:emerald_chestplate`, `your_namespace:emerald_leggings` etc

### Adding custom textures

Copy your player armor `armor.png` and `leggings.png` textures inside `plugins/BetterModel/armors/armors/emerald/`  _(replace `emerald` by your armor type name)_

If you are not sure, take a look at the vanilla armors already inside `plugins/BetterModel/armors/`

### Mapping the textures to the armor

Go inside `/Armature/config.yml` and look for `armor-mappings:`. Follow this structure:

<pre class="language-yaml"><code class="lang-yaml">armor-mappings:
  "<a data-footnote-ref href="#user-content-fn-1">your_namespace:emerald_chestplate</a>": <a data-footnote-ref href="#user-content-fn-2">emerald</a> 
</code></pre>



[^1]: your custom armor item ID

[^2]: ```
    name of the folder inside /BetterModel/armors/armors/
    ```
