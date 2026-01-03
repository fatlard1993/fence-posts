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

# Parent model definitions - these use the "texture" variable that children will provide
def create_parent_fence_post_model():
    """Parent model for fence posts - children just need to set the texture"""
    return {
        "textures": {"particle": "#texture"},
        "display": DISPLAY_TRANSFORMS,
        "elements": [{
            "from": [6, 0, 6], "to": [10, 16, 10],
            "faces": {
                "down": {"uv": [6, 6, 10, 10], "texture": "#texture"},
                "up": {"uv": [6, 6, 10, 10], "texture": "#texture"},
                "north": {"uv": [6, 0, 10, 16], "texture": "#texture"},
                "south": {"uv": [6, 0, 10, 16], "texture": "#texture"},
                "west": {"uv": [6, 0, 10, 16], "texture": "#texture"},
                "east": {"uv": [6, 0, 10, 16], "texture": "#texture"}
            }
        }]
    }

def create_parent_fence_post_slab_bottom_model():
    """Parent model for fence post slab bottom"""
    return {
        "textures": {"particle": "#texture"},
        "display": DISPLAY_TRANSFORMS,
        "elements": [{
            "from": [6, 0, 6], "to": [10, 8, 10],
            "faces": {
                "down": {"uv": [6, 6, 10, 10], "texture": "#texture"},
                "up": {"uv": [6, 6, 10, 10], "texture": "#texture"},
                "north": {"uv": [6, 8, 10, 16], "texture": "#texture"},
                "south": {"uv": [6, 8, 10, 16], "texture": "#texture"},
                "west": {"uv": [6, 8, 10, 16], "texture": "#texture"},
                "east": {"uv": [6, 8, 10, 16], "texture": "#texture"}
            }
        }]
    }

def create_parent_fence_post_slab_top_model():
    """Parent model for fence post slab top"""
    return {
        "textures": {"particle": "#texture"},
        "display": DISPLAY_TRANSFORMS,
        "elements": [{
            "from": [6, 8, 6], "to": [10, 16, 10],
            "faces": {
                "down": {"uv": [6, 6, 10, 10], "texture": "#texture"},
                "up": {"uv": [6, 6, 10, 10], "texture": "#texture"},
                "north": {"uv": [6, 0, 10, 8], "texture": "#texture"},
                "south": {"uv": [6, 0, 10, 8], "texture": "#texture"},
                "west": {"uv": [6, 0, 10, 8], "texture": "#texture"},
                "east": {"uv": [6, 0, 10, 8], "texture": "#texture"}
            }
        }]
    }

def create_parent_wall_post_model():
    """Parent model for wall posts"""
    return {
        "textures": {"particle": "#texture"},
        "display": DISPLAY_TRANSFORMS,
        "elements": [{
            "from": [4, 0, 4], "to": [12, 16, 12],
            "faces": {
                "down": {"uv": [4, 4, 12, 12], "texture": "#texture"},
                "up": {"uv": [4, 4, 12, 12], "texture": "#texture"},
                "north": {"uv": [4, 0, 12, 16], "texture": "#texture"},
                "south": {"uv": [4, 0, 12, 16], "texture": "#texture"},
                "west": {"uv": [4, 0, 12, 16], "texture": "#texture"},
                "east": {"uv": [4, 0, 12, 16], "texture": "#texture"}
            }
        }]
    }

def create_parent_wall_post_slab_bottom_model():
    """Parent model for wall post slab bottom"""
    return {
        "textures": {"particle": "#texture"},
        "display": DISPLAY_TRANSFORMS,
        "elements": [{
            "from": [4, 0, 4], "to": [12, 8, 12],
            "faces": {
                "down": {"uv": [4, 4, 12, 12], "texture": "#texture"},
                "up": {"uv": [4, 4, 12, 12], "texture": "#texture"},
                "north": {"uv": [4, 8, 12, 16], "texture": "#texture"},
                "south": {"uv": [4, 8, 12, 16], "texture": "#texture"},
                "west": {"uv": [4, 8, 12, 16], "texture": "#texture"},
                "east": {"uv": [4, 8, 12, 16], "texture": "#texture"}
            }
        }]
    }

def create_parent_wall_post_slab_top_model():
    """Parent model for wall post slab top"""
    return {
        "textures": {"particle": "#texture"},
        "display": DISPLAY_TRANSFORMS,
        "elements": [{
            "from": [4, 8, 4], "to": [12, 16, 12],
            "faces": {
                "down": {"uv": [4, 4, 12, 12], "texture": "#texture"},
                "up": {"uv": [4, 4, 12, 12], "texture": "#texture"},
                "north": {"uv": [4, 0, 12, 8], "texture": "#texture"},
                "south": {"uv": [4, 0, 12, 8], "texture": "#texture"},
                "west": {"uv": [4, 0, 12, 8], "texture": "#texture"},
                "east": {"uv": [4, 0, 12, 8], "texture": "#texture"}
            }
        }]
    }

# Fence types: (base_name, texture, is_burnable)
FENCE_TYPES = [
    ("oak", "minecraft:block/oak_planks", True),
    ("spruce", "minecraft:block/spruce_planks", True),
    ("birch", "minecraft:block/birch_planks", True),
    ("jungle", "minecraft:block/jungle_planks", True),
    ("acacia", "minecraft:block/acacia_planks", True),
    ("dark_oak", "minecraft:block/dark_oak_planks", True),
    ("mangrove", "minecraft:block/mangrove_planks", True),
    ("cherry", "minecraft:block/cherry_planks", True),
    ("bamboo", "minecraft:block/bamboo_planks", True),
    ("crimson", "minecraft:block/crimson_planks", False),
    ("warped", "minecraft:block/warped_planks", False),
    ("pale_oak", "minecraft:block/pale_oak_planks", True),
    ("nether_brick", "minecraft:block/nether_bricks", False),
]

# Wall types: (base_name, texture)
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

def create_fence_post_model(texture):
    """Full height fence post (4x4, 16px tall)"""
    return {
        "textures": {"texture": texture, "particle": texture},
        "display": DISPLAY_TRANSFORMS,
        "elements": [{
            "from": [6, 0, 6], "to": [10, 16, 10],
            "faces": {
                "down": {"uv": [6, 6, 10, 10], "texture": "#texture"},
                "up": {"uv": [6, 6, 10, 10], "texture": "#texture"},
                "north": {"uv": [6, 0, 10, 16], "texture": "#texture"},
                "south": {"uv": [6, 0, 10, 16], "texture": "#texture"},
                "west": {"uv": [6, 0, 10, 16], "texture": "#texture"},
                "east": {"uv": [6, 0, 10, 16], "texture": "#texture"}
            }
        }]
    }

def create_fence_post_slab_bottom_model(texture):
    """Bottom half fence post slab (4x4, 8px tall, Y 0-8)"""
    return {
        "textures": {"texture": texture, "particle": texture},
        "display": DISPLAY_TRANSFORMS,
        "elements": [{
            "from": [6, 0, 6], "to": [10, 8, 10],
            "faces": {
                "down": {"uv": [6, 6, 10, 10], "texture": "#texture"},
                "up": {"uv": [6, 6, 10, 10], "texture": "#texture"},
                "north": {"uv": [6, 8, 10, 16], "texture": "#texture"},
                "south": {"uv": [6, 8, 10, 16], "texture": "#texture"},
                "west": {"uv": [6, 8, 10, 16], "texture": "#texture"},
                "east": {"uv": [6, 8, 10, 16], "texture": "#texture"}
            }
        }]
    }

def create_fence_post_slab_top_model(texture):
    """Top half fence post slab (4x4, 8px tall, Y 8-16)"""
    return {
        "textures": {"texture": texture, "particle": texture},
        "display": DISPLAY_TRANSFORMS,
        "elements": [{
            "from": [6, 8, 6], "to": [10, 16, 10],
            "faces": {
                "down": {"uv": [6, 6, 10, 10], "texture": "#texture"},
                "up": {"uv": [6, 6, 10, 10], "texture": "#texture"},
                "north": {"uv": [6, 0, 10, 8], "texture": "#texture"},
                "south": {"uv": [6, 0, 10, 8], "texture": "#texture"},
                "west": {"uv": [6, 0, 10, 8], "texture": "#texture"},
                "east": {"uv": [6, 0, 10, 8], "texture": "#texture"}
            }
        }]
    }

def create_fence_post_slab_double_model(texture):
    """Double fence post slab (4x4, 16px tall) - same as full post"""
    return {
        "textures": {"texture": texture, "particle": texture},
        "display": DISPLAY_TRANSFORMS,
        "elements": [{
            "from": [6, 0, 6], "to": [10, 16, 10],
            "faces": {
                "down": {"uv": [6, 6, 10, 10], "texture": "#texture"},
                "up": {"uv": [6, 6, 10, 10], "texture": "#texture"},
                "north": {"uv": [6, 0, 10, 16], "texture": "#texture"},
                "south": {"uv": [6, 0, 10, 16], "texture": "#texture"},
                "west": {"uv": [6, 0, 10, 16], "texture": "#texture"},
                "east": {"uv": [6, 0, 10, 16], "texture": "#texture"}
            }
        }]
    }

def create_wall_post_model(texture):
    """Full height wall post (8x8, 16px tall)"""
    return {
        "textures": {"texture": texture, "particle": texture},
        "display": DISPLAY_TRANSFORMS,
        "elements": [{
            "from": [4, 0, 4], "to": [12, 16, 12],
            "faces": {
                "down": {"uv": [4, 4, 12, 12], "texture": "#texture"},
                "up": {"uv": [4, 4, 12, 12], "texture": "#texture"},
                "north": {"uv": [4, 0, 12, 16], "texture": "#texture"},
                "south": {"uv": [4, 0, 12, 16], "texture": "#texture"},
                "west": {"uv": [4, 0, 12, 16], "texture": "#texture"},
                "east": {"uv": [4, 0, 12, 16], "texture": "#texture"}
            }
        }]
    }

def create_wall_post_slab_bottom_model(texture):
    """Bottom half wall post slab (8x8, 8px tall, Y 0-8)"""
    return {
        "textures": {"texture": texture, "particle": texture},
        "display": DISPLAY_TRANSFORMS,
        "elements": [{
            "from": [4, 0, 4], "to": [12, 8, 12],
            "faces": {
                "down": {"uv": [4, 4, 12, 12], "texture": "#texture"},
                "up": {"uv": [4, 4, 12, 12], "texture": "#texture"},
                "north": {"uv": [4, 8, 12, 16], "texture": "#texture"},
                "south": {"uv": [4, 8, 12, 16], "texture": "#texture"},
                "west": {"uv": [4, 8, 12, 16], "texture": "#texture"},
                "east": {"uv": [4, 8, 12, 16], "texture": "#texture"}
            }
        }]
    }

def create_wall_post_slab_top_model(texture):
    """Top half wall post slab (8x8, 8px tall, Y 8-16)"""
    return {
        "textures": {"texture": texture, "particle": texture},
        "display": DISPLAY_TRANSFORMS,
        "elements": [{
            "from": [4, 8, 4], "to": [12, 16, 12],
            "faces": {
                "down": {"uv": [4, 4, 12, 12], "texture": "#texture"},
                "up": {"uv": [4, 4, 12, 12], "texture": "#texture"},
                "north": {"uv": [4, 0, 12, 8], "texture": "#texture"},
                "south": {"uv": [4, 0, 12, 8], "texture": "#texture"},
                "west": {"uv": [4, 0, 12, 8], "texture": "#texture"},
                "east": {"uv": [4, 0, 12, 8], "texture": "#texture"}
            }
        }]
    }

def create_wall_post_slab_double_model(texture):
    """Double wall post slab (8x8, 16px tall) - same as full post"""
    return {
        "textures": {"texture": texture, "particle": texture},
        "display": DISPLAY_TRANSFORMS,
        "elements": [{
            "from": [4, 0, 4], "to": [12, 16, 12],
            "faces": {
                "down": {"uv": [4, 4, 12, 12], "texture": "#texture"},
                "up": {"uv": [4, 4, 12, 12], "texture": "#texture"},
                "north": {"uv": [4, 0, 12, 16], "texture": "#texture"},
                "south": {"uv": [4, 0, 12, 16], "texture": "#texture"},
                "west": {"uv": [4, 0, 12, 16], "texture": "#texture"},
                "east": {"uv": [4, 0, 12, 16], "texture": "#texture"}
            }
        }]
    }

def create_simple_blockstate(name):
    """Blockstate for full posts (waterlogged only)"""
    return {
        "variants": {
            "waterlogged=false": {"model": f"{MOD_ID}:block/{name}"},
            "waterlogged=true": {"model": f"{MOD_ID}:block/{name}"}
        }
    }

def create_slab_blockstate(name):
    """Blockstate for slabs (type + waterlogged) - uses separate models per type"""
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

def create_fence_post_recipe(name, base):
    """Recipe: 1 fence -> 2 posts"""
    return {
        "type": "minecraft:crafting_shapeless",
        "ingredients": [f"minecraft:{base}_fence"],
        "result": {"id": f"{MOD_ID}:{name}", "count": 2}
    }

def create_fence_slab_recipe(name, post_name):
    """Recipe: 1 post -> 2 slabs"""
    return {
        "type": "minecraft:crafting_shapeless",
        "ingredients": [f"{MOD_ID}:{post_name}"],
        "result": {"id": f"{MOD_ID}:{name}", "count": 2}
    }

def create_wall_post_recipe(name, base):
    """Recipe: 1 wall -> 2 posts"""
    return {
        "type": "minecraft:crafting_shapeless",
        "ingredients": [f"minecraft:{base}_wall"],
        "result": {"id": f"{MOD_ID}:{name}", "count": 2}
    }

def create_wall_slab_recipe(name, post_name):
    """Recipe: 1 post -> 2 slabs"""
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
    """Slab drops 1 normally, 2 when double"""
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

def main():
    assets_path = f"{BASE_PATH}/assets/{MOD_ID}"
    data_path = f"{BASE_PATH}/data/{MOD_ID}"

    ensure_dir(f"{assets_path}/blockstates")
    ensure_dir(f"{assets_path}/models/block")
    ensure_dir(f"{assets_path}/models/block/parent")
    ensure_dir(f"{assets_path}/models/item")
    ensure_dir(f"{assets_path}/items")
    ensure_dir(f"{assets_path}/lang")
    ensure_dir(f"{data_path}/recipe")
    ensure_dir(f"{data_path}/loot_table/blocks")

    # Generate parent models for inheritance
    with open(f"{assets_path}/models/block/parent/fence_post.json", 'w') as f:
        json.dump(create_parent_fence_post_model(), f, indent='\t')
    with open(f"{assets_path}/models/block/parent/fence_post_slab_bottom.json", 'w') as f:
        json.dump(create_parent_fence_post_slab_bottom_model(), f, indent='\t')
    with open(f"{assets_path}/models/block/parent/fence_post_slab_top.json", 'w') as f:
        json.dump(create_parent_fence_post_slab_top_model(), f, indent='\t')
    with open(f"{assets_path}/models/block/parent/wall_post.json", 'w') as f:
        json.dump(create_parent_wall_post_model(), f, indent='\t')
    with open(f"{assets_path}/models/block/parent/wall_post_slab_bottom.json", 'w') as f:
        json.dump(create_parent_wall_post_slab_bottom_model(), f, indent='\t')
    with open(f"{assets_path}/models/block/parent/wall_post_slab_top.json", 'w') as f:
        json.dump(create_parent_wall_post_slab_top_model(), f, indent='\t')
    print("Generated 6 parent models for inheritance")

    lang = {}
    stats = {"fence_posts": 0, "fence_slabs": 0, "wall_posts": 0, "wall_slabs": 0}

    # Generate fence posts and slabs
    for base, texture, burnable in FENCE_TYPES:
        post_name = f"{base}_fence_post"
        slab_name = f"{base}_fence_post_slab"

        # Full post
        with open(f"{assets_path}/models/block/{post_name}.json", 'w') as f:
            json.dump(create_fence_post_model(texture), f, indent='\t')
        with open(f"{assets_path}/blockstates/{post_name}.json", 'w') as f:
            json.dump(create_simple_blockstate(post_name), f, indent='\t')
        with open(f"{assets_path}/models/item/{post_name}.json", 'w') as f:
            json.dump(create_item_model(post_name), f, indent='\t')
        with open(f"{assets_path}/items/{post_name}.json", 'w') as f:
            json.dump(create_item_definition(post_name), f, indent='\t')
        with open(f"{data_path}/recipe/{post_name}.json", 'w') as f:
            json.dump(create_fence_post_recipe(post_name, base), f, indent='\t')
        with open(f"{data_path}/loot_table/blocks/{post_name}.json", 'w') as f:
            json.dump(create_loot_table(post_name), f, indent='\t')
        lang[f"block.{MOD_ID}.{post_name}"] = post_name.replace("_", " ").title()
        stats["fence_posts"] += 1

        # Slab - create bottom, top, and double models
        with open(f"{assets_path}/models/block/{slab_name}_bottom.json", 'w') as f:
            json.dump(create_fence_post_slab_bottom_model(texture), f, indent='\t')
        with open(f"{assets_path}/models/block/{slab_name}_top.json", 'w') as f:
            json.dump(create_fence_post_slab_top_model(texture), f, indent='\t')
        with open(f"{assets_path}/models/block/{slab_name}_double.json", 'w') as f:
            json.dump(create_fence_post_slab_double_model(texture), f, indent='\t')
        with open(f"{assets_path}/blockstates/{slab_name}.json", 'w') as f:
            json.dump(create_slab_blockstate(slab_name), f, indent='\t')
        with open(f"{assets_path}/models/item/{slab_name}.json", 'w') as f:
            json.dump({"parent": f"{MOD_ID}:block/{slab_name}_bottom"}, f, indent='\t')
        with open(f"{assets_path}/items/{slab_name}.json", 'w') as f:
            json.dump(create_item_definition(slab_name), f, indent='\t')
        with open(f"{data_path}/recipe/{slab_name}.json", 'w') as f:
            json.dump(create_fence_slab_recipe(slab_name, post_name), f, indent='\t')
        with open(f"{data_path}/loot_table/blocks/{slab_name}.json", 'w') as f:
            json.dump(create_slab_loot_table(slab_name), f, indent='\t')
        lang[f"block.{MOD_ID}.{slab_name}"] = slab_name.replace("_", " ").title()
        stats["fence_slabs"] += 1

    # Generate wall posts and slabs
    for base, texture in WALL_TYPES:
        post_name = f"{base}_wall_post"
        slab_name = f"{base}_wall_post_slab"

        # Full post
        with open(f"{assets_path}/models/block/{post_name}.json", 'w') as f:
            json.dump(create_wall_post_model(texture), f, indent='\t')
        with open(f"{assets_path}/blockstates/{post_name}.json", 'w') as f:
            json.dump(create_simple_blockstate(post_name), f, indent='\t')
        with open(f"{assets_path}/models/item/{post_name}.json", 'w') as f:
            json.dump(create_item_model(post_name), f, indent='\t')
        with open(f"{assets_path}/items/{post_name}.json", 'w') as f:
            json.dump(create_item_definition(post_name), f, indent='\t')
        with open(f"{data_path}/recipe/{post_name}.json", 'w') as f:
            json.dump(create_wall_post_recipe(post_name, base), f, indent='\t')
        with open(f"{data_path}/loot_table/blocks/{post_name}.json", 'w') as f:
            json.dump(create_loot_table(post_name), f, indent='\t')
        lang[f"block.{MOD_ID}.{post_name}"] = post_name.replace("_", " ").title()
        stats["wall_posts"] += 1

        # Slab - create bottom, top, and double models
        with open(f"{assets_path}/models/block/{slab_name}_bottom.json", 'w') as f:
            json.dump(create_wall_post_slab_bottom_model(texture), f, indent='\t')
        with open(f"{assets_path}/models/block/{slab_name}_top.json", 'w') as f:
            json.dump(create_wall_post_slab_top_model(texture), f, indent='\t')
        with open(f"{assets_path}/models/block/{slab_name}_double.json", 'w') as f:
            json.dump(create_wall_post_slab_double_model(texture), f, indent='\t')
        with open(f"{assets_path}/blockstates/{slab_name}.json", 'w') as f:
            json.dump(create_slab_blockstate(slab_name), f, indent='\t')
        with open(f"{assets_path}/models/item/{slab_name}.json", 'w') as f:
            json.dump({"parent": f"{MOD_ID}:block/{slab_name}_bottom"}, f, indent='\t')
        with open(f"{assets_path}/items/{slab_name}.json", 'w') as f:
            json.dump(create_item_definition(slab_name), f, indent='\t')
        with open(f"{data_path}/recipe/{slab_name}.json", 'w') as f:
            json.dump(create_wall_slab_recipe(slab_name, post_name), f, indent='\t')
        with open(f"{data_path}/loot_table/blocks/{slab_name}.json", 'w') as f:
            json.dump(create_slab_loot_table(slab_name), f, indent='\t')
        lang[f"block.{MOD_ID}.{slab_name}"] = slab_name.replace("_", " ").title()
        stats["wall_slabs"] += 1

    # Write language file
    with open(f"{assets_path}/lang/en_us.json", 'w') as f:
        json.dump(lang, f, indent='\t')

    total = sum(stats.values())
    print(f"Generated {total} blocks:")
    print(f"  - {stats['fence_posts']} fence posts")
    print(f"  - {stats['fence_slabs']} fence post slabs")
    print(f"  - {stats['wall_posts']} wall posts")
    print(f"  - {stats['wall_slabs']} wall post slabs")

if __name__ == "__main__":
    main()
