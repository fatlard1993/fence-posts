package justfatlard.fence_posts;

import com.mojang.serialization.Codec;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

/**
 * Which fences and walls in a level stand alone.
 *
 * <p>The mark Pandorical carries is what the shape logic reads, on both sides, and it is not
 * saved; this is the record that outlives a restart, marked again as each level loads.
 */
public final class Posts extends SavedData {
	private static final String STORAGE_KEY = "fence_posts";

	public static final Codec<Posts> CODEC = Codec.LONG.listOf().xmap(Posts::fromList, Posts::toList);

	private static final SavedDataType<Posts> TYPE = new SavedDataType<>(
		Identifier.parse(STORAGE_KEY), Posts::new, CODEC, DataFixTypes.LEVEL);

	private final Set<Long> standing = new HashSet<>();

	public static Posts get(ServerLevel level) {
		return level.getDataStorage().computeIfAbsent(TYPE);
	}

	public boolean isPost(BlockPos pos) {
		return this.standing.contains(pos.asLong());
	}

	public void set(BlockPos pos, boolean post) {
		if (post ? this.standing.add(pos.asLong()) : this.standing.remove(pos.asLong())) this.setDirty();
	}

	public List<BlockPos> all() {
		return this.standing.stream().map(BlockPos::of).toList();
	}

	private static Posts fromList(List<Long> stored) {
		Posts posts = new Posts();
		posts.standing.addAll(stored);
		return posts;
	}

	private static List<Long> toList(Posts posts) {
		return List.copyOf(posts.standing);
	}
}
