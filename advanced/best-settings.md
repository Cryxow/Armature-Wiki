# 🔥 Best settings

For faster model movements and animations, it is better to use these BetterModel settings:

<pre class="language-yaml" data-title="BetterModel/config.yml"><code class="lang-yaml"># The time in ticks between inserted keyframes for smooth interpolation (lerp).
# Lower values result in smoother but potentially more resource-intensive animations.
lerp-frame-time: 1

# The number of packets to bundle together before sending.
<strong># Higher values can reduce network overhead but may increase perceived latency. 0 to disable.
</strong>packet-bundling-size: 0 # or 4
</code></pre>

{% hint style="warning" icon="hand-point-up" %}
This will result in significantly better animations but can be more resource-intensive, try what values suit better for your hardware.
{% endhint %}

