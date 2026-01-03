package justfatlard.fence_posts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Generates runtime resources for dynamically discovered fences and walls.
 * This allows the mod to automatically support any modded fence or wall block.
 */
public class RuntimeResourceGenerator {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final String MOD_ID = Main.MOD_ID;

	// Track which blocks we've already generated resources for
	private static final Set<String> generatedFences = new HashSet<>();
	private static final Set<String> generatedWalls = new HashSet<>();

	// Known vanilla blocks (already handled by static resources)
	private static final Set<String> VANILLA_FENCES = Set.of(
		"minecraft:oak_fence", "minecraft:spruce_fence", "minecraft:birch_fence",
		"minecraft:jungle_fence", "minecraft:acacia_fence", "minecraft:dark_oak_fence",
		"minecraft:mangrove_fence", "minecraft:cherry_fence", "minecraft:bamboo_fence",
		"minecraft:crimson_fence", "minecraft:warped_fence", "minecraft:pale_oak_fence",
		"minecraft:nether_brick_fence"
	);

	private static final Set<String> VANILLA_WALLS = Set.of(
		"minecraft:cobblestone_wall", "minecraft:mossy_cobblestone_wall",
		"minecraft:stone_brick_wall", "minecraft:mossy_stone_brick_wall",
		"minecraft:brick_wall", "minecraft:mud_brick_wall",
		"minecraft:sandstone_wall", "minecraft:red_sandstone_wall",
		"minecraft:granite_wall", "minecraft:diorite_wall", "minecraft:andesite_wall",
		"minecraft:prismarine_wall", "minecraft:nether_brick_wall",
		"minecraft:red_nether_brick_wall", "minecraft:end_stone_brick_wall",
		"minecraft:blackstone_wall", "minecraft:polished_blackstone_wall",
		"minecraft:polished_blackstone_brick_wall", "minecraft:cobbled_deepslate_wall",
		"minecraft:polished_deepslate_wall", "minecraft:deepslate_brick_wall",
		"minecraft:deepslate_tile_wall", "minecraft:tuff_wall",
		"minecraft:polished_tuff_wall", "minecraft:tuff_brick_wall"
	);

	/**
	 * Scan the registry and return info about modded fences and walls.
	 */
	public static List<FenceInfo> discoverModdedFences() {
		List<FenceInfo> fences = new ArrayList<>();

		for (Block block : Registries.BLOCK) {
			if (block instanceof FenceBlock) {
				Identifier id = Registries.BLOCK.getId(block);
				String fullId = id.toString();

				// Skip vanilla fences (already have static resources)
				if (VANILLA_FENCES.contains(fullId)) continue;

				// Skip if already generated
				if (generatedFences.contains(fullId)) continue;

				fences.add(new FenceInfo(id, block));
				generatedFences.add(fullId);
			}
		}

		return fences;
	}

	/**
	 * Scan the registry and return info about modded walls.
	 */
	public static List<WallInfo> discoverModdedWalls() {
		List<WallInfo> walls = new ArrayList<>();

		for (Block block : Registries.BLOCK) {
			if (block instanceof WallBlock) {
				Identifier id = Registries.BLOCK.getId(block);
				String fullId = id.toString();

				// Skip vanilla walls (already have static resources)
				if (VANILLA_WALLS.contains(fullId)) continue;

				// Skip if already generated
				if (generatedWalls.contains(fullId)) continue;

				walls.add(new WallInfo(id, block));
				generatedWalls.add(fullId);
			}
		}

		return walls;
	}

	/**
	 * Generate all resource files for a fence post and its slab variant.
	 */
	public static void generateFencePostResources(Path outputPath, String baseName, String namespace, String texturePath) throws IOException {
		String postName = baseName + "_fence_post";
		String slabName = baseName + "_fence_post_slab";

		// Block models
		writeJson(outputPath.resolve("assets/" + MOD_ID + "/models/block/" + postName + ".json"),
			createChildModel(MOD_ID + ":block/parent/fence_post", texturePath));
		writeJson(outputPath.resolve("assets/" + MOD_ID + "/models/block/" + slabName + "_bottom.json"),
			createChildModel(MOD_ID + ":block/parent/fence_post_slab_bottom", texturePath));
		writeJson(outputPath.resolve("assets/" + MOD_ID + "/models/block/" + slabName + "_top.json"),
			createChildModel(MOD_ID + ":block/parent/fence_post_slab_top", texturePath));
		writeJson(outputPath.resolve("assets/" + MOD_ID + "/models/block/" + slabName + "_double.json"),
			createChildModel(MOD_ID + ":block/parent/fence_post", texturePath));

		// Blockstates
		writeJson(outputPath.resolve("assets/" + MOD_ID + "/blockstates/" + postName + ".json"),
			createSimpleBlockstate(postName));
		writeJson(outputPath.resolve("assets/" + MOD_ID + "/blockstates/" + slabName + ".json"),
			createSlabBlockstate(slabName));

		// Item models
		writeJson(outputPath.resolve("assets/" + MOD_ID + "/models/item/" + postName + ".json"),
			createItemModel(postName));
		writeJson(outputPath.resolve("assets/" + MOD_ID + "/models/item/" + slabName + ".json"),
			createItemModel(slabName + "_bottom"));

		// Item definitions
		writeJson(outputPath.resolve("assets/" + MOD_ID + "/items/" + postName + ".json"),
			createItemDefinition(postName));
		writeJson(outputPath.resolve("assets/" + MOD_ID + "/items/" + slabName + ".json"),
			createItemDefinition(slabName));

		// Recipes
		writeJson(outputPath.resolve("data/" + MOD_ID + "/recipe/" + postName + ".json"),
			createFencePostRecipe(postName, namespace, baseName));
		writeJson(outputPath.resolve("data/" + MOD_ID + "/recipe/" + slabName + ".json"),
			createSlabRecipe(slabName, postName));

		// Loot tables
		writeJson(outputPath.resolve("data/" + MOD_ID + "/loot_table/blocks/" + postName + ".json"),
			createLootTable(postName));
		writeJson(outputPath.resolve("data/" + MOD_ID + "/loot_table/blocks/" + slabName + ".json"),
			createSlabLootTable(slabName));
	}

	/**
	 * Generate all resource files for a wall post and its slab variant.
	 */
	public static void generateWallPostResources(Path outputPath, String baseName, String namespace, String texturePath) throws IOException {
		String postName = baseName + "_wall_post";
		String slabName = baseName + "_wall_post_slab";

		// Block models
		writeJson(outputPath.resolve("assets/" + MOD_ID + "/models/block/" + postName + ".json"),
			createChildModel(MOD_ID + ":block/parent/wall_post", texturePath));
		writeJson(outputPath.resolve("assets/" + MOD_ID + "/models/block/" + slabName + "_bottom.json"),
			createChildModel(MOD_ID + ":block/parent/wall_post_slab_bottom", texturePath));
		writeJson(outputPath.resolve("assets/" + MOD_ID + "/models/block/" + slabName + "_top.json"),
			createChildModel(MOD_ID + ":block/parent/wall_post_slab_top", texturePath));
		writeJson(outputPath.resolve("assets/" + MOD_ID + "/models/block/" + slabName + "_double.json"),
			createChildModel(MOD_ID + ":block/parent/wall_post", texturePath));

		// Blockstates
		writeJson(outputPath.resolve("assets/" + MOD_ID + "/blockstates/" + postName + ".json"),
			createSimpleBlockstate(postName));
		writeJson(outputPath.resolve("assets/" + MOD_ID + "/blockstates/" + slabName + ".json"),
			createSlabBlockstate(slabName));

		// Item models
		writeJson(outputPath.resolve("assets/" + MOD_ID + "/models/item/" + postName + ".json"),
			createItemModel(postName));
		writeJson(outputPath.resolve("assets/" + MOD_ID + "/models/item/" + slabName + ".json"),
			createItemModel(slabName + "_bottom"));

		// Item definitions
		writeJson(outputPath.resolve("assets/" + MOD_ID + "/items/" + postName + ".json"),
			createItemDefinition(postName));
		writeJson(outputPath.resolve("assets/" + MOD_ID + "/items/" + slabName + ".json"),
			createItemDefinition(slabName));

		// Recipes
		writeJson(outputPath.resolve("data/" + MOD_ID + "/recipe/" + postName + ".json"),
			createWallPostRecipe(postName, namespace, baseName));
		writeJson(outputPath.resolve("data/" + MOD_ID + "/recipe/" + slabName + ".json"),
			createSlabRecipe(slabName, postName));

		// Loot tables
		writeJson(outputPath.resolve("data/" + MOD_ID + "/loot_table/blocks/" + postName + ".json"),
			createLootTable(postName));
		writeJson(outputPath.resolve("data/" + MOD_ID + "/loot_table/blocks/" + slabName + ".json"),
			createSlabLootTable(slabName));
	}

	// JSON generation helpers

	private static JsonObject createChildModel(String parent, String texture) {
		JsonObject obj = new JsonObject();
		obj.addProperty("parent", parent);
		JsonObject textures = new JsonObject();
		textures.addProperty("texture", texture);
		obj.add("textures", textures);
		return obj;
	}

	private static JsonObject createSimpleBlockstate(String name) {
		JsonObject obj = new JsonObject();
		JsonObject variants = new JsonObject();
		JsonObject modelRef = new JsonObject();
		modelRef.addProperty("model", MOD_ID + ":block/" + name);
		variants.add("waterlogged=false", modelRef);
		variants.add("waterlogged=true", modelRef);
		obj.add("variants", variants);
		return obj;
	}

	private static JsonObject createSlabBlockstate(String name) {
		JsonObject obj = new JsonObject();
		JsonObject variants = new JsonObject();

		String[] types = {"bottom", "top", "double"};
		for (String type : types) {
			JsonObject modelRef = new JsonObject();
			modelRef.addProperty("model", MOD_ID + ":block/" + name + "_" + type);
			variants.add("type=" + type + ",waterlogged=false", modelRef);
			variants.add("type=" + type + ",waterlogged=true", modelRef);
		}

		obj.add("variants", variants);
		return obj;
	}

	private static JsonObject createItemModel(String blockName) {
		JsonObject obj = new JsonObject();
		obj.addProperty("parent", MOD_ID + ":block/" + blockName);
		return obj;
	}

	private static JsonObject createItemDefinition(String name) {
		JsonObject obj = new JsonObject();
		JsonObject model = new JsonObject();
		model.addProperty("type", "minecraft:model");
		model.addProperty("model", MOD_ID + ":item/" + name);
		obj.add("model", model);
		return obj;
	}

	private static JsonObject createFencePostRecipe(String postName, String namespace, String baseName) {
		JsonObject obj = new JsonObject();
		obj.addProperty("type", "minecraft:crafting_shapeless");
		JsonArray ingredients = new JsonArray();
		ingredients.add(namespace + ":" + baseName + "_fence");
		obj.add("ingredients", ingredients);
		JsonObject result = new JsonObject();
		result.addProperty("id", MOD_ID + ":" + postName);
		result.addProperty("count", 2);
		obj.add("result", result);
		return obj;
	}

	private static JsonObject createWallPostRecipe(String postName, String namespace, String baseName) {
		JsonObject obj = new JsonObject();
		obj.addProperty("type", "minecraft:crafting_shapeless");
		JsonArray ingredients = new JsonArray();
		ingredients.add(namespace + ":" + baseName + "_wall");
		obj.add("ingredients", ingredients);
		JsonObject result = new JsonObject();
		result.addProperty("id", MOD_ID + ":" + postName);
		result.addProperty("count", 2);
		obj.add("result", result);
		return obj;
	}

	private static JsonObject createSlabRecipe(String slabName, String postName) {
		JsonObject obj = new JsonObject();
		obj.addProperty("type", "minecraft:crafting_shapeless");
		JsonArray ingredients = new JsonArray();
		ingredients.add(MOD_ID + ":" + postName);
		obj.add("ingredients", ingredients);
		JsonObject result = new JsonObject();
		result.addProperty("id", MOD_ID + ":" + slabName);
		result.addProperty("count", 2);
		obj.add("result", result);
		return obj;
	}

	private static JsonObject createLootTable(String name) {
		JsonObject obj = new JsonObject();
		obj.addProperty("type", "minecraft:block");
		JsonArray pools = new JsonArray();
		JsonObject pool = new JsonObject();
		pool.addProperty("rolls", 1);
		JsonArray entries = new JsonArray();
		JsonObject entry = new JsonObject();
		entry.addProperty("type", "minecraft:item");
		entry.addProperty("name", MOD_ID + ":" + name);
		entries.add(entry);
		pool.add("entries", entries);
		JsonArray conditions = new JsonArray();
		JsonObject condition = new JsonObject();
		condition.addProperty("condition", "minecraft:survives_explosion");
		conditions.add(condition);
		pool.add("conditions", conditions);
		pools.add(pool);
		obj.add("pools", pools);
		return obj;
	}

	private static JsonObject createSlabLootTable(String name) {
		JsonObject obj = new JsonObject();
		obj.addProperty("type", "minecraft:block");
		JsonArray pools = new JsonArray();
		JsonObject pool = new JsonObject();
		pool.addProperty("rolls", 1);

		JsonArray entries = new JsonArray();
		JsonObject entry = new JsonObject();
		entry.addProperty("type", "minecraft:item");
		entry.addProperty("name", MOD_ID + ":" + name);

		JsonArray functions = new JsonArray();
		JsonObject setCount = new JsonObject();
		setCount.addProperty("function", "minecraft:set_count");
		setCount.addProperty("count", 2);
		JsonArray funcConditions = new JsonArray();
		JsonObject blockState = new JsonObject();
		blockState.addProperty("condition", "minecraft:block_state_property");
		blockState.addProperty("block", MOD_ID + ":" + name);
		JsonObject props = new JsonObject();
		props.addProperty("type", "double");
		blockState.add("properties", props);
		funcConditions.add(blockState);
		setCount.add("conditions", funcConditions);
		functions.add(setCount);
		entry.add("functions", functions);

		entries.add(entry);
		pool.add("entries", entries);

		JsonArray conditions = new JsonArray();
		JsonObject condition = new JsonObject();
		condition.addProperty("condition", "minecraft:survives_explosion");
		conditions.add(condition);
		pool.add("conditions", conditions);

		pools.add(pool);
		obj.add("pools", pools);
		return obj;
	}

	private static void writeJson(Path path, JsonObject json) throws IOException {
		Files.createDirectories(path.getParent());
		try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
			GSON.toJson(json, writer);
		}
	}

	/**
	 * Try to determine the texture path for a fence block.
	 * This uses heuristics based on common naming conventions.
	 */
	public static String guessFenceTexture(Identifier fenceId) {
		String namespace = fenceId.getNamespace();
		String path = fenceId.getPath();

		// Remove "_fence" suffix to get base name
		String baseName = path.replace("_fence", "");

		// Common patterns for fence textures
		// Most fences use planks texture
		return namespace + ":block/" + baseName + "_planks";
	}

	/**
	 * Try to determine the texture path for a wall block.
	 */
	public static String guessWallTexture(Identifier wallId) {
		String namespace = wallId.getNamespace();
		String path = wallId.getPath();

		// Remove "_wall" suffix
		String baseName = path.replace("_wall", "");

		// Wall textures are usually the block name itself
		return namespace + ":block/" + baseName;
	}

	// Info classes

	public record FenceInfo(Identifier id, Block block) {
		public String getBaseName() {
			return id.getPath().replace("_fence", "");
		}
	}

	public record WallInfo(Identifier id, Block block) {
		public String getBaseName() {
			return id.getPath().replace("_wall", "");
		}
	}
}
