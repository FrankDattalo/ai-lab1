package mbra;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Primary class of model based reflex agent.
 *
 * @author Frank Dattalo 2 / 2 / 17
 */
public class ModelBasedReflexAgent {

	private MovingRoverSensors sensors;
	private State state;
	private Action action;
	private Model model;
	private List<Rule> rules;

	/**
	 * Public constructor for MBRA.
	 *
	 * @param sensors
	 *            - The agent sensors
	 */
	public ModelBasedReflexAgent(MovingRoverSensors sensors) {
		this.sensors = sensors;

		// allocate boolean arrays with padding on each side to ignore zero
		// indexing, easier for position calculation and edge cases
		this.state = new State(1, 1, new boolean[7][4], new boolean[7][4], new boolean[7][4], new HashSet<>(),
				Direction.SOUTH, null);

		// set previous action to noop
		this.action = Action.NOOP;

		// instantiate model
		this.model = new Model();

		// due to the implementation of the rule matching algorithm,
		// we can gaunteee that the rules are traversed in order
		// this means that we can reason about the state of the world with
		// respect to the ordering of the
		// rules
		this.rules = Arrays
				.asList(

						// if we are in a spot where the sample has not been
						// picked up then pick it up

						new Rule(Action.GRAB,
								state -> state.percept != null
										&& !state.grabbed.contains(state.percept.getSampleValue())),

						// if we are north and not facing south and we have not
						// sensed south look south

						// if we are north and facing south and we have not
						// sensed south sense outh

						new Rule(Action.LOOK_SOUTH,
								state -> state.isNorth && !state.sensed[state.x][state.y - 1]
										&& (state.x >= 1 && state.x <= 5 && state.y >= 1 && state.y <= 2)),

						// if we are north and facing south and we have not been
						// to south and south is open then move south

						new Rule(Action.GO_SOUTH,
								state -> state.isNorth && !state.visited[state.x][state.y - 1]
										&& state.clear[state.x][state.y - 1]
										&& (state.x >= 1 && state.x <= 5 && state.y >= 1 && state.y <= 2)),

						// if we are south and not facing north and we have not
						// sensed north look north

						// if we are south and facing north and we have not
						// sensed north sense north

						new Rule(Action.LOOK_NORTH,
								state -> !state.isNorth && !state.sensed[state.x][state.y + 1]
										&& state.facing != Direction.NORTH
										&& (state.x >= 1 && state.x <= 5 && state.y >= 1 && state.y <= 2)),

						// if we are south and facing north and we have not been
						// to north and north is open the move north

						new Rule(Action.GO_NORTH,
								state -> !state.isNorth && !state.visited[state.x][state.y + 1]
										&& state.sensed[state.x][state.y + 1] && state.clear[state.x][state.y + 1]
										&& (state.x >= 1 && state.x <= 5 && state.y >= 1 && state.y <= 2)),

						// if we are north and sensed south or south and sensed
						// north and we are at the eastern edge of the board
						// then noop

						new Rule(Action.NOOP,
								state -> state.isNorth && state.sensed[state.x][state.y - 1] && state.x == 5),

						new Rule(Action.NOOP,
								state -> !state.isNorth && state.sensed[state.x][state.y + 1] && state.x == 5),

						// Noop if the position to our east has been sensed and
						// is not clear and we have sensed the position above us

						new Rule(Action.NOOP,
								state -> state.sensed[state.x + 1][state.y] && !state.clear[state.x + 1][state.y]
										&& !state.isNorth && state.sensed[state.x][state.y + 1]
										&& !state.clear[state.x][state.y + 1]),

						new Rule(Action.NOOP,
								state -> state.sensed[state.x + 1][state.y] && !state.clear[state.x + 1][state.y]
										&& !state.isNorth && state.sensed[state.x + 1][state.y + 1]
										&& !state.clear[state.x + 1][state.y + 1]),

						// Noop if the position to our east has been sensed and
						// is not clear and we have sensed the position below us

						new Rule(Action.NOOP,
								state -> state.sensed[state.x + 1][state.y] && !state.clear[state.x + 1][state.y]
										&& state.isNorth && state.sensed[state.x][state.y - 1]
										&& !state.clear[state.x][state.y - 1]),

						new Rule(Action.NOOP,
								state -> state.sensed[state.x + 1][state.y] && !state.clear[state.x + 1][state.y]
										&& state.isNorth && state.sensed[state.x + 1][state.y - 1]
										&& !state.clear[state.x + 1][state.y - 1]),

						// if we are north and sensed south or south and sensed
						// north and we have not sensed east and we are not
						// facing east then face east

						// if we are facing east and we have not sensed east
						// sense east

						new Rule(Action.LOOK_EAST,
								state -> state.isNorth && state.sensed[state.x][state.y - 1]
										&& !state.sensed[state.x + 1][state.y]
										&& (state.x >= 1 && state.x <= 5 && state.y >= 1 && state.y <= 2)),

						new Rule(Action.LOOK_EAST,
								state -> !state.isNorth && state.sensed[state.x][state.y + 1]
										&& !state.sensed[state.x + 1][state.y]
										&& (state.x >= 1 && state.x <= 5 && state.y >= 1 && state.y <= 2)),

						// if we are facing east and east is empty then go east

						new Rule(Action.GO_EAST,
								state -> state.sensed[state.x + 1][state.y] && state.clear[state.x + 1][state.y]
										&& (state.x >= 1 && state.x <= 5 && state.y >= 1 && state.y <= 2)),

						// if we are facing east and east is not empty and we
						// are north then go south

						new Rule(Action.GO_SOUTH,
								state -> state.facing == Direction.EAST && !state.clear[state.x + 1][state.y]
										&& state.isNorth
										&& (state.x >= 1 && state.x <= 5 && state.y >= 1 && state.y <= 2)),

						// if we are facing east and east is not empty and we
						// are south then go north

						new Rule(Action.GO_NORTH,
								state -> state.facing == Direction.EAST && !state.clear[state.x + 1][state.y]
										&& !state.isNorth
										&& (state.x >= 1 && state.x <= 5 && state.y >= 1 && state.y <= 2)),

						// error because we have exhausted all of our states

						new Rule(Action.NOOP, state -> {
							throw new RuntimeException("No Matches");
						}));

	}

	/**
	 * Returns the next action of the agent.
	 *
	 * @return the next action of the agent.
	 */
	public Action nextAction() {

		this.state = updateState(this.state, this.action, this.sensors, this.model);
		Rule r = ruleMatch(this.state, this.rules);
		this.action = r.action;

		return this.action;
	}

	/**
	 * Returns the current state of the agent.
	 *
	 * @return the current state of the agent.
	 */
	public State getState() {
		return this.state;
	}

	/**
	 * Returns a new state based off of the application of the current state,
	 * previous action, and sensors
	 *
	 * @param state
	 *            - the current state of the agent
	 * @param action
	 *            - the previous action of the agent
	 * @param sensors
	 *            - the sensors of the agent
	 * @param model
	 *            - the model to apply the state transition
	 *
	 * @return A new state
	 */
	private static State updateState(State state, Action action, MovingRoverSensors sensors, Model model) {
		return model.apply(action, sensors, state);
	}

	/**
	 * Matches a rule from a list of rules given a state.
	 *
	 * @param state
	 *            the current state of the agent
	 * @param rules
	 *            a list of rules
	 *
	 * @return The rule which contains a predicate and action to take.
	 */
	private static Rule ruleMatch(State state, List<Rule> rules) {
		for (Rule rule : rules) {
			if (rule.predicate.test(state)) {
				return rule;
			}
		}

		throw new RuntimeException("No Rules Matches state: " + state);
	}

}