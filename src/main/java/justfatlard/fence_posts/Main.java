package justfatlard.fence_posts;

import justfatlard.pandorical.api.BlockRegistration;
import justfatlard.pandorical.api.ItemRegistration;
import justfatlard.pandorical.api.PandoricalApi;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main implements ModInitializer {
	public static final String MOD_ID = "fence-posts";
	private static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final double FENCE_POST_INSET = 6.0;
	private static final double WALL_POST_INSET = 4.0;

	private static final List<Block> FENCE_POSTS = new ArrayList<>();
	private static final List<Block> FENCE_POST_SLABS = new ArrayList<>();
	private static final List<Block> WALL_POSTS = new ArrayList<>();
	private static final List<Block> WALL_POST_SLABS = new ArrayList<>();

	private static final Set<String> registered = new HashSet<>();

	public static List<Block> getFencePosts() { return Collections.unmodifiableList(FENCE_POSTS); }
	public static List<Block> getFencePostSlabs() { return Collections.unmodifiableList(FENCE_POST_SLABS); }
	public static List<Block> getWallPosts() { return Collections.unmodifiableList(WALL_POSTS); }
	public static List<Block> getWallPostSlabs() { return Collections.unmodifiableList(WALL_POST_SLABS); }

	@Override
	public void onInitialize() {
		// Fence posts -- wood types (axe-mineable, no tool required, burnable)
		registerFencePost("oak", SoundType.WOOD, true, false, 2.0f, 3.0f, MapColor.WOOD);
		registerFencePost("spruce", SoundType.WOOD, true, false, 2.0f, 3.0f, MapColor.PODZOL);
		registerFencePost("birch", SoundType.WOOD, true, false, 2.0f, 3.0f, MapColor.SAND);
		registerFencePost("jungle", SoundType.WOOD, true, false, 2.0f, 3.0f, MapColor.DIRT);
		registerFencePost("acacia", SoundType.WOOD, true, false, 2.0f, 3.0f, MapColor.COLOR_ORANGE);
		registerFencePost("dark_oak", SoundType.WOOD, true, false, 2.0f, 3.0f, MapColor.COLOR_BROWN);
		registerFencePost("mangrove", SoundType.WOOD, true, false, 2.0f, 3.0f, MapColor.COLOR_RED);
		registerFencePost("cherry", SoundType.CHERRY_WOOD, true, false, 2.0f, 3.0f, MapColor.TERRACOTTA_WHITE);
		registerFencePost("bamboo", SoundType.BAMBOO_WOOD, true, false, 2.0f, 3.0f, MapColor.COLOR_YELLOW);
		registerFencePost("crimson", SoundType.NETHER_WOOD, false, false, 2.0f, 3.0f, MapColor.CRIMSON_STEM);
		registerFencePost("warped", SoundType.NETHER_WOOD, false, false, 2.0f, 3.0f, MapColor.WARPED_STEM);
		registerFencePost("pale_oak", SoundType.WOOD, true, false, 2.0f, 3.0f, MapColor.WOOD);
		// Nether brick fence post -- pickaxe-mineable, requires tool
		registerFencePost("nether_brick", SoundType.NETHER_BRICKS, false, true, 2.0f, 6.0f, MapColor.NETHER);

		// Wall posts -- all pickaxe-mineable, require tool
		registerWallPost("cobblestone", SoundType.STONE, 2.0f, 6.0f, MapColor.STONE);
		registerWallPost("mossy_cobblestone", SoundType.STONE, 2.0f, 6.0f, MapColor.STONE);
		registerWallPost("stone_brick", SoundType.STONE, 1.5f, 6.0f, MapColor.STONE);
		registerWallPost("mossy_stone_brick", SoundType.STONE, 1.5f, 6.0f, MapColor.STONE);
		registerWallPost("brick", SoundType.STONE, 2.0f, 6.0f, MapColor.FIRE);
		registerWallPost("mud_brick", SoundType.MUD_BRICKS, 1.5f, 3.0f, MapColor.TERRACOTTA_LIGHT_GRAY);
		registerWallPost("sandstone", SoundType.STONE, 0.8f, 0.8f, MapColor.SAND);
		registerWallPost("red_sandstone", SoundType.STONE, 0.8f, 0.8f, MapColor.COLOR_ORANGE);
		registerWallPost("granite", SoundType.STONE, 1.5f, 6.0f, MapColor.DIRT);
		registerWallPost("diorite", SoundType.STONE, 1.5f, 6.0f, MapColor.QUARTZ);
		registerWallPost("andesite", SoundType.STONE, 1.5f, 6.0f, MapColor.STONE);
		registerWallPost("prismarine", SoundType.STONE, 1.5f, 6.0f, MapColor.COLOR_CYAN);
		registerWallPost("nether_brick", SoundType.NETHER_BRICKS, 2.0f, 6.0f, MapColor.NETHER);
		registerWallPost("red_nether_brick", SoundType.NETHER_BRICKS, 2.0f, 6.0f, MapColor.NETHER);
		registerWallPost("end_stone_brick", SoundType.STONE, 3.0f, 9.0f, MapColor.SAND);
		registerWallPost("blackstone", SoundType.STONE, 1.5f, 6.0f, MapColor.COLOR_BLACK);
		registerWallPost("polished_blackstone", SoundType.STONE, 2.0f, 6.0f, MapColor.COLOR_BLACK);
		registerWallPost("polished_blackstone_brick", SoundType.STONE, 1.5f, 6.0f, MapColor.COLOR_BLACK);
		registerWallPost("cobbled_deepslate", SoundType.DEEPSLATE, 3.5f, 6.0f, MapColor.DEEPSLATE);
		registerWallPost("polished_deepslate", SoundType.POLISHED_DEEPSLATE, 3.5f, 6.0f, MapColor.DEEPSLATE);
		registerWallPost("deepslate_brick", SoundType.DEEPSLATE_BRICKS, 3.5f, 6.0f, MapColor.DEEPSLATE);
		registerWallPost("deepslate_tile", SoundType.DEEPSLATE_TILES, 3.5f, 6.0f, MapColor.DEEPSLATE);
		registerWallPost("tuff", SoundType.TUFF, 1.5f, 6.0f, MapColor.TERRACOTTA_GRAY);
		registerWallPost("polished_tuff", SoundType.POLISHED_TUFF, 1.5f, 6.0f, MapColor.TERRACOTTA_GRAY);
		registerWallPost("tuff_brick", SoundType.TUFF_BRICKS, 1.5f, 6.0f, MapColor.TERRACOTTA_GRAY);

		// The game's fences and walls that never had a crafted post: a slab, and no legacy post to keep.
		registerFencePostSlab("poplar", SoundType.WOOD, true, false, 2.0f, 3.0f, MapColor.COLOR_LIGHT_GRAY);
		registerWallPostSlab("resin_brick", SoundType.RESIN_BRICKS, 1.5f, 6.0f, MapColor.TERRACOTTA_ORANGE);
		registerWallPostSlab("cinnabar", SoundType.CINNABAR, 1.5f, 6.0f, MapColor.COLOR_RED);
		registerWallPostSlab("cinnabar_brick", SoundType.CINNABAR, 1.5f, 6.0f, MapColor.COLOR_RED);
		registerWallPostSlab("polished_cinnabar", SoundType.CINNABAR, 1.5f, 6.0f, MapColor.COLOR_RED);
		registerWallPostSlab("sulfur", SoundType.SULFUR, 1.5f, 6.0f, MapColor.COLOR_YELLOW);
		registerWallPostSlab("sulfur_brick", SoundType.SULFUR, 1.5f, 6.0f, MapColor.COLOR_YELLOW);
		registerWallPostSlab("polished_sulfur", SoundType.SULFUR, 1.5f, 6.0f, MapColor.COLOR_YELLOW);

		// The crafted posts are kept so a world that has them keeps them; there is no recipe and
		// no creative entry for them any more. A post is a fence told to stand alone, see PostToggle.
		PostToggle.register();
		// A lead from fence to fence, held at the near end by a golem nobody sees; see FenceLeads.
		FenceLeads.register();

		PandoricalApi.content().registerModAssets(MOD_ID);

		int total = FENCE_POSTS.size() + FENCE_POST_SLABS.size() + WALL_POSTS.size() + WALL_POST_SLABS.size();
		LOGGER.info("Registered {} blocks ({} fence posts, {} fence post slabs, {} wall posts, {} wall post slabs)",
			total, FENCE_POSTS.size(), FENCE_POST_SLABS.size(), WALL_POSTS.size(), WALL_POST_SLABS.size());
	}

	/**
	 * Mirror a post (or post slab) and its BlockItem into Pandorical's content registry.
	 * Blocks that exist only in vanilla's registries reach Pandorical clients through
	 * auto-detection, which guesses a base block from the sound type; naming the real
	 * vanilla counterpart gives the client the right properties for the block it builds.
	 *
	 * @param name Registry path, e.g. "oak_fence_post" or "oak_fence_post_slab"
	 * @param vanillaBaseId Vanilla block to copy properties from, e.g. "minecraft:oak_fence"
	 * @param slab Whether this is the half-height slab variant
	 */
	private static void registerPandoricalContent(String name, String vanillaBaseId, boolean slab) {
		if (!PandoricalApi.isAvailable()) return;

		BlockRegistration block = new BlockRegistration()
			.baseBlock(vanillaBaseId)
			.property("waterlogged")
			.model(MOD_ID + ":block/" + name + (slab ? "_bottom" : ""));
		if (slab) block.property("type");

		PandoricalApi.content().registerBlock(MOD_ID + ":" + name, block);
		PandoricalApi.content().registerItem(MOD_ID + ":" + name, new ItemRegistration()
			.model(MOD_ID + ":item/" + name));
	}

	/**
	 * Register a fence post and its slab variant.
	 *
	 * @param baseName Base name (e.g., "oak" for "oak_fence_post")
	 * @param soundGroup Sound group for this material
	 * @param burnable Whether this post can burn and spread fire
	 * @param requiresTool Whether this post requires the correct tool to drop
	 * @param hardness Block hardness (mining speed)
	 * @param resistance Blast resistance
	 * @param mapColor Color shown on maps
	 * @return The registered PostBlock, or null if already registered
	 */
	public static PostBlock registerFencePost(String baseName, SoundType soundGroup, boolean burnable, boolean requiresTool, float hardness, float resistance, MapColor mapColor) {
		PostBlock post = registerPost(baseName + "_fence_post", "minecraft:" + baseName + "_fence", FENCE_POST_INSET, soundGroup, burnable, requiresTool, hardness, resistance, mapColor, FENCE_POSTS);
		if (post != null) registerFencePostSlab(baseName, soundGroup, burnable, requiresTool, hardness, resistance, mapColor);
		return post;
	}

	/**
	 * Register a wall post and its slab variant.
	 * All wall posts require a pickaxe to drop.
	 *
	 * @param baseName Base name (e.g., "cobblestone" for "cobblestone_wall_post")
	 * @param soundGroup Sound group for this material
	 * @param hardness Block hardness (mining speed)
	 * @param resistance Blast resistance
	 * @param mapColor Color shown on maps
	 * @return The registered PostBlock, or null if already registered
	 */
	public static PostBlock registerWallPost(String baseName, SoundType soundGroup, float hardness, float resistance, MapColor mapColor) {
		PostBlock post = registerPost(baseName + "_wall_post", "minecraft:" + baseName + "_wall", WALL_POST_INSET, soundGroup, false, true, hardness, resistance, mapColor, WALL_POSTS);
		if (post != null) registerWallPostSlab(baseName, soundGroup, hardness, resistance, mapColor);
		return post;
	}

	private static PostSlabBlock registerFencePostSlab(String baseName, SoundType soundGroup, boolean burnable, boolean requiresTool, float hardness, float resistance, MapColor mapColor) {
		return registerSlab(baseName + "_fence_post_slab", "minecraft:" + baseName + "_fence", FENCE_POST_INSET, soundGroup, burnable, requiresTool, hardness, resistance, mapColor, FENCE_POST_SLABS);
	}

	private static PostSlabBlock registerWallPostSlab(String baseName, SoundType soundGroup, float hardness, float resistance, MapColor mapColor) {
		return registerSlab(baseName + "_wall_post_slab", "minecraft:" + baseName + "_wall", WALL_POST_INSET, soundGroup, false, true, hardness, resistance, mapColor, WALL_POST_SLABS);
	}

	private static PostBlock registerPost(String name, String vanillaBaseId, double inset, SoundType soundGroup, boolean burnable, boolean requiresTool, float hardness, float resistance, MapColor mapColor, List<Block> into) {
		if (!registered.add(name)) return null;
		registerPandoricalContent(name, vanillaBaseId, false);

		ResourceKey<Block> key = blockKey(name);
		PostBlock post = new PostBlock(properties(key, soundGroup, burnable, requiresTool, hardness, resistance, mapColor), inset);
		registerWithItem(key, post, burnable, into);
		return post;
	}

	private static PostSlabBlock registerSlab(String name, String vanillaBaseId, double inset, SoundType soundGroup, boolean burnable, boolean requiresTool, float hardness, float resistance, MapColor mapColor, List<Block> into) {
		if (!registered.add(name)) return null;
		registerPandoricalContent(name, vanillaBaseId, true);

		ResourceKey<Block> key = blockKey(name);
		PostSlabBlock slab = new PostSlabBlock(properties(key, soundGroup, burnable, requiresTool, hardness, resistance, mapColor), inset);
		registerWithItem(key, slab, burnable, into);
		return slab;
	}

	private static ResourceKey<Block> blockKey(String name) {
		return ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, name));
	}

	private static BlockBehaviour.Properties properties(ResourceKey<Block> key, SoundType soundGroup, boolean burnable, boolean requiresTool, float hardness, float resistance, MapColor mapColor) {
		BlockBehaviour.Properties settings = BlockBehaviour.Properties.of()
			.setId(key)
			.strength(hardness, resistance)
			.sound(soundGroup)
			.mapColor(mapColor)
			.noOcclusion();
		if (burnable) settings = settings.ignitedByLava();
		if (requiresTool) settings = settings.requiresCorrectToolForDrops();
		return settings;
	}

	private static void registerWithItem(ResourceKey<Block> key, Block block, boolean burnable, List<Block> into) {
		Registry.register(BuiltInRegistries.BLOCK, key.identifier(), block);
		ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, key.identifier());
		Registry.register(BuiltInRegistries.ITEM, itemKey.identifier(), new BlockItem(block, new Item.Properties().setId(itemKey)));
		into.add(block);
		// Wooden posts spread fire like vanilla fences
		if (burnable) FlammableBlockRegistry.getDefaultInstance().add(block, 5, 20);
	}
}
