package justfatlard.fence_posts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.golem.CopperGolem;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * A lead tied from one fence to another, with no chicken or boat on the end of it.
 *
 * <p>Click a fence holding a lead, with nothing on a lead of your own, and a knot goes on it with
 * the lead running from there to your hand. Click a second fence and the game ties the far end the
 * way it ties any lead to a fence. The game only draws a lead from something that can be leashed to
 * whatever holds it, and a knot cannot be leashed, so the near end is held by a small copper golem
 * nobody can see, pinned under the first knot. What every client draws, a vanilla one included, is
 * the plain lead between two knots.
 *
 * <p>A copper golem because it is a creature the game lets wear a lead, its lead hangs from the
 * middle of it rather than its chest (so, pinned, the lead leaves the knot and the golem sits inside
 * it), it draws nothing at all when invisible, eyes included, it never despawns, counts toward no
 * spawning cap, and picks nothing up. Scaled to three quarters it sits almost wholly inside the knot
 * and is still large enough that clients draw its lead from as far off as they draw the knots.
 *
 * <p>The lead is never lost or doubled. Every way the game lets go of a lead, snapped by distance,
 * a knot broken, untied by hand, a holder gone, drops the one lead itself; when the golem is found
 * let go it is only taken away. When the first fence or its knot goes, or the lead is tied back to
 * the fence it started from, the golem lets go the game's own way, which drops the lead, and goes.
 */
public final class FenceLeads {
	private FenceLeads() {}

	/** Marks the golem as ours to anyone with a command line: {@code @e[tag=fence-posts.lead]}. */
	private static final String TAG = "fence-posts.lead";
	private static final double SCALE = 0.75;

	/** The fence the near end is tied to. Saved with the golem; knots are not saved, so it is how the knot comes back. */
	private static final AttachmentType<BlockPos> ANCHOR = AttachmentRegistry.createPersistent(
		Identifier.fromNamespaceAndPath(Main.MOD_ID, "lead_anchor"), BlockPos.CODEC);

	private static final Set<CopperGolem> HOLDERS = Collections.newSetFromMap(new IdentityHashMap<>());
	/** The knot each golem last stood under, kept to tell a knot broken from one the game tidied away. */
	private static final Map<CopperGolem, LeashFenceKnotEntity> KNOTS = new IdentityHashMap<>();
	private static final List<Gone> GONE = new ArrayList<>();

	/** A golem that is gone from its level, leaving its knot to tidy. */
	private record Gone(ServerLevel level, BlockPos anchor) {}

	public static void register() {
		UseBlockCallback.EVENT.register(FenceLeads::onUseBlock);
		UseEntityCallback.EVENT.register(FenceLeads::onUseEntity);

		// Invisibility is not saved for a mob, so it is put back before any client is sent the golem.
		ServerEntityEvents.ALLOW_LOAD.register((entity, level, reason, fromDisk) -> {
			if (entity instanceof CopperGolem golem && isHolder(golem)) hide(golem);
			return true;
		});
		ServerEntityEvents.ENTITY_LOAD.register((entity, level) -> {
			if (entity instanceof CopperGolem golem && isHolder(golem)) HOLDERS.add(golem);
		});
		ServerEntityEvents.ENTITY_UNLOAD.register(FenceLeads::onUnload);
		ServerTickEvents.END_LEVEL_TICK.register(FenceLeads::tick);
		ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
			HOLDERS.clear();
			KNOTS.clear();
			GONE.clear();
		});
	}

	private static InteractionResult onUseBlock(Player player, Level level, InteractionHand hand, BlockHitResult hit) {
		ItemStack held = player.getItemInHand(hand);
		if (!held.is(Items.LEAD) || player.isSpectator()) return InteractionResult.PASS;
		return tie(player, level, held, hit.getBlockPos());
	}

	/** The knot covers the top of its post, so a click on a knotted fence lands on the knot. */
	private static InteractionResult onUseEntity(Player player, Level level, InteractionHand hand, Entity entity, EntityHitResult hit) {
		if (!(entity instanceof LeashFenceKnotEntity knot)) return InteractionResult.PASS;
		ItemStack held = player.getItemInHand(hand);
		if (!held.is(Items.LEAD) || player.isSpectator()) return InteractionResult.PASS;
		// Clicked without sneaking, a knot hands the player whatever is tied to it: that is the game's to do.
		if (!player.isSecondaryUseActive() && !Leashable.leashableLeashedTo(knot).isEmpty()) return InteractionResult.PASS;
		return tie(player, level, held, knot.getPos());
	}

	private static InteractionResult tie(Player player, Level level, ItemStack held, BlockPos pos) {
		// Fences only: the game takes a knot off anything else.
		if (!level.getBlockState(pos).is(BlockTags.FENCES)) return InteractionResult.PASS;
		// A player with something on a lead is tying it up, the game's own way, and this is how the far end is tied.
		if (holdsAnything(player, level, pos)) return InteractionResult.PASS;
		if (!(level instanceof ServerLevel server)) return InteractionResult.SUCCESS;

		LeashFenceKnotEntity knot = LeashFenceKnotEntity.getOrCreateKnot(server, pos);
		CopperGolem holder = spawn(server, pos.immutable(), knot);
		if (holder == null) {
			untie(server, pos);
			return InteractionResult.PASS;
		}
		holder.setLeashedTo(player, true);
		held.consume(1, player);
		knot.playPlacementSound();
		server.gameEvent(GameEvent.BLOCK_ATTACH, pos, GameEvent.Context.of(player));
		return InteractionResult.SUCCESS;
	}

	/** Whatever the game would tie to this fence for this player, looked for where the game looks and where the knot does. */
	private static boolean holdsAnything(Player player, Level level, BlockPos pos) {
		Predicate<Leashable> theirs = leashable -> leashable.getLeashHolder() == player;
		return !Leashable.leashableInArea(level, Vec3.atCenterOf(pos), theirs).isEmpty()
			|| !Leashable.leashableInArea(player, theirs).isEmpty();
	}

	private static CopperGolem spawn(ServerLevel level, BlockPos anchor, LeashFenceKnotEntity knot) {
		CopperGolem holder = EntityTypes.COPPER_GOLEM.create(level, EntitySpawnReason.TRIGGERED);
		if (holder == null) return null;
		holder.addTag(TAG);
		holder.setAttached(ANCHOR, anchor);
		holder.setNoAi(true);
		holder.setSilent(true);
		holder.setPermanentlyInvulnerable(true);
		holder.setNoGravity(true);
		holder.setPersistenceRequired();
		AttributeInstance scale = holder.getAttribute(Attributes.SCALE);
		if (scale != null) scale.setBaseValue(SCALE);
		holder.refreshDimensions();
		hide(holder);
		holder.snapTo(pinFor(holder, knot));
		if (!level.addFreshEntity(holder)) return null;
		KNOTS.put(holder, knot);
		return holder;
	}

	private static boolean isHolder(Entity entity) {
		return entity.entityTags().contains(TAG);
	}

	private static BlockPos anchorOf(CopperGolem holder) {
		return holder.getAttachedOrElse(ANCHOR, holder.blockPosition());
	}

	/** Where the golem stands so its lead leaves from where a knot's lead leaves. */
	private static Vec3 pinFor(CopperGolem holder, LeashFenceKnotEntity knot) {
		return knot.getRopeHoldPosition(1.0F).subtract(holder.getLeashOffset());
	}

	private static void hide(CopperGolem holder) {
		if (!holder.isInvisible()) holder.setInvisible(true);
		if (holder.isOnFire()) holder.clearFire();
	}

	private static void tick(ServerLevel level) {
		if (!HOLDERS.isEmpty()) {
			for (CopperGolem holder : List.copyOf(HOLDERS)) {
				if (holder.level() == level) tend(level, holder);
			}
		}
		if (!GONE.isEmpty()) {
			GONE.removeIf(gone -> {
				if (gone.level() != level) return false;
				untie(level, gone.anchor());
				return true;
			});
		}
	}

	private static void tend(ServerLevel level, CopperGolem holder) {
		// Dying (to /kill, say): the game lets go of its lead, and drops it, on its own.
		if (holder.isRemoved() || !holder.isAlive()) return;
		BlockPos anchor = anchorOf(holder);

		// The first fence is gone, or its knot was broken off: the lead comes loose.
		LeashFenceKnotEntity known = KNOTS.get(holder);
		boolean knotBroken = known != null && known.getRemovalReason() == Entity.RemovalReason.KILLED;
		if (knotBroken || !level.getBlockState(anchor).is(BlockTags.FENCES)) {
			release(level, holder);
			return;
		}

		Leashable.LeashData leash = holder.getLeashData();
		// Let go already: snapped, untied, its holder gone. The game dropped the lead when it let go.
		if (leash == null) {
			holder.discard();
			return;
		}
		// Tied back to the fence it started from: a lead from a post to itself is a lead put away.
		if (leash.leashHolder instanceof LeashFenceKnotEntity knot && knot.getPos().equals(anchor)) {
			release(level, holder);
			return;
		}

		// The knot comes back after a restart (knots are not saved), and after the game tidies away
		// a knot with nothing of its own tied to it, which a knot holding only this end looks like.
		if (known == null || known.isRemoved()) {
			known = LeashFenceKnotEntity.getOrCreateKnot(level, anchor);
			KNOTS.put(holder, known);
		}

		Vec3 pin = pinFor(holder, known);
		if (holder.position().distanceToSqr(pin) > 1.0E-6) holder.snapTo(pin);
		holder.setDeltaMovement(Vec3.ZERO);
		hide(holder);
	}

	/** Let go the game's own way, which drops the one lead, and take the golem away. */
	private static void release(ServerLevel level, CopperGolem holder) {
		if (holder.isLeashed()) {
			holder.dropLeash();
		} else if (holder.getLeashData() != null) {
			// Still waiting, just loaded, to find a holder that is not here yet: nothing for the game to drop, so this does.
			holder.setLeashData(null);
			holder.spawnAtLocation(level, Items.LEAD);
		}
		holder.discard();
	}

	private static void onUnload(Entity entity, ServerLevel level) {
		if (!(entity instanceof CopperGolem holder) || !HOLDERS.remove(holder)) return;
		KNOTS.remove(holder);
		Entity.RemovalReason reason = holder.getRemovalReason();
		if (reason == Entity.RemovalReason.UNLOADED_TO_CHUNK || reason == Entity.RemovalReason.UNLOADED_WITH_PLAYER) return;
		// Every way out has let go of the lead already. Teleported to another dimension, the golem
		// arrives there carrying it and, with no fence under it, lets go of it there.
		GONE.add(new Gone(level, anchorOf(holder)));
	}

	/** Take the knot off a fence when nothing is tied to it and no golem stands under it. */
	private static void untie(ServerLevel level, BlockPos anchor) {
		LeashFenceKnotEntity.getKnot(level, anchor).ifPresent(knot -> {
			if (!Leashable.leashableLeashedTo(knot).isEmpty()) return;
			for (CopperGolem other : HOLDERS) {
				if (other.level() == level && !other.isRemoved() && anchorOf(other).equals(anchor)) return;
			}
			knot.discard();
		});
	}
}
