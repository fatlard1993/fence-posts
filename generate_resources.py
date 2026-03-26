#!/usr/bin/env python3
import json
import os

MOD_ID = "fence-posts"
BASE_PATH = "src/main/resources"

# Display transforms for 3D item rendering
DISPLAY_TRANSFORMS = {
    "thirdperson_righthand": {"rotation": [75, 45, 0], "translation": [0, 2.5, 0], "scale": [0.375, 0.375, 0.375]},
    "thirdperson_lefthand": {"rotation": [75, 45, 0], "translation": [0, 2.5, 0], "scale": [0.375, 0.375, 0.375]},
    "firstperson_righthand": {"rotation": [0, 45, 0], "translation": [0, 0, 0], "scale": [0.4, 0.4, 0.4]},
    "firstperson_lefthand": {"rotation": [0, 225, 0], "translation": [0, 0, 0], "scale": [0.4, 0.4, 0.4]},
    "gui": {"rotation": [30, 225, 0], "translation": [0, 0, 0], "scale": [0.625, 0.625, 0.625]},
    "ground": {"rotation": [0, 0, 0], "translation": [0, 3, 0], "scale": [0.25, 0.25, 0.25]},
    "fixed": {"rotation": [0, 0, 0], "translation": [0, 0, 0], "scale": [0.5, 0.5, 0.5]},
    "head": {"rotation": [0, 0, 0], "translation": [0, 0, 0], "scale": [1, 1, 1]}
}


def create_parent_model(inset, y_from, y_to):
    """Create a parent model with given inset (from edge) and Y range.
    Inset 6 = 4x4 centered (fence posts), inset 4 = 8x8 centered (wall posts)."""
    far = 16 - inset
    # Side UV: map the Y range onto the texture. Bottom of texture = Y=0, top = Y=16.
    side_uv_top = 16 - y_to
    side_uv_bottom = 16 - y_from
    return {
        "textures": {"particle": "#texture"},
        "display": DISPLAY_TRANSFORMS,
        "elements": [{
            "from": [inset, y_from, inset], "to": [far, y_to, far],
            "faces": {
                "down":  {"uv": [inset, inset, far, far], "texture": "#texture"},
                "up":    {"uv": [inset, inset, far, far], "texture": "#texture"},
                "north": {"uv": [inset, side_uv_top, far, side_uv_bottom], "texture": "#texture"},
                "south": {"uv": [inset, side_uv_top, far, side_uv_bottom], "texture": "#texture"},
                "west":  {"uv": [inset, side_uv_top, far, side_uv_bottom], "texture": "#texture"},
                "east":  {"uv": [inset, side_uv_top, far, side_uv_bottom], "texture": "#texture"}
            }
        }]
    }


# Fence types: (base_name, texture, is_burnable, mineable_tool)
# mineable_tool: "axe" or "pickaxe"
FENCE_TYPES = [
    ("oak", "minecraft:block/oak_planks", True, "axe"),
    ("spruce", "minecraft:block/spruce_planks", True, "axe"),
    ("birch", "minecraft:block/birch_planks", True, "axe"),
    ("jungle", "minecraft:block/jungle_planks", True, "axe"),
    ("acacia", "minecraft:block/acacia_planks", True, "axe"),
    ("dark_oak", "minecraft:block/dark_oak_planks", True, "axe"),
    ("mangrove", "minecraft:block/mangrove_planks", True, "axe"),
    ("cherry", "minecraft:block/cherry_planks", True, "axe"),
    ("bamboo", "minecraft:block/bamboo_planks", True, "axe"),
    ("crimson", "minecraft:block/crimson_planks", False, "axe"),
    ("warped", "minecraft:block/warped_planks", False, "axe"),
    ("pale_oak", "minecraft:block/pale_oak_planks", True, "axe"),
    ("nether_brick", "minecraft:block/nether_bricks", False, "pickaxe"),
]

# Wall types: (base_name, texture) -- all pickaxe-mineable
WALL_TYPES = [
    ("cobblestone", "minecraft:block/cobblestone"),
    ("mossy_cobblestone", "minecraft:block/mossy_cobblestone"),
    ("stone_brick", "minecraft:block/stone_bricks"),
    ("mossy_stone_brick", "minecraft:block/mossy_stone_bricks"),
    ("brick", "minecraft:block/bricks"),
    ("mud_brick", "minecraft:block/mud_bricks"),
    ("sandstone", "minecraft:block/sandstone"),
    ("red_sandstone", "minecraft:block/red_sandstone"),
    ("granite", "minecraft:block/granite"),
    ("diorite", "minecraft:block/diorite"),
    ("andesite", "minecraft:block/andesite"),
    ("prismarine", "minecraft:block/prismarine"),
    ("nether_brick", "minecraft:block/nether_bricks"),
    ("red_nether_brick", "minecraft:block/red_nether_bricks"),
    ("end_stone_brick", "minecraft:block/end_stone_bricks"),
    ("blackstone", "minecraft:block/blackstone"),
    ("polished_blackstone", "minecraft:block/polished_blackstone"),
    ("polished_blackstone_brick", "minecraft:block/polished_blackstone_bricks"),
    ("cobbled_deepslate", "minecraft:block/cobbled_deepslate"),
    ("polished_deepslate", "minecraft:block/polished_deepslate"),
    ("deepslate_brick", "minecraft:block/deepslate_bricks"),
    ("deepslate_tile", "minecraft:block/deepslate_tiles"),
    ("tuff", "minecraft:block/tuff"),
    ("polished_tuff", "minecraft:block/polished_tuff"),
    ("tuff_brick", "minecraft:block/tuff_bricks"),
]


def ensure_dir(path):
    os.makedirs(path, exist_ok=True)


def create_child_model(parent, texture):
    """Child model referencing a parent, only overriding the texture."""
    return {
        "parent": f"{MOD_ID}:block/parent/{parent}",
        "textures": {"texture": texture}
    }


def create_simple_blockstate(name):
    """Blockstate for full posts (waterlogged only)."""
    return {
        "variants": {
            "waterlogged=false": {"model": f"{MOD_ID}:block/{name}"},
            "waterlogged=true": {"model": f"{MOD_ID}:block/{name}"}
        }
    }


def create_slab_blockstate(name):
    """Blockstate for slabs (type + waterlogged)."""
    return {
        "variants": {
            "type=bottom,waterlogged=false": {"model": f"{MOD_ID}:block/{name}_bottom"},
            "type=bottom,waterlogged=true": {"model": f"{MOD_ID}:block/{name}_bottom"},
            "type=top,waterlogged=false": {"model": f"{MOD_ID}:block/{name}_top"},
            "type=top,waterlogged=true": {"model": f"{MOD_ID}:block/{name}_top"},
            "type=double,waterlogged=false": {"model": f"{MOD_ID}:block/{name}_double"},
            "type=double,waterlogged=true": {"model": f"{MOD_ID}:block/{name}_double"}
        }
    }


def create_item_model(name):
    return {"parent": f"{MOD_ID}:block/{name}"}


def create_item_definition(name):
    return {"model": {"type": "minecraft:model", "model": f"{MOD_ID}:item/{name}"}}


def create_post_recipe(name, ingredient):
    """Recipe: 1 source block -> 2 posts."""
    return {
        "type": "minecraft:crafting_shapeless",
        "ingredients": [ingredient],
        "result": {"id": f"{MOD_ID}:{name}", "count": 2}
    }


def create_slab_recipe(name, post_name):
    """Recipe: 1 post -> 2 slabs."""
    return {
        "type": "minecraft:crafting_shapeless",
        "ingredients": [f"{MOD_ID}:{post_name}"],
        "result": {"id": f"{MOD_ID}:{name}", "count": 2}
    }


def create_loot_table(name):
    return {
        "type": "minecraft:block",
        "pools": [{
            "rolls": 1,
            "entries": [{"type": "minecraft:item", "name": f"{MOD_ID}:{name}"}],
            "conditions": [{"condition": "minecraft:survives_explosion"}]
        }]
    }


def create_slab_loot_table(name):
    """Slab drops 1 normally, 2 when double."""
    return {
        "type": "minecraft:block",
        "pools": [{
            "rolls": 1,
            "entries": [{
                "type": "minecraft:item",
                "name": f"{MOD_ID}:{name}",
                "functions": [{
                    "function": "minecraft:set_count",
                    "count": 2,
                    "conditions": [{
                        "condition": "minecraft:block_state_property",
                        "block": f"{MOD_ID}:{name}",
                        "properties": {"type": "double"}
                    }]
                }]
            }],
            "conditions": [{"condition": "minecraft:survives_explosion"}]
        }]
    }


def create_tag(values):
    """Create a block/item tag file."""
    return {
        "replace": False,
        "values": values
    }


def write_json(path, data):
    ensure_dir(os.path.dirname(path))
    with open(path, 'w') as f:
        json.dump(data, f, indent='\t')


def main():
    assets_path = f"{BASE_PATH}/assets/{MOD_ID}"
    data_path = f"{BASE_PATH}/data/{MOD_ID}"
    mc_tags_path = f"{BASE_PATH}/data/minecraft/tags/block"

    lang = {}
    stats = {"fence_posts": 0, "fence_slabs": 0, "wall_posts": 0, "wall_slabs": 0}

    # Collect block IDs for tags
    axe_mineable = []
    pickaxe_mineable = []
    fence_tag_entries = []
    wall_tag_entries = []

    # Generate parent models (6 total: fence post/slab + wall post/slab)
    parents = {
        "fence_post":             (6, 0, 16),
        "fence_post_slab_bottom": (6, 0, 8),
        "fence_post_slab_top":    (6, 8, 16),
        "wall_post":              (4, 0, 16),
        "wall_post_slab_bottom":  (4, 0, 8),
        "wall_post_slab_top":     (4, 8, 16),
    }
    for name, (inset, y_from, y_to) in parents.items():
        write_json(f"{assets_path}/models/block/parent/{name}.json",
                   create_parent_model(inset, y_from, y_to))
    print(f"Generated {len(parents)} parent models")

    # Generate fence posts and slabs
    for base, texture, burnable, tool in FENCE_TYPES:
        post_name = f"{base}_fence_post"
        slab_name = f"{base}_fence_post_slab"
        post_id = f"{MOD_ID}:{post_name}"
        slab_id = f"{MOD_ID}:{slab_name}"

        # Full post -- child model referencing parent
        write_json(f"{assets_path}/models/block/{post_name}.json",
                   create_child_model("fence_post", texture))
        write_json(f"{assets_path}/blockstates/{post_name}.json",
                   create_simple_blockstate(post_name))
        write_json(f"{assets_path}/models/item/{post_name}.json",
                   create_item_model(post_name))
        write_json(f"{assets_path}/items/{post_name}.json",
                   create_item_definition(post_name))
        write_json(f"{data_path}/recipe/{post_name}.json",
                   create_post_recipe(post_name, f"minecraft:{base}_fence"))
        write_json(f"{data_path}/loot_table/blocks/{post_name}.json",
                   create_loot_table(post_name))

        # Slab -- bottom, top, double child models
        write_json(f"{assets_path}/models/block/{slab_name}_bottom.json",
                   create_child_model("fence_post_slab_bottom", texture))
        write_json(f"{assets_path}/models/block/{slab_name}_top.json",
                   create_child_model("fence_post_slab_top", texture))
        write_json(f"{assets_path}/models/block/{slab_name}_double.json",
                   create_child_model("fence_post", texture))
        write_json(f"{assets_path}/blockstates/{slab_name}.json",
                   create_slab_blockstate(slab_name))
        write_json(f"{assets_path}/models/item/{slab_name}.json",
                   {"parent": f"{MOD_ID}:block/{slab_name}_bottom"})
        write_json(f"{assets_path}/items/{slab_name}.json",
                   create_item_definition(slab_name))
        write_json(f"{data_path}/recipe/{slab_name}.json",
                   create_slab_recipe(slab_name, post_name))
        write_json(f"{data_path}/loot_table/blocks/{slab_name}.json",
                   create_slab_loot_table(slab_name))

        # Language
        lang[f"block.{MOD_ID}.{post_name}"] = post_name.replace("_", " ").title()
        lang[f"block.{MOD_ID}.{slab_name}"] = slab_name.replace("_", " ").title()

        # Tags
        if tool == "axe":
            axe_mineable.extend([post_id, slab_id])
        else:
            pickaxe_mineable.extend([post_id, slab_id])
        fence_tag_entries.extend([post_id, slab_id])

        stats["fence_posts"] += 1
        stats["fence_slabs"] += 1

    # Generate wall posts and slabs
    for base, texture in WALL_TYPES:
        post_name = f"{base}_wall_post"
        slab_name = f"{base}_wall_post_slab"
        post_id = f"{MOD_ID}:{post_name}"
        slab_id = f"{MOD_ID}:{slab_name}"

        # Full post
        write_json(f"{assets_path}/models/block/{post_name}.json",
                   create_child_model("wall_post", texture))
        write_json(f"{assets_path}/blockstates/{post_name}.json",
                   create_simple_blockstate(post_name))
        write_json(f"{assets_path}/models/item/{post_name}.json",
                   create_item_model(post_name))
        write_json(f"{assets_path}/items/{post_name}.json",
                   create_item_definition(post_name))
        write_json(f"{data_path}/recipe/{post_name}.json",
                   create_post_recipe(post_name, f"minecraft:{base}_wall"))
        write_json(f"{data_path}/loot_table/blocks/{post_name}.json",
                   create_loot_table(post_name))

        # Slab
        write_json(f"{assets_path}/models/block/{slab_name}_bottom.json",
                   create_child_model("wall_post_slab_bottom", texture))
        write_json(f"{assets_path}/models/block/{slab_name}_top.json",
                   create_child_model("wall_post_slab_top", texture))
        write_json(f"{assets_path}/models/block/{slab_name}_double.json",
                   create_child_model("wall_post", texture))
        write_json(f"{assets_path}/blockstates/{slab_name}.json",
                   create_slab_blockstate(slab_name))
        write_json(f"{assets_path}/models/item/{slab_name}.json",
                   {"parent": f"{MOD_ID}:block/{slab_name}_bottom"})
        write_json(f"{assets_path}/items/{slab_name}.json",
                   create_item_definition(slab_name))
        write_json(f"{data_path}/recipe/{slab_name}.json",
                   create_slab_recipe(slab_name, post_name))
        write_json(f"{data_path}/loot_table/blocks/{slab_name}.json",
                   create_slab_loot_table(slab_name))

        # Language
        lang[f"block.{MOD_ID}.{post_name}"] = post_name.replace("_", " ").title()
        lang[f"block.{MOD_ID}.{slab_name}"] = slab_name.replace("_", " ").title()

        # Tags -- all wall posts are pickaxe-mineable
        pickaxe_mineable.extend([post_id, slab_id])
        wall_tag_entries.extend([post_id, slab_id])

        stats["wall_posts"] += 1
        stats["wall_slabs"] += 1

    # Write language file
    write_json(f"{assets_path}/lang/en_us.json", lang)

    # Write mineable tags
    write_json(f"{mc_tags_path}/mineable/axe.json", create_tag(axe_mineable))
    write_json(f"{mc_tags_path}/mineable/pickaxe.json", create_tag(pickaxe_mineable))

    # Write block type tags
    write_json(f"{mc_tags_path}/fences.json", create_tag(fence_tag_entries))
    write_json(f"{mc_tags_path}/walls.json", create_tag(wall_tag_entries))

    total = sum(stats.values())
    print(f"Generated resources for {total} blocks:")
    print(f"  {stats['fence_posts']} fence posts")
    print(f"  {stats['fence_slabs']} fence post slabs")
    print(f"  {stats['wall_posts']} wall posts")
    print(f"  {stats['wall_slabs']} wall post slabs")
    print(f"  {len(axe_mineable)} blocks in mineable/axe tag")
    print(f"  {len(pickaxe_mineable)} blocks in mineable/pickaxe tag")
    print(f"  {len(fence_tag_entries)} blocks in fences tag")
    print(f"  {len(wall_tag_entries)} blocks in walls tag")


if __name__ == "__main__":
    main()
