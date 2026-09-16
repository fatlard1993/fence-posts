# Fence Posts

A Minecraft Fabric mod that lets any fence or wall stand alone as a post, with half-height post slabs.

## Screenshots

| Fence Posts | Wall Posts |
|:-----------:|:----------:|
| ![Fence Posts](images/screenshot2.png) | ![Wall Posts](images/screenshot3.png) |

## Features

- **Posts** - Sneak and click any fence or wall with an empty hand and it stands alone as a post; click again and it joins back in. Every fence and wall the game has, and any a mod adds. A post crafted in an older version becomes the fence or wall it stood for, standing alone, with the same click
- **Post Slabs** - Half-height posts for the 13 vanilla fences and 25 vanilla walls, stackable into a double
- **Mixed Slabs** - With Mixed Slabs installed, a post slab goes in the empty half of any other slab
- **Waterlogging** - Post slabs support waterlogging

## Supported Blocks

Posts work on every fence and wall; post slabs come in these materials.

### Fence Post Slabs
Oak, Spruce, Birch, Jungle, Acacia, Dark Oak, Mangrove, Cherry, Bamboo, Crimson, Warped, Pale Oak, Nether Brick

### Wall Post Slabs
Cobblestone, Mossy Cobblestone, Stone Brick, Mossy Stone Brick, Brick, Mud Brick, Sandstone, Red Sandstone, Granite, Diorite, Andesite, Prismarine, Nether Brick, Red Nether Brick, End Stone Brick, Blackstone, Polished Blackstone, Polished Blackstone Brick, Cobbled Deepslate, Polished Deepslate, Deepslate Brick, Deepslate Tile, Tuff, Polished Tuff, Tuff Brick

## Crafting

- **1 Fence → 2 Fence Post Slabs**
- **1 Wall → 2 Wall Post Slabs**

Posts are not crafted: every fence and wall is already one, a sneak-click away.

## Pandorical

Fence Posts runs server-side, and Pandorical is required: the server will not load this mod without it. All 76 post blocks and their items are mirrored into Pandorical's content registry, and their textures and models arrive through Pandorical's content sync.

Clients are the optional half. A player on a Pandorical client sees the posts; a player on a vanilla client cannot render them, and because these are real registered blocks, cannot receive chunks containing one.

## Development

Installing and the API for other mods are in [DEVELOPMENT.md](DEVELOPMENT.md).

## License

MIT, see [LICENSE](LICENSE).
