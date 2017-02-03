package mbra;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Class that represents the state of the world according to our mbra.
 *
 * @author Frank Dattalo 2 / 2 / 17
 */
public class State {
	public boolean isNorth;
	public int x;
	public int y;
	public Percept percept;
	public final boolean[][] visited;
	public final boolean[][] sensed;
	public final boolean[][] clear;
	public final Set<Integer> grabbed;
	public Direction facing;

	public State(int x, int y, boolean[][] visited, boolean[][] sensed, boolean[][] clear, Set<Integer> grabbed,
			Direction facing, Percept percept) {
		this.x = x;
		this.y = y;
		this.visited = visited;
		this.sensed = sensed;
		this.clear = clear;
		this.grabbed = grabbed;
		this.facing = facing;
		this.percept = percept;

		this.isNorth = y == 2;

		this.clear[x][y] = true;
		this.visited[x][y] = true;
	}

	public State copy() {
		return new State(this.x, this.y, copy(this.visited), copy(this.sensed), copy(this.clear),
				this.grabbed.parallelStream().collect(Collectors.toSet()), this.facing, this.percept);
	}

	private static boolean[][] copy(boolean[][] arr) {
		boolean[][] copy = new boolean[arr.length][];
		for (int i = 0; i < copy.length; i++) {
			copy[i] = arr[i].clone();
		}
		return copy;
	}
}
