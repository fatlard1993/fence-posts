package justfatlard.fence_posts;

import justfatlard.pandorical.api.BlockMarkApi;
import justfatlard.pandorical.api.PandoricalApi;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

/**
 * A post is a fence or wall told to stand alone.
 *
 * <p>Sneak and click any fence or wall with an empty hand: it lets go of its neighbours and they
 * of it, and stands as a post of its own material, poplar included and whatever comes next. Click
 * it again and it joins back in. A post is a mark on the block, carried by Pandorical to every
 * client and remembered here across restarts, so there is nothing to craft and nothing to carry:
 * every fence is already the post it might need to be.
 *
 * <p>A post block from before this, the crafted kind, is turned into the fence or wall it stood
 * for with the same click, standing alone; the next click joins it in.
 */
public final class PostToggle {
	private PostToggle() {}

	public static void register() {
		UseBlockCallback.EVENT.register(PostToggle::onUse);
		PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
			if (level instanceof ServerLevel server) forget(server, pos);
		});
		// Marked again as the server comes up, for every level: the marks are not saved, this is.
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			for (ServerLevel level : server.getAllLevels()) {
				for (BlockPos pos : Posts.get(level).all()) PandoricalApi.blockMarks().mark(level, pos, BlockMarkApi.POST);
			}
		});
	}

	private static InteractionResult onUse(Player player, Level level, InteractionHand hand, BlockHitResult hit) {
		if (hand != InteractionHand.MAIN_HAND || !player.isSecondaryUseActive()) return InteractionResult.PASS;
		if (!player.getMainHandItem().isEmpty()) return InteractionResult.PASS;

		BlockPos pos = hit.getBlockPos();
		BlockState state = level.getBlockState(pos);
		Block block = state.getBlock();
		boolean legacy = block instanceof PostBlock;
		if (!legacy && !(block instanceof FenceBlock) && !(block instanceof WallBlock)) return InteractionResult.PASS;
		if (!(level instanceof ServerLevel server)) return InteractionResult.SUCCESS;

		if (legacy) {
			BlockState replacement = fenceFor(block, state);
			if (replacement == null) return InteractionResult.PASS;
			server.setBlock(pos, replacement, Block.UPDATE_CLIENTS);
			stand(server, pos, true);
		} else {
			stand(server, pos, !Posts.get(server).isPost(pos));
		}

		BlockState now = server.getBlockState(pos);
		server.playSound(null, pos, now.getSoundType().getPlaceSound(), SoundSource.BLOCKS, 0.8F,
			Posts.get(server).isPost(pos) ? 0.8F : 1.1F);
		if (player instanceof ServerPlayer told) {
			told.sendSystemMessage(Component.literal(Posts.get(server).isPost(pos) ? "Standing alone" : "Joined in"), true);
		}
		return InteractionResult.SUCCESS;
	}

	/** Mark or unmark, and reshape the block and its neighbours to match. */
	private static void stand(ServerLevel level, BlockPos pos, boolean post) {
		Posts.get(level).set(pos, post);
		if (post) PandoricalApi.blockMarks().mark(level, pos, BlockMarkApi.POST);
		else PandoricalApi.blockMarks().unmark(level, pos, BlockMarkApi.POST);
		// Its own sides from what stands beside it, with the mark in force; the neighbours are
		// told of the change and let go, or take hold, the ordinary way.
		BlockState fresh = Block.updateFromNeighbourShapes(level.getBlockState(pos), level, pos);
		level.setBlock(pos, fresh, Block.UPDATE_ALL);
	}

	private static void forget(ServerLevel level, BlockPos pos) {
		if (!Posts.get(level).isPost(pos)) return;
		Posts.get(level).set(pos, false);
		PandoricalApi.blockMarks().unmark(level, pos, BlockMarkApi.POST);
	}

	/** The vanilla fence or wall a crafted post stood for, waterlogging kept; null for a slab, which has no vanilla twin. */
	private static BlockState fenceFor(Block legacy, BlockState state) {
		if (legacy instanceof PostSlabBlock) return null;
		String path = BuiltInRegistries.BLOCK.getKey(legacy).getPath();
		String vanilla;
		if (path.endsWith("_fence_post")) vanilla = path.substring(0, path.length() - "_post".length());
		else if (path.endsWith("_wall_post")) vanilla = path.substring(0, path.length() - "_post".length());
		else return null;
		return BuiltInRegistries.BLOCK.getOptional(Identifier.withDefaultNamespace(vanilla))
			.map(block -> {
				BlockState fresh = block.defaultBlockState();
				if (fresh.hasProperty(BlockStateProperties.WATERLOGGED) && state.hasProperty(BlockStateProperties.WATERLOGGED)) {
					fresh = fresh.setValue(BlockStateProperties.WATERLOGGED, state.getValue(BlockStateProperties.WATERLOGGED));
				}
				return fresh;
			})
			.orElse(null);
	}
}
