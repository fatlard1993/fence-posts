package justfatlard.fence_posts;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.enums.SlabType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

/**
 * Half-height (8px) fence post slab with stackable behavior.
 * 4x4 centered shape matching fence post dimensions.
 */
public class FencePostSlab extends Block implements Waterloggable {
	public static final EnumProperty<SlabType> TYPE;
	public static final BooleanProperty WATERLOGGED;

	protected static final VoxelShape BOTTOM_SHAPE;
	protected static final VoxelShape TOP_SHAPE;
	protected static final VoxelShape DOUBLE_SHAPE;

	public FencePostSlab(AbstractBlock.Settings settings) {
		super(settings);
		this.setDefaultState(this.getDefaultState().with(TYPE, SlabType.BOTTOM).with(WATERLOGGED, false));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(TYPE, WATERLOGGED);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		SlabType type = state.get(TYPE);
		return switch (type) {
			case DOUBLE -> DOUBLE_SHAPE;
			case TOP -> TOP_SHAPE;
			default -> BOTTOM_SHAPE;
		};
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockPos blockPos = ctx.getBlockPos();
		BlockState blockState = ctx.getWorld().getBlockState(blockPos);

		if (blockState.isOf(this)) {
			return blockState.with(TYPE, SlabType.DOUBLE).with(WATERLOGGED, false);
		}

		FluidState fluidState = ctx.getWorld().getFluidState(blockPos);
		BlockState newState = this.getDefaultState().with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);

		Direction side = ctx.getSide();
		if (side == Direction.DOWN) {
			return newState.with(TYPE, SlabType.TOP);
		} else if (side == Direction.UP) {
			return newState.with(TYPE, SlabType.BOTTOM);
		} else {
			double hitY = ctx.getHitPos().y - (double)blockPos.getY();
			return newState.with(TYPE, hitY > 0.5D ? SlabType.TOP : SlabType.BOTTOM);
		}
	}

	@Override
	public boolean canReplace(BlockState state, ItemPlacementContext context) {
		ItemStack itemStack = context.getStack();
		SlabType slabType = state.get(TYPE);

		if (slabType == SlabType.DOUBLE || !itemStack.isOf(this.asItem())) {
			return false;
		}

		if (context.canReplaceExisting()) {
			boolean clickedTop = context.getHitPos().y - (double)context.getBlockPos().getY() > 0.5D;
			Direction side = context.getSide();

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
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		if (state.get(WATERLOGGED)) {
			tickView.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
	}

	static {
		TYPE = Properties.SLAB_TYPE;
		WATERLOGGED = Properties.WATERLOGGED;

		// 4x4 centered shapes
		BOTTOM_SHAPE = Block.createCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 8.0D, 10.0D);
		TOP_SHAPE = Block.createCuboidShape(6.0D, 8.0D, 6.0D, 10.0D, 16.0D, 10.0D);
		DOUBLE_SHAPE = Block.createCuboidShape(6.0D, 0.0D, 6.0D, 10.0D, 16.0D, 10.0D);
	}
}
