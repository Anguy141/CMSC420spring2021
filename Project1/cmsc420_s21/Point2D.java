package cmsc420_s21;

// YOU SHOULD NOT MODIFY THIS FILE

/**
 * A 2-dimensional point. We represent a point as a 2-element array of type
 * float.
 */

public class Point2D {
	final static int DIM = 2; // spatial dimension
	float[] coord; // coordinates

	/**
	 * Default constructor.
	 * 
	 */
	public Point2D() {
		coord = new float[DIM];
		for (int i = 0; i < DIM; i++)
			coord[i] = 0;
	}

	/**
	 * Construct from coordinates.
	 * 
	 * @param coord The array of coordinates.
	 */
	public Point2D(float x, float y) {
		coord = new float[DIM];
		coord[0] = x;
		coord[1] = y;
	}

	/**
	 * Construct from a 2-element coordinate array.
	 * 
	 * @param coord The array of coordinates.
	 */
	public Point2D(float[] coord) {
		assert (coord.length == DIM);
		this.coord = new float[DIM];
		for (int i = 0; i < DIM; i++)
			this.coord[i] = coord[i];
	}

	/**
	 * Copy constructor.
	 * 
	 * @param p The point to copy.
	 */
	public Point2D(Point2D p) {
		coord = new float[DIM];
		for (int i = 0; i < DIM; i++)
			coord[i] = p.getCoord(i);
	}

	/**
	 * Get the dimension.
	 * 
	 * @return The dimension
	 */
	public static int getDim() {
		return DIM;
	}

	/**
	 * Get the i-th coordinate (i=0 for x, i=1 for y).
	 * 
	 * @param i The coordinate index 0 or 1.
	 * @return The i-th coordinate of the point.
	 */
	public float getCoord(int i) {
		return coord[i];
	}

	/**
	 * Set the i-th coordinate (i=0 for x, i=1 for y).
	 * 
	 * @param i The coordinate index 0 or 1.
	 */
	public void setCoord(int i, float c) {
		coord[i] = c;
	}

	/**
	 * Get the x-coordinate.
	 * 
	 * @return The x-coordinate of the point.
	 */
	public float getX() {
		return coord[0];
	}

	/**
	 * Get the y-coordinate.
	 * 
	 * @return The y-coordinate of the point.
	 */
	public float getY() {
		return coord[1];
	}

	/**
	 * L1 (Manhattan) distance between points.
	 * 
	 * @param p Source
	 * @param q Destination
	 * @return The distance
	 */
	public static float distanceL1(Point2D p, Point2D q) {
		float sum = 0;
		for (int i = 0; i < DIM; i++)
			sum += Math.abs(p.coord[i] - q.coord[i]);
		return sum;
	}

	/**
	 * Euclidean distance between points.
	 * 
	 * @param p Source
	 * @param q Destination
	 * @return The distance
	 */
	public static float distance(Point2D p, Point2D q) {
		float sum = 0;
		for (int i = 0; i < DIM; i++)
			sum += Math.pow(p.coord[i] - q.coord[i], 2);
		return (float) Math.sqrt(sum);
	}

	/**
	 * String representation.
	 * 
	 * @return String representation of the point.
	 */
	public String toString() {
		return "(" + coord[0] + "," + coord[1] + ")";
	}

}
