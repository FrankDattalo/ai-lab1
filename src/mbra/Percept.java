package mbra;

import percept.SamplePercept;
import percept.VisionPercept;

/**
 * Percept class for MBRA that encapsulates both vision and sample percepts.
 *
 * @author Frank Dattalo 2 / 2 / 17
 */
public class Percept {
	private final VisionPercept visionPercept;
	private final SamplePercept samplePercept;

	private int visionX;
	private int visionY;
	private int x;
	private int y;

	public int getVisionX() {
		return this.visionX;
	}

	public int getVisionY() {
		return this.visionY;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public boolean isVisionIsClear() {
		return this.visionPercept != null && this.visionPercept.isClear();
	}

	public int getSampleValue() {
		return this.samplePercept != null ? this.samplePercept.value() : 0;
	}

	/**
	 * Constructor for percept, creates a new percept given a position,
	 * direction, and a sensor object.
	 *
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y coordinate
	 * @param direction
	 *            the direction to look for the vision percept
	 * @param sensors
	 *            the sensor object
	 */
	public Percept(int x, int y, Direction direction, MovingRoverSensors sensors) {

		this.x = x;
		this.y = y;

		this.visionY = this.y;
		this.visionX = this.x;

		switch (direction) {
		case NORTH:
			this.visionY++;
			break;
		case SOUTH:
			this.visionY--;
			break;
		case WEST:
			this.visionX--;
			break;
		case EAST:
			this.visionX++;
			break;
		default:
			throw new RuntimeException("Invalid facing direction");
		}

		this.visionPercept = sensors.getVisionPercept(this.visionX, this.visionY);
		this.samplePercept = sensors.getSamplePercept(this.x, this.y);
	}
}