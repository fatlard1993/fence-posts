package justfatlard.fence_posts;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Half-height (8px) post slab with stackable behavior.
 * Shape width is determined by the inset parameter:
 * - Fence post slabs use inset 6.0 (4x4 centered)
 * - Wall post slabs use inset 4.0 (8x8 centered)
 */
public class PostSlabBlock extends Block implements SimpleWaterloggedBlock {
	public static final EnumProperty<SlabType> TYPE = BlockStateProperties.SLAB_TYPE;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	protected final VoxelShape bottomShape;
	protected final VoxelShape topShape;
	protected final VoxelShape doubleShape;

	public PostSlabBlock(BlockBehaviour.Properties settings, double inset) {
		super(settings);
		this.bottomShape = Block.box(inset, 0.0, inset, 16.0 - inset, 8.0, 16.0 - inset);
		this.topShape = Block.box(inset, 8.0, inset, 16.0 - inset, 16.0, 16.0 - inset);
		this.doubleShape = Block.box(inset, 0.0, inset, 16.0 - inset, 16.0, 16.0 - inset);
		this.registerDefaultState(this.defaultBlockState().setValue(TYPE, SlabType.BOTTOM).setValue(WATERLOGGED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(TYPE, WATERLOGGED);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(TYPE)) {
			case DOUBLE -> doubleShape;
			case TOP -> topShape;
			default -> bottomShape;
		};
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockPos blockPos = ctx.getClickedPos();
		BlockState blockState = ctx.getLevel().getBlockState(blockPos);

		if (blockState.is(this)) {
			return blockState.setValue(TYPE, SlabType.DOUBLE).setValue(WATERLOGGED, false);
		}

		FluidState fluidState = ctx.getLevel().getFluidState(blockPos);
		BlockState newState = this.defaultBlockState().setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);

		Direction side = ctx.getClickedFace();
		if (side == Direction.DOWN) {
			return newState.setValue(TYPE, SlabType.TOP);
		} else if (side == Direction.UP) {
			return newState.setValue(TYPE, SlabType.BOTTOM);
		} else {
			double hitY = ctx.getClickLocation().y - (double)blockPos.getY();
			return newState.setValue(TYPE, hitY > 0.5D ? SlabType.TOP : SlabType.BOTTOM);
		}
	}

	@Override
	public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		ItemStack itemStack = context.getItemInHand();
		SlabType slabType = state.getValue(TYPE);

		if (slabType == SlabType.DOUBLE || !itemStack.is(this.asItem())) {
			return false;
		}

		if (context.replacingClickedOnBlock()) {
			boolean clickedTop = context.getClickLocation().y - (double)context.getClickedPos().getY() > 0.5D;
			Direction side = context.getClickedFace();

			if (slabType == SlabType.BOTTOM) {
				return side == Direction.UP || (clickedTop && side.getAxis().isHorizontal());
			} else {
				return side == Direction.DOWN || (!clickedTop && side.getAxis().isHorizontal());
			}
		}

		return true;
	}

	@Override
	public FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	public BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		if (state.getValue(WATERLOGGED)) {
			tickView.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
		}
		return super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
	}
}
