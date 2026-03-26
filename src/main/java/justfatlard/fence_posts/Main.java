package justfatlard.fence_posts;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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
		registerFencePost("oak", BlockSoundGroup.WOOD, true, false, 2.0f, 3.0f, MapColor.OAK_TAN);
		registerFencePost("spruce", BlockSoundGroup.WOOD, true, false, 2.0f, 3.0f, MapColor.SPRUCE_BROWN);
		registerFencePost("birch", BlockSoundGroup.WOOD, true, false, 2.0f, 3.0f, MapColor.PALE_YELLOW);
		registerFencePost("jungle", BlockSoundGroup.WOOD, true, false, 2.0f, 3.0f, MapColor.DIRT_BROWN);
		registerFencePost("acacia", BlockSoundGroup.WOOD, true, false, 2.0f, 3.0f, MapColor.ORANGE);
		registerFencePost("dark_oak", BlockSoundGroup.WOOD, true, false, 2.0f, 3.0f, MapColor.BROWN);
		registerFencePost("mangrove", BlockSoundGroup.WOOD, true, false, 2.0f, 3.0f, MapColor.RED);
		registerFencePost("cherry", BlockSoundGroup.CHERRY_WOOD, true, false, 2.0f, 3.0f, MapColor.TERRACOTTA_WHITE);
		registerFencePost("bamboo", BlockSoundGroup.BAMBOO_WOOD, true, false, 2.0f, 3.0f, MapColor.YELLOW);
		registerFencePost("crimson", BlockSoundGroup.NETHER_WOOD, false, false, 2.0f, 3.0f, MapColor.DULL_PINK);
		registerFencePost("warped", BlockSoundGroup.NETHER_WOOD, false, false, 2.0f, 3.0f, MapColor.DARK_AQUA);
		registerFencePost("pale_oak", BlockSoundGroup.WOOD, true, false, 2.0f, 3.0f, MapColor.OAK_TAN);
		// Nether brick fence post -- pickaxe-mineable, requires tool
		registerFencePost("nether_brick", BlockSoundGroup.NETHER_BRICKS, false, true, 2.0f, 6.0f, MapColor.DARK_RED);

		// Wall posts -- all pickaxe-mineable, require tool
		registerWallPost("cobblestone", BlockSoundGroup.STONE, 2.0f, 6.0f, MapColor.STONE_GRAY);
		registerWallPost("mossy_cobblestone", BlockSoundGroup.STONE, 2.0f, 6.0f, MapColor.STONE_GRAY);
		registerWallPost("stone_brick", BlockSoundGroup.STONE, 1.5f, 6.0f, MapColor.STONE_GRAY);
		registerWallPost("mossy_stone_brick", BlockSoundGroup.STONE, 1.5f, 6.0f, MapColor.STONE_GRAY);
		registerWallPost("brick", BlockSoundGroup.STONE, 2.0f, 6.0f, MapColor.BRIGHT_RED);
		registerWallPost("mud_brick", BlockSoundGroup.MUD_BRICKS, 1.5f, 3.0f, MapColor.TERRACOTTA_LIGHT_GRAY);
		registerWallPost("sandstone", BlockSoundGroup.STONE, 0.8f, 0.8f, MapColor.PALE_YELLOW);
		registerWallPost("red_sandstone", BlockSoundGroup.STONE, 0.8f, 0.8f, MapColor.ORANGE);
		registerWallPost("granite", BlockSoundGroup.STONE, 1.5f, 6.0f, MapColor.DIRT_BROWN);
		registerWallPost("diorite", BlockSoundGroup.STONE, 1.5f, 6.0f, MapColor.OFF_WHITE);
		registerWallPost("andesite", BlockSoundGroup.STONE, 1.5f, 6.0f, MapColor.STONE_GRAY);
		registerWallPost("prismarine", BlockSoundGroup.STONE, 1.5f, 6.0f, MapColor.CYAN);
		registerWallPost("nether_brick", BlockSoundGroup.NETHER_BRICKS, 2.0f, 6.0f, MapColor.DARK_RED);
		registerWallPost("red_nether_brick", BlockSoundGroup.NETHER_BRICKS, 2.0f, 6.0f, MapColor.DARK_RED);
		registerWallPost("end_stone_brick", BlockSoundGroup.STONE, 3.0f, 9.0f, MapColor.PALE_YELLOW);
		registerWallPost("blackstone", BlockSoundGroup.STONE, 1.5f, 6.0f, MapColor.BLACK);
		registerWallPost("polished_blackstone", BlockSoundGroup.STONE, 2.0f, 6.0f, MapColor.BLACK);
		registerWallPost("polished_blackstone_brick", BlockSoundGroup.STONE, 1.5f, 6.0f, MapColor.BLACK);
		registerWallPost("cobbled_deepslate", BlockSoundGroup.DEEPSLATE, 3.5f, 6.0f, MapColor.DEEPSLATE_GRAY);
		registerWallPost("polished_deepslate", BlockSoundGroup.POLISHED_DEEPSLATE, 3.5f, 6.0f, MapColor.DEEPSLATE_GRAY);
		registerWallPost("deepslate_brick", BlockSoundGroup.DEEPSLATE_BRICKS, 3.5f, 6.0f, MapColor.DEEPSLATE_GRAY);
		registerWallPost("deepslate_tile", BlockSoundGroup.DEEPSLATE_TILES, 3.5f, 6.0f, MapColor.DEEPSLATE_GRAY);
		registerWallPost("tuff", BlockSoundGroup.TUFF, 1.5f, 6.0f, MapColor.TERRACOTTA_GRAY);
		registerWallPost("polished_tuff", BlockSoundGroup.POLISHED_TUFF, 1.5f, 6.0f, MapColor.TERRACOTTA_GRAY);
		registerWallPost("tuff_brick", BlockSoundGroup.TUFF_BRICKS, 1.5f, 6.0f, MapColor.TERRACOTTA_GRAY);

		// Item group
		ItemGroup postGroup = FabricItemGroup.builder()
			.displayName(Text.literal("Fence & Wall Posts"))
			.icon(() -> FENCE_POSTS.isEmpty() ? ItemStack.EMPTY : new ItemStack(FENCE_POSTS.get(0)))
			.entries((context, entries) -> {
				for (int i = 0; i < FENCE_POSTS.size(); i++) {
					entries.add(new ItemStack(FENCE_POSTS.get(i)));
					if (i < FENCE_POST_SLABS.size()) {
						entries.add(new ItemStack(FENCE_POST_SLABS.get(i)));
					}
				}
				for (int i = 0; i < WALL_POSTS.size(); i++) {
					entries.add(new ItemStack(WALL_POSTS.get(i)));
					if (i < WALL_POST_SLABS.size()) {
						entries.add(new ItemStack(WALL_POST_SLABS.get(i)));
					}
				}
			})
			.build();

		Registry.register(Registries.ITEM_GROUP, Identifier.of(MOD_ID, "posts"), postGroup);

		int total = FENCE_POSTS.size() + FENCE_POST_SLABS.size() + WALL_POSTS.size() + WALL_POST_SLABS.size();
		LOGGER.info("Registered {} blocks ({} fence posts, {} fence post slabs, {} wall posts, {} wall post slabs)",
			total, FENCE_POSTS.size(), FENCE_POST_SLABS.size(), WALL_POSTS.size(), WALL_POST_SLABS.size());
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
	public static PostBlock registerFencePost(String baseName, BlockSoundGroup soundGroup, boolean burnable, boolean requiresTool, float hardness, float resistance, MapColor mapColor) {
		String postName = baseName + "_fence_post";
		String slabName = baseName + "_fence_post_slab";

		if (registeredPosts.contains(postName)) {
			return null;
		}
		registeredPosts.add(postName);

		// Full post
		RegistryKey<Block> postBlockKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(MOD_ID, postName));
		AbstractBlock.Settings postSettings = AbstractBlock.Settings.create()
			.registryKey(postBlockKey)
			.strength(hardness, resistance)
			.sounds(soundGroup)
			.mapColor(mapColor)
			.nonOpaque();
		if (burnable) postSettings = postSettings.burnable();
		if (requiresTool) postSettings = postSettings.requiresTool();

		PostBlock post = new PostBlock(postSettings, FENCE_POST_INSET);
		Registry.register(Registries.BLOCK, postBlockKey.getValue(), post);

		RegistryKey<Item> postItemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, postName));
		Registry.register(Registries.ITEM, postItemKey.getValue(),
			new BlockItem(post, new Item.Settings().registryKey(postItemKey)));
		FENCE_POSTS.add(post);

		// Slab
		RegistryKey<Block> slabBlockKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(MOD_ID, slabName));
		AbstractBlock.Settings slabSettings = AbstractBlock.Settings.create()
			.registryKey(slabBlockKey)
			.strength(hardness, resistance)
			.sounds(soundGroup)
			.mapColor(mapColor)
			.nonOpaque();
		if (burnable) slabSettings = slabSettings.burnable();
		if (requiresTool) slabSettings = slabSettings.requiresTool();

		PostSlabBlock slab = new PostSlabBlock(slabSettings, FENCE_POST_INSET);
		Registry.register(Registries.BLOCK, slabBlockKey.getValue(), slab);

		RegistryKey<Item> slabItemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, slabName));
		Registry.register(Registries.ITEM, slabItemKey.getValue(),
			new BlockItem(slab, new Item.Settings().registryKey(slabItemKey)));
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
	public static PostBlock registerWallPost(String baseName, BlockSoundGroup soundGroup, float hardness, float resistance, MapColor mapColor) {
		String postName = baseName + "_wall_post";
		String slabName = baseName + "_wall_post_slab";

		if (registeredPosts.contains(postName)) {
			return null;
		}
		registeredPosts.add(postName);

		// Full post
		RegistryKey<Block> postBlockKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(MOD_ID, postName));
		AbstractBlock.Settings postSettings = AbstractBlock.Settings.create()
			.registryKey(postBlockKey)
			.strength(hardness, resistance)
			.sounds(soundGroup)
			.mapColor(mapColor)
			.nonOpaque()
			.requiresTool();

		PostBlock post = new PostBlock(postSettings, WALL_POST_INSET);
		Registry.register(Registries.BLOCK, postBlockKey.getValue(), post);

		RegistryKey<Item> postItemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, postName));
		Registry.register(Registries.ITEM, postItemKey.getValue(),
			new BlockItem(post, new Item.Settings().registryKey(postItemKey)));
		WALL_POSTS.add(post);

		// Slab
		RegistryKey<Block> slabBlockKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(MOD_ID, slabName));
		AbstractBlock.Settings slabSettings = AbstractBlock.Settings.create()
			.registryKey(slabBlockKey)
			.strength(hardness, resistance)
			.sounds(soundGroup)
			.mapColor(mapColor)
			.nonOpaque()
			.requiresTool();

		PostSlabBlock slab = new PostSlabBlock(slabSettings, WALL_POST_INSET);
		Registry.register(Registries.BLOCK, slabBlockKey.getValue(), slab);

		RegistryKey<Item> slabItemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, slabName));
		Registry.register(Registries.ITEM, slabItemKey.getValue(),
			new BlockItem(slab, new Item.Settings().registryKey(slabItemKey)));
		WALL_POST_SLABS.add(slab);

		return post;
	}
}
