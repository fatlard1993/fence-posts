# Fence Posts - Development Guide

For what the mod is and how it plays, see [README.md](README.md).

## Installation

Install server-side alongside its declared dependencies (see `fabric.mod.json`); connecting clients need only Pandorical. Version targets live in `gradle.properties` (Minecraft, loader, Fabric API) and `fabric.mod.json` (Java).

## For Mod Developers

`Main` exposes the same two registration entry points the mod uses on itself:

```java
import justfatlard.fence_posts.Main;
import justfatlard.fence_posts.PostBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;

// (baseName, sound, burnable, requiresTool, hardness, resistance, mapColor)
PostBlock post = Main.registerFencePost("oak", SoundType.WOOD, true, false, 2.0f, 3.0f, MapColor.WOOD);

// (baseName, sound, hardness, resistance, mapColor)
PostBlock wallPost = Main.registerWallPost("cobblestone", SoundType.STONE, 2.0f, 6.0f, MapColor.STONE);
```

Each call registers the post, its slab variant, and both BlockItems, and mirrors all four into Pandorical's content registry. Call them during `onInitialize`, before Pandorical's content sync runs. A repeat registration of the same name returns `null` rather than throwing.

**These are not yet usable for modded blocks.** `baseName` is resolved against the vanilla namespace (`minecraft:<baseName>_fence` / `_wall`) to derive models and textures, so the methods currently only reach blocks vanilla already has. Supporting a modded base block means letting the caller pass the base identifier; the two methods are public because that change is intended, not because it already works.
