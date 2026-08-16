# Fence Posts

A Minecraft Fabric mod that adds standalone fence and wall posts with half-height slab variants.

## Screenshots

| Fence Posts | Wall Posts |
|:-----------:|:----------:|
| ![Fence Posts](images/screenshot2.png) | ![Wall Posts](images/screenshot3.png) |

![Creative Inventory](images/screenshot4.png)

## Features

- **Fence Posts** - Standalone 4x4 pixel posts for all vanilla fence types (13 variants)
- **Wall Posts** - Standalone 8x8 pixel posts for all vanilla wall types (25 variants)
- **Slab Variants** - Half-height versions of all posts that can be stacked
- **Waterlogging** - All posts support waterlogging
- **Lead Attachment** - Fence posts support attaching leads (just like vanilla fences)
- **Creative Tab** - Dedicated "Fence & Wall Posts" creative tab

## Supported Blocks

### Fence Posts
Oak, Spruce, Birch, Jungle, Acacia, Dark Oak, Mangrove, Cherry, Bamboo, Crimson, Warped, Pale Oak, Nether Brick

### Wall Posts
Cobblestone, Mossy Cobblestone, Stone Brick, Mossy Stone Brick, Brick, Mud Brick, Sandstone, Red Sandstone, Granite, Diorite, Andesite, Prismarine, Nether Brick, Red Nether Brick, End Stone Brick, Blackstone, Polished Blackstone, Polished Blackstone Brick, Cobbled Deepslate, Polished Deepslate, Deepslate Brick, Deepslate Tile, Tuff, Polished Tuff, Tuff Brick

## Crafting

- **1 Fence → 2 Fence Posts**
- **1 Wall → 2 Wall Posts**
- **1 Post → 2 Post Slabs**

## Pandorical

Fence Posts runs server-side, and Pandorical is a hard dependency (`fabric.mod.json`): the server will not load this mod without it. All 76 post blocks and their items are mirrored into Pandorical's content registry, and the mod's assets are synced with `registerModAssets`.

Clients are the optional half. A player on a Pandorical client sees the posts; a player on a vanilla client cannot render them, and because these are real registered blocks, cannot receive chunks containing one.

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

## License

MIT, see [LICENSE](LICENSE).
