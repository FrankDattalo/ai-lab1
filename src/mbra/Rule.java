package mbra;

import java.util.function.Predicate;

/**
 * Class for instantating rules for mbra.
 *
 * @author Frank Dattalo 2 / 2 / 17
 */
public class Rule {
	public final Predicate<State> predicate;
	public final Action action;

	public Rule(Action actionToTake, Predicate<State> pred) {
		this.predicate = pred;
		this.action = actionToTake;
	}
}
