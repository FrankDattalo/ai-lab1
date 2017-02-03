package mbra;

/**
 * Function that operates on a state, previous action, and sensors to return a
 * new state
 *
 * @author Frank Dattalo 2 / 2 / 17
 *
 */
public class Model {
	public State apply(Action action, MovingRoverSensors sensors, State state) {

		// create copy of state as to not modify old version
		State newState = state.copy();

		// update new state depending on action
		switch (action) {
		case GO_WEST:
			newState.x--;
			newState.visited[newState.x][newState.y] = true;
			break;
		case GO_EAST:
			newState.x++;
			newState.visited[newState.x][newState.y] = true;
			break;
		case GO_NORTH:
			newState.y++;
			newState.visited[newState.x][newState.y] = true;
			newState.isNorth = true;
			break;
		case GO_SOUTH:
			newState.y--;
			newState.visited[newState.x][newState.y] = true;
			newState.isNorth = false;
			break;
		case GRAB:
			newState.grabbed.add(state.percept.getSampleValue());
			break;
		case LOOK_NORTH:
			newState.facing = Direction.NORTH;
			break;
		case LOOK_SOUTH:
			newState.facing = Direction.SOUTH;
			break;
		case LOOK_WEST:
			newState.facing = Direction.WEST;
			break;
		case LOOK_EAST:
			newState.facing = Direction.EAST;
			break;
		case NOOP:
			break;
		default:
			throw new RuntimeException("Invalid action");
		}

		// read in new percept
		newState.percept = new Percept(newState.x, newState.y, newState.facing, sensors);

		// update state variables based on current state
		newState.visited[newState.x][newState.y] = true;
		newState.sensed[newState.x][newState.y] = true;
		newState.clear[newState.x][newState.y] = true;

		// update state variables based on current percept
		newState.sensed[newState.percept.getVisionX()][newState.percept.getVisionY()] = true;
		newState.clear[newState.percept.getVisionX()][newState.percept.getVisionY()] = newState.percept
				.isVisionIsClear();

		return newState;

	}
}
