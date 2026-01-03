package justfatlard.fence_posts;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.WallBlock;
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

import java.util.*;

public class Main implements ModInitializer {
	public static final String MOD_ID = "fence-posts";

	// Track registered blocks for item group
	private static final List<Block> FENCE_POSTS = new ArrayList<>();
	private static final List<Block> FENCE_POST_SLABS = new ArrayList<>();
	private static final List<Block> WALL_POSTS = new ArrayList<>();
	private static final List<Block> WALL_POST_SLABS = new ArrayList<>();

	// Track registered post names to avoid duplicates
	private static final Set<String> registeredPosts = new HashSet<>();

	// Public API for other mods to access registered posts
	public static List<Block> getFencePosts() { return Collections.unmodifiableList(FENCE_POSTS); }
	public static List<Block> getFencePostSlabs() { return Collections.unmodifiableList(FENCE_POST_SLABS); }
	public static List<Block> getWallPosts() { return Collections.unmodifiableList(WALL_POSTS); }
	public static List<Block> getWallPostSlabs() { return Collections.unmodifiableList(WALL_POST_SLABS); }

	@Override
	public void onInitialize() {
		// Register fence posts for all vanilla fence types
		registerFencePost("oak", BlockSoundGroup.WOOD, true);
		registerFencePost("spruce", BlockSoundGroup.WOOD, true);
		registerFencePost("birch", BlockSoundGroup.WOOD, true);
		registerFencePost("jungle", BlockSoundGroup.WOOD, true);
		registerFencePost("acacia", BlockSoundGroup.WOOD, true);
		registerFencePost("dark_oak", BlockSoundGroup.WOOD, true);
		registerFencePost("mangrove", BlockSoundGroup.WOOD, true);
		registerFencePost("cherry", BlockSoundGroup.CHERRY_WOOD, true);
		registerFencePost("bamboo", BlockSoundGroup.BAMBOO_WOOD, true);
		registerFencePost("crimson", BlockSoundGroup.NETHER_WOOD, false);
		registerFencePost("warped", BlockSoundGroup.NETHER_WOOD, false);
		registerFencePost("pale_oak", BlockSoundGroup.WOOD, true);
		registerFencePost("nether_brick", BlockSoundGroup.NETHER_BRICKS, false);

		// Register wall posts for all vanilla wall types
		registerWallPost("cobblestone", BlockSoundGroup.STONE);
		registerWallPost("mossy_cobblestone", BlockSoundGroup.STONE);
		registerWallPost("stone_brick", BlockSoundGroup.STONE);
		registerWallPost("mossy_stone_brick", BlockSoundGroup.STONE);
		registerWallPost("brick", BlockSoundGroup.STONE);
		registerWallPost("mud_brick", BlockSoundGroup.MUD_BRICKS);
		registerWallPost("sandstone", BlockSoundGroup.STONE);
		registerWallPost("red_sandstone", BlockSoundGroup.STONE);
		registerWallPost("granite", BlockSoundGroup.STONE);
		registerWallPost("diorite", BlockSoundGroup.STONE);
		registerWallPost("andesite", BlockSoundGroup.STONE);
		registerWallPost("prismarine", BlockSoundGroup.STONE);
		registerWallPost("nether_brick", BlockSoundGroup.NETHER_BRICKS);
		registerWallPost("red_nether_brick", BlockSoundGroup.NETHER_BRICKS);
		registerWallPost("end_stone_brick", BlockSoundGroup.STONE);
		registerWallPost("blackstone", BlockSoundGroup.STONE);
		registerWallPost("polished_blackstone", BlockSoundGroup.STONE);
		registerWallPost("polished_blackstone_brick", BlockSoundGroup.STONE);
		registerWallPost("cobbled_deepslate", BlockSoundGroup.DEEPSLATE);
		registerWallPost("polished_deepslate", BlockSoundGroup.POLISHED_DEEPSLATE);
		registerWallPost("deepslate_brick", BlockSoundGroup.DEEPSLATE_BRICKS);
		registerWallPost("deepslate_tile", BlockSoundGroup.DEEPSLATE_TILES);
		registerWallPost("tuff", BlockSoundGroup.TUFF);
		registerWallPost("polished_tuff", BlockSoundGroup.POLISHED_TUFF);
		registerWallPost("tuff_brick", BlockSoundGroup.TUFF_BRICKS);

		// Register item group
		ItemGroup postGroup = FabricItemGroup.builder()
			.displayName(Text.literal("Fence & Wall Posts"))
			.icon(() -> FENCE_POSTS.isEmpty() ? ItemStack.EMPTY : new ItemStack(FENCE_POSTS.get(0)))
			.entries((context, entries) -> {
				// Add fence posts and slabs interleaved
				for (int i = 0; i < FENCE_POSTS.size(); i++) {
					entries.add(new ItemStack(FENCE_POSTS.get(i)));
					if (i < FENCE_POST_SLABS.size()) {
						entries.add(new ItemStack(FENCE_POST_SLABS.get(i)));
					}
				}
				// Add wall posts and slabs interleaved
				for (int i = 0; i < WALL_POSTS.size(); i++) {
					entries.add(new ItemStack(WALL_POSTS.get(i)));
					if (i < WALL_POST_SLABS.size()) {
						entries.add(new ItemStack(WALL_POST_SLABS.get(i)));
					}
				}
			})
			.build();

		Registry.register(Registries.ITEM_GROUP, Identifier.of(MOD_ID, "posts"), postGroup);

		// Scan for modded fences and walls
		scanForModdedBlocks();

		int total = FENCE_POSTS.size() + FENCE_POST_SLABS.size() + WALL_POSTS.size() + WALL_POST_SLABS.size();
		System.out.println("[" + MOD_ID + "] Registered " + total + " blocks (" +
			FENCE_POSTS.size() + " fence posts, " + FENCE_POST_SLABS.size() + " fence post slabs, " +
			WALL_POSTS.size() + " wall posts, " + WALL_POST_SLABS.size() + " wall post slabs)");
	}

	/**
	 * Scan the registry for modded fences and walls.
	 * Note: This finds blocks registered before this mod initializes.
	 * For full modded support, mods should use the API methods below.
	 */
	private void scanForModdedBlocks() {
		int moddedFences = 0;
		int moddedWalls = 0;

		for (Block block : Registries.BLOCK) {
			Identifier id = Registries.BLOCK.getId(block);

			// Skip vanilla blocks (already registered)
			if ("minecraft".equals(id.getNamespace())) continue;

			if (block instanceof FenceBlock) {
				String baseName = id.getPath().replace("_fence", "");
				String postName = baseName + "_fence_post";

				// Skip if already registered
				if (registeredPosts.contains(postName)) continue;

				// Log discovered modded fence (resources would need to be generated)
				System.out.println("[" + MOD_ID + "] Discovered modded fence: " + id +
					" (post support requires resource pack)");
				moddedFences++;
			} else if (block instanceof WallBlock) {
				String baseName = id.getPath().replace("_wall", "");
				String postName = baseName + "_wall_post";

				// Skip if already registered
				if (registeredPosts.contains(postName)) continue;

				// Log discovered modded wall (resources would need to be generated)
				System.out.println("[" + MOD_ID + "] Discovered modded wall: " + id +
					" (post support requires resource pack)");
				moddedWalls++;
			}
		}

		if (moddedFences > 0 || moddedWalls > 0) {
			System.out.println("[" + MOD_ID + "] Found " + moddedFences + " modded fences and " +
				moddedWalls + " modded walls. Use RuntimeResourceGenerator to create support.");
		}
	}

	/**
	 * Public API: Register a fence post for a custom fence.
	 * Other mods can call this to add fence post support for their fences.
	 *
	 * @param baseName The base name (e.g., "oak" for "oak_fence_post")
	 * @param soundGroup The sound group to use
	 * @param burnable Whether the post can burn
	 * @return The registered FencePost block, or null if already registered
	 */
	public static FencePost registerFencePost(String baseName, BlockSoundGroup soundGroup, boolean burnable) {
		String postName = baseName + "_fence_post";
		String slabName = baseName + "_fence_post_slab";

		// Skip if already registered
		if (registeredPosts.contains(postName)) {
			return null;
		}
		registeredPosts.add(postName);

		// Full post
		RegistryKey<Block> postBlockKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(MOD_ID, postName));
		AbstractBlock.Settings postSettings = AbstractBlock.Settings.create()
			.registryKey(postBlockKey)
			.strength(2.0f, 3.0f)
			.sounds(soundGroup);
		if (burnable) postSettings = postSettings.burnable();

		FencePost post = new FencePost(postSettings);
		Registry.register(Registries.BLOCK, postBlockKey.getValue(), post);

		RegistryKey<Item> postItemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, postName));
		Registry.register(Registries.ITEM, postItemKey.getValue(),
			new BlockItem(post, new Item.Settings().registryKey(postItemKey)));
		FENCE_POSTS.add(post);

		// Slab
		RegistryKey<Block> slabBlockKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(MOD_ID, slabName));
		AbstractBlock.Settings slabSettings = AbstractBlock.Settings.create()
			.registryKey(slabBlockKey)
			.strength(2.0f, 3.0f)
			.sounds(soundGroup);
		if (burnable) slabSettings = slabSettings.burnable();

		FencePostSlab slab = new FencePostSlab(slabSettings);
		Registry.register(Registries.BLOCK, slabBlockKey.getValue(), slab);

		RegistryKey<Item> slabItemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, slabName));
		Registry.register(Registries.ITEM, slabItemKey.getValue(),
			new BlockItem(slab, new Item.Settings().registryKey(slabItemKey)));
		FENCE_POST_SLABS.add(slab);

		return post;
	}

	/**
	 * Public API: Register a wall post for a custom wall.
	 * Other mods can call this to add wall post support for their walls.
	 *
	 * @param baseName The base name (e.g., "cobblestone" for "cobblestone_wall_post")
	 * @param soundGroup The sound group to use
	 * @return The registered WallPost block, or null if already registered
	 */
	public static WallPost registerWallPost(String baseName, BlockSoundGroup soundGroup) {
		String postName = baseName + "_wall_post";
		String slabName = baseName + "_wall_post_slab";

		// Skip if already registered
		if (registeredPosts.contains(postName)) {
			return null;
		}
		registeredPosts.add(postName);

		// Full post
		RegistryKey<Block> postBlockKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(MOD_ID, postName));
		AbstractBlock.Settings postSettings = AbstractBlock.Settings.create()
			.registryKey(postBlockKey)
			.strength(2.0f, 3.0f)
			.sounds(soundGroup);

		WallPost post = new WallPost(postSettings);
		Registry.register(Registries.BLOCK, postBlockKey.getValue(), post);

		RegistryKey<Item> postItemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, postName));
		Registry.register(Registries.ITEM, postItemKey.getValue(),
			new BlockItem(post, new Item.Settings().registryKey(postItemKey)));
		WALL_POSTS.add(post);

		// Slab
		RegistryKey<Block> slabBlockKey = RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(MOD_ID, slabName));
		AbstractBlock.Settings slabSettings = AbstractBlock.Settings.create()
			.registryKey(slabBlockKey)
			.strength(2.0f, 3.0f)
			.sounds(soundGroup);

		WallPostSlab slab = new WallPostSlab(slabSettings);
		Registry.register(Registries.BLOCK, slabBlockKey.getValue(), slab);

		RegistryKey<Item> slabItemKey = RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, slabName));
		Registry.register(Registries.ITEM, slabItemKey.getValue(),
			new BlockItem(slab, new Item.Settings().registryKey(slabItemKey)));
		WALL_POST_SLABS.add(slab);

		return post;
	}
}
