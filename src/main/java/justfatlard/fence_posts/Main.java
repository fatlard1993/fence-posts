package justfatlard.fence_posts;

import justfatlard.pandorical.api.BlockRegistration;
import justfatlard.pandorical.api.ItemRegistration;
import justfatlard.pandorical.api.PandoricalApi;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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

	private static final Set<String> registeredPosts = new HashSet<>();

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

		// Item group
		CreativeModeTab postGroup = FabricCreativeModeTab.builder()
			.title(Component.literal("Fence & Wall Posts"))
			.icon(() -> FENCE_POSTS.isEmpty() ? ItemStack.EMPTY : new ItemStack(FENCE_POSTS.get(0)))
			.displayItems((context, entries) -> {
				for (int i = 0; i < FENCE_POSTS.size(); i++) {
					entries.accept(new ItemStack(FENCE_POSTS.get(i)));
					if (i < FENCE_POST_SLABS.size()) {
						entries.accept(new ItemStack(FENCE_POST_SLABS.get(i)));
					}
				}
				for (int i = 0; i < WALL_POSTS.size(); i++) {
					entries.accept(new ItemStack(WALL_POSTS.get(i)));
					if (i < WALL_POST_SLABS.size()) {
						entries.accept(new ItemStack(WALL_POST_SLABS.get(i)));
					}
				}
			})
			.build();

		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(MOD_ID, "posts"), postGroup);

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
		String postName = baseName + "_fence_post";
		String slabName = baseName + "_fence_post_slab";
		String vanillaBaseId = "minecraft:" + baseName + "_fence";

		if (registeredPosts.contains(postName)) {
			return null;
		}
		registeredPosts.add(postName);

		registerPandoricalContent(postName, vanillaBaseId, false);
		registerPandoricalContent(slabName, vanillaBaseId, true);

		// Full post
		ResourceKey<Block> postBlockKey = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, postName));
		BlockBehaviour.Properties postSettings = BlockBehaviour.Properties.of()
			.setId(postBlockKey)
			.strength(hardness, resistance)
			.sound(soundGroup)
			.mapColor(mapColor)
			.noOcclusion();
		if (burnable) postSettings = postSettings.ignitedByLava();
		if (requiresTool) postSettings = postSettings.requiresCorrectToolForDrops();

		PostBlock post = new PostBlock(postSettings, FENCE_POST_INSET);
		Registry.register(BuiltInRegistries.BLOCK, postBlockKey.identifier(), post);

		ResourceKey<Item> postItemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, postName));
		Registry.register(BuiltInRegistries.ITEM, postItemKey.identifier(),
			new BlockItem(post, new Item.Properties().setId(postItemKey)));
		FENCE_POSTS.add(post);

		// Slab
		ResourceKey<Block> slabBlockKey = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, slabName));
		BlockBehaviour.Properties slabSettings = BlockBehaviour.Properties.of()
			.setId(slabBlockKey)
			.strength(hardness, resistance)
			.sound(soundGroup)
			.mapColor(mapColor)
			.noOcclusion();
		if (burnable) slabSettings = slabSettings.ignitedByLava();
		if (requiresTool) slabSettings = slabSettings.requiresCorrectToolForDrops();

		PostSlabBlock slab = new PostSlabBlock(slabSettings, FENCE_POST_INSET);
		Registry.register(BuiltInRegistries.BLOCK, slabBlockKey.identifier(), slab);

		ResourceKey<Item> slabItemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, slabName));
		Registry.register(BuiltInRegistries.ITEM, slabItemKey.identifier(),
			new BlockItem(slab, new Item.Properties().setId(slabItemKey)));
		FENCE_POST_SLABS.add(slab);

		// Flammability -- wooden posts should spread fire like vanilla fences
		if (burnable) {
			FlammableBlockRegistry.getDefaultInstance().add(post, 5, 20);
			FlammableBlockRegistry.getDefaultInstance().add(slab, 5, 20);
		}

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
		String postName = baseName + "_wall_post";
		String slabName = baseName + "_wall_post_slab";
		String vanillaBaseId = "minecraft:" + baseName + "_wall";

		if (registeredPosts.contains(postName)) {
			return null;
		}
		registeredPosts.add(postName);

		registerPandoricalContent(postName, vanillaBaseId, false);
		registerPandoricalContent(slabName, vanillaBaseId, true);

		// Full post
		ResourceKey<Block> postBlockKey = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, postName));
		BlockBehaviour.Properties postSettings = BlockBehaviour.Properties.of()
			.setId(postBlockKey)
			.strength(hardness, resistance)
			.sound(soundGroup)
			.mapColor(mapColor)
			.noOcclusion()
			.requiresCorrectToolForDrops();

		PostBlock post = new PostBlock(postSettings, WALL_POST_INSET);
		Registry.register(BuiltInRegistries.BLOCK, postBlockKey.identifier(), post);

		ResourceKey<Item> postItemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, postName));
		Registry.register(BuiltInRegistries.ITEM, postItemKey.identifier(),
			new BlockItem(post, new Item.Properties().setId(postItemKey)));
		WALL_POSTS.add(post);

		// Slab
		ResourceKey<Block> slabBlockKey = ResourceKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MOD_ID, slabName));
		BlockBehaviour.Properties slabSettings = BlockBehaviour.Properties.of()
			.setId(slabBlockKey)
			.strength(hardness, resistance)
			.sound(soundGroup)
			.mapColor(mapColor)
			.noOcclusion()
			.requiresCorrectToolForDrops();

		PostSlabBlock slab = new PostSlabBlock(slabSettings, WALL_POST_INSET);
		Registry.register(BuiltInRegistries.BLOCK, slabBlockKey.identifier(), slab);

		ResourceKey<Item> slabItemKey = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, slabName));
		Registry.register(BuiltInRegistries.ITEM, slabItemKey.identifier(),
			new BlockItem(slab, new Item.Properties().setId(slabItemKey)));
		WALL_POST_SLABS.add(slab);

		return post;
	}
}
