---
description: "Client Rendering & Fallback"
---

# Client Rendering & Fallback

The server still chooses profiles, variants, clips, conditions, visibility,
held-item visuals and action timing. The client draws the selected presentation
at frame rate. It does not take over weapon gameplay or select its own YAML
profile.

## Handoff

1. The client advertises its protocol and rendering preference.
2. The server checks readiness and supplies the model bundle and state.
3. The client loads textures/geometry; a cold upload may take time.
4. The renderer switch coordinates the fallback pack. An equip intro waits for
   upload so that it is not skipped before the model is drawable.

Native model bundles are delivered separately from common resource-pack assets.
They do not replace custom-item textures, sounds, fonts or other provider
content. See [Resource Packs](../configuration/resource-packs.md) for split-pack
delivery and why the provider's final pack must still reach the player.

## Server fallback

No mod, an unsupported protocol, a disabled preference or unavailable client
rendering leaves the server renderer responsible for presentation. A renderer
switch can wait for a resource reload rather than drawing against missing
assets. Keep the server renderer pack available even on a modded server.

The current client transport handles multiple hand/outgoing channels, but does
not transport arbitrary composite attachment scenes. Profiles with native
compositions therefore use the server renderer. A successful client handshake
does not mean every profile will use client rendering.

Outgoing equip/unequip generations retain their own state; a stale stop or
bundle response must not remove a newer generation. Test rapid slot changes,
both hands and toggling the renderer during a finite action.

## Verify fallback

Connect once with the mod enabled, once with its preference disabled and once
with no mod. Test a simple profile and an attachment profile. Check model,
held-item visibility, skin, armor, pack acceptance and action completion in each
case. Missing common textures are a pack delivery issue, even if the model
bundle itself loaded successfully.
