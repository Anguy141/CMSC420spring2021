package cmsc420_s21;

// YOU SHOULD NOT MODIFY THIS FILE

/**
 * A 2-dimensional point with a string label.
 */
public interface LabeledPoint2D {
	public float getX(); // get point's x-coordinate

	public float getY(); // get point's y-coordinate

	public float getCoord(int i); // get point's i-th coordinate (0=x, 1=y)

	public Point2D getPoint2D(); // get the point itself

	public String getLabel(); // get the label
}
