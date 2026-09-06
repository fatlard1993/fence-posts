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

Fence Posts runs server-side, and Pandorical is required: the server will not load this mod without it. All 76 post blocks and their items are mirrored into Pandorical's content registry, and their textures and models arrive through Pandorical's content sync.

Clients are the optional half. A player on a Pandorical client sees the posts; a player on a vanilla client cannot render them, and because these are real registered blocks, cannot receive chunks containing one.

## Development

Installing and the API for other mods are in [DEVELOPMENT.md](DEVELOPMENT.md).

## License

MIT, see [LICENSE](LICENSE).
