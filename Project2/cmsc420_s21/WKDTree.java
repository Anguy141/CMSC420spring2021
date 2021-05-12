package cmsc420_s21; // Don't delete this or your file won't pass the autograder

import java.util.ArrayList;

/**
 * WKDTree (skeleton)
 *
 * MODIFY THE FOLLOWING CLASS.
 *
 * You are free to make whatever changes you like or to create additional
 * classes and files.
 */

public class WKDTree<LPoint extends LabeledPoint2D> {

	private abstract class Node { // generic node (purely abstract)
		Node() {
		}

		abstract LPoint find(Point2D pt);

		abstract Node insert(LPoint pt) throws Exception;

		abstract Node delete(Point2D pt) throws Exception;

		abstract ArrayList<String> getPreorderList();

		abstract LPoint getMinX();

		abstract LPoint getMinY();

		abstract LPoint getMaxX();

		abstract LPoint getMaxY();

		abstract LPoint findSmallerXHelper(float x, LPoint best);

		abstract LPoint findLargerXHelper(float x, LPoint best);

		abstract LPoint findSmallerYHelper(float y, LPoint best);

		abstract LPoint findLargerYHelper(float y, LPoint best);

		abstract ArrayList<LPoint> circularRange(Point2D center, float sqRadius);

		public abstract String toString();

		abstract Rectangle2D getWrapper();

		abstract void setWrapper(Rectangle2D wrapper);

		abstract LPoint fixedRadNNHelper(Point2D center, double sqRadius, LPoint best);
	}

	private class InternalNode extends Node {
		int cutDim;
		float cutVal;
		Rectangle2D wrapper;
		Node left, right;

		public InternalNode(int cutDim, float cutVal, Rectangle2D wrapper, Node left, Node right) {
			super();
			this.cutDim = cutDim;
			this.cutVal = cutVal;
			this.wrapper = wrapper;
			this.left = left;
			this.right = right;
		}

		public Rectangle2D getWrapper() {
			return this.wrapper;
		}

		@Override
		void setWrapper(Rectangle2D wrapper) {
			this.wrapper = wrapper;

		}

		@Override
		LPoint find(Point2D pt) {
			if (cutDim == 0) {
				if (pt.getX() < cutVal) {
					return left.find(pt);
				} else {
					return right.find(pt);
				}
			} else {
				if (pt.getY() < cutVal) {
					return left.find(pt);
				} else {
					return right.find(pt);
				}
			}
		}

		@Override
		Node insert(LPoint pt) throws Exception {
			if (cutDim == 0) {
				if (pt.getX() < this.cutVal) {
					left = left.insert(pt);
				} else {
					right = right.insert(pt);
				}
			} else {
				if (pt.getY() < this.cutVal) {
					left = left.insert(pt);
				} else {
					right = right.insert(pt);
				}
			}
			wrapper.add(pt.getPoint2D());
			return this;
		}

		@Override
		Node delete(Point2D pt) throws Exception {
			if (cutDim == 0) {
				if (pt.getX() < cutVal) {
					this.left = left.delete(pt);
					if (left == null) {
						return right;
					}
				} else {
					this.right = right.delete(pt);
					if (right == null) {
						return left;
					}
				}
			} else {
				if (pt.getY() < cutVal) {
					this.left = left.delete(pt);
					if (left == null) {
						return right;
					}
				} else {
					this.right = right.delete(pt);
					if (right == null) {
						return left;
					}
				}
			}
			setWrapper(Rectangle2D.union(left.getWrapper(), right.getWrapper()));
			return this;
		}

		@Override
		ArrayList<String> getPreorderList() {
			ArrayList<String> list = new ArrayList<String>();
			list.add(toString());
			list.addAll(left.getPreorderList());
			list.addAll(right.getPreorderList());
			return list;
		}

		@Override
		LPoint getMinX() {
			if (cutDim == 0) {
				return this.left.getMinX();
			} else {
				LPoint left = this.left.getMinX();
				LPoint right = this.right.getMinX();
				double leftX = left.getX();
				double leftY = left.getY();
				double rightX = right.getX();
				double rightY = right.getY();

				if (leftX < rightX) {
					return left;
				} else if (leftX == rightX) {
					if (leftY < rightY) {
						return left;
					} else {
						return right;
					}
				} else {
					return right;
				}
			}
		}

		@Override
		LPoint getMinY() {
			if (cutDim == 1) {
				return this.left.getMinY();
			} else {
				LPoint left = this.left.getMinY();
				LPoint right = this.right.getMinY();
				double leftX = left.getX();
				double leftY = left.getY();
				double rightX = right.getX();
				double rightY = right.getY();

				if (leftY < rightY) {
					return left;
				} else if (leftY == rightY) {
					if (leftX < rightX) {
						return left;
					} else {
						return right;
					}
				} else {
					return right;
				}
			}
		}

		@Override
		LPoint getMaxX() {
			if (cutDim == 0) {
				return this.right.getMaxX();
			} else {
				LPoint left = this.left.getMaxX();
				LPoint right = this.right.getMaxX();
				double leftX = left.getX();
				double leftY = left.getY();
				double rightX = right.getX();
				double rightY = right.getY();

				if (leftX > rightX) {
					return left;
				} else if (leftX == rightX) {
					if (leftY > rightY) {
						return left;
					} else {
						return right;
					}
				} else {
					return right;
				}
			}
		}

		@Override
		LPoint getMaxY() {
			if (cutDim == 1) {
				return this.right.getMaxY();
			} else {
				LPoint left = this.left.getMaxY();
				LPoint right = this.right.getMaxY();
				double leftX = left.getX();
				double leftY = left.getY();
				double rightX = right.getX();
				double rightY = right.getY();

				if (leftY > rightY) {
					return left;
				} else if (leftY == rightY) {
					if (leftX > rightX) {
						return left;
					} else {
						return right;
					}
				} else {
					return right;
				}
			}
		}

		@Override
		LPoint findSmallerXHelper(float x, LPoint best) {
			if (best == null) {
				best = left.findSmallerXHelper(x, best);
				best = right.findSmallerXHelper(x, best);
			} else if (getWrapper().low.getX() < x && getWrapper().high.getX() > best.getX() && best != null) {
				best = left.findSmallerXHelper(x, best);
				best = right.findSmallerXHelper(x, best);
			}
			return best;
		}

		@Override
		LPoint findLargerXHelper(float x, LPoint best) {
			if (best == null) {
				best = left.findLargerXHelper(x, best);
				best = right.findLargerXHelper(x, best);
			} else if (getWrapper().high.getX() > x && getWrapper().low.getX() < best.getX() && best != null) {
				best = left.findLargerXHelper(x, best);
				best = right.findLargerXHelper(x, best);
			}
			return best;
		}

		@Override
		LPoint findSmallerYHelper(float y, LPoint best) {
			if (best == null) {
				best = left.findSmallerYHelper(y, best);
				best = right.findSmallerYHelper(y, best);
			} else if (getWrapper().low.getY() < y && getWrapper().high.getY() > best.getY() && best != null) {
				best = left.findSmallerYHelper(y, best);
				best = right.findSmallerYHelper(y, best);
			}
			return best;
		}

		@Override
		LPoint findLargerYHelper(float y, LPoint best) {
			if (best == null) {
				best = left.findLargerYHelper(y, best);
				best = right.findLargerYHelper(y, best);
			} else if (getWrapper().high.getY() > y && getWrapper().low.getY() < best.getY() && best != null) {
				best = left.findLargerYHelper(y, best);
				best = right.findLargerYHelper(y, best);
			}
			return best;
		}

		@Override
		ArrayList<LPoint> circularRange(Point2D center, float sqRadius) {
			ArrayList<LPoint> list = new ArrayList<LPoint>();
			if (getWrapper().distanceSq(center) <= sqRadius) {
				list.addAll(left.circularRange(center, sqRadius));
				list.addAll(right.circularRange(center, sqRadius));
			}
			return list;
		}

		@Override
		public String toString() {
			if (cutDim == 0) {
				return "(x=" + cutVal + "): " + wrapper.toString();
			} else {
				return "(y=" + cutVal + "): " + wrapper.toString();
			}
		}

		@Override
		LPoint fixedRadNNHelper(Point2D center, double sqRadius, LPoint best) {
			if (getWrapper().distanceSq(center) >= sqRadius) {
				return best;
			} else if (best != null && getWrapper().distanceSq(center) > best.getPoint2D().distanceSq(center)) {
				return best;
			} else {
				best = left.fixedRadNNHelper(center, sqRadius, best);
				best = right.fixedRadNNHelper(center, sqRadius, best);
			}
			return best;
		}
	}

	private class ExternalNode extends Node {
		LPoint currPt;

		public ExternalNode(LPoint pt) {
			super();
			this.currPt = pt;
		}

		@Override
		Rectangle2D getWrapper() {
			return new Rectangle2D(currPt.getPoint2D(), currPt.getPoint2D());
		}

		@Override
		void setWrapper(Rectangle2D wrapper) {
			// Do nothing
		}

		@Override
		LPoint find(Point2D pt) {
			if (currPt.getPoint2D().equals(pt)) {
				return currPt;
			} else {
				return null;
			}
		}

		@Override
		Node insert(LPoint pt) throws Exception {
			if (currPt.getPoint2D().equals(pt.getPoint2D())) {
				throw new Exception("Duplicate coordinates");
			} else {
				float midPoint;
				ExternalNode q = new ExternalNode(pt);
				Rectangle2D r = new Rectangle2D(new Point2D(pt.getPoint2D()), new Point2D(currPt.getPoint2D()));
				double height = r.getWidth(1);
				double width = r.getWidth(0);
				int cuttingDim = 0;
				if (height > width) {
					cuttingDim = 1;
				}

				if (cuttingDim == 0) {
					midPoint = (float) ((pt.getX() + currPt.getX()) / 2);
				} else {
					midPoint = (float) ((pt.getY() + currPt.getY()) / 2);
				}

				InternalNode p;
				if (cuttingDim == 0) { // cuttingDim vertical, compare x values
					if (pt.getX() < currPt.getX()) {
						p = new InternalNode(cuttingDim, midPoint, r, q, this);
					} else {
						p = new InternalNode(cuttingDim, midPoint, r, this, q);
					}
				} else { // cuttingDim horizontal, compare y values
					if (pt.getY() < currPt.getY()) {
						p = new InternalNode(cuttingDim, midPoint, r, q, this);
					} else {
						p = new InternalNode(cuttingDim, midPoint, r, this, q);
					}
				}
				return p;
			}
		}

		@Override
		Node delete(Point2D pt) throws Exception {
			if (currPt.getPoint2D().equals(pt)) {
				return null;
			} else {
				throw new Exception("Deletion of nonexistent point");
			}
		}

		@Override
		ArrayList<String> getPreorderList() {
			ArrayList<String> list = new ArrayList<String>();
			list.add(toString());
			return list;
		}

		@Override
		LPoint getMinX() {
			return this.currPt;
		}

		@Override
		LPoint getMinY() {
			return this.currPt;
		}

		@Override
		LPoint getMaxX() {
			return this.currPt;
		}

		@Override
		LPoint getMaxY() {
			return this.currPt;
		}

		@Override
		LPoint findSmallerXHelper(float x, LPoint best) {
			if (best == null) {
				if (currPt.getX() < x) {
					return currPt;
				} else {
					return best;
				}
			} else {
				if (currPt.getX() < x) {
					if (currPt.getX() > best.getX()) {
						return currPt;
					} else if (currPt.getX() == best.getX()) {
						if (currPt.getY() > best.getY()) {
							return currPt;
						} else {
							return best;
						}
					} else {
						return best;
					}
				} else {
					return best;
				}
			}
		}

		@Override
		LPoint findLargerXHelper(float x, LPoint best) {
			if (best == null) {
				if (currPt.getX() > x) {
					return currPt;
				} else {
					return best;
				}
			} else {
				if (currPt.getX() > x) {
					if (currPt.getX() < best.getX()) {
						return currPt;
					} else if (currPt.getX() == best.getX()) {
						if (currPt.getY() < best.getY()) {
							return currPt;
						} else {
							return best;
						}
					} else {
						return best;
					}
				} else {
					return best;
				}
			}
		}

		@Override
		LPoint findSmallerYHelper(float y, LPoint best) {
			if (best == null) {
				if (currPt.getY() < y) {
					return currPt;
				} else {
					return best;
				}
			} else {
				if (currPt.getY() < y) {
					if (currPt.getY() > best.getY()) {
						return currPt;
					} else if (currPt.getY() == best.getY()) {
						if (currPt.getX() > best.getX()) {
							return currPt;
						} else {
							return best;
						}
					} else {
						return best;
					}
				} else {
					return best;
				}
			}
		}

		@Override
		LPoint findLargerYHelper(float y, LPoint best) {
			if (best == null) {
				if (currPt.getY() > y) {
					return currPt;
				} else {
					return best;
				}
			} else {
				if (currPt.getY() > y) {
					if (currPt.getY() < best.getY()) {
						return currPt;
					} else if (currPt.getY() == best.getY()) {
						if (currPt.getX() < best.getX()) {
							return currPt;
						} else {
							return best;
						}
					} else {
						return best;
					}
				} else {
					return best;
				}
			}
		}

		@Override
		ArrayList<LPoint> circularRange(Point2D center, float sqRadius) {
			ArrayList<LPoint> list = new ArrayList<LPoint>();
			if (currPt.getPoint2D().distanceSq(center) <= sqRadius) {
				list.add(currPt);
			}
			return list;
		}

		@Override
		public String toString() {
			return "[" + currPt.toString() + "]";
		}

		@Override
		LPoint fixedRadNNHelper(Point2D center, double sqRadius, LPoint best) {
			if (best == null) {
				if (this.currPt.getPoint2D().distanceSq(center) != 0
						&& this.currPt.getPoint2D().distanceSq(center) < sqRadius) {
					return currPt;
				} else {
					return best;
				}
			} else {
				if (this.currPt.getPoint2D().distanceSq(center) == 0
						|| this.currPt.getPoint2D().distanceSq(center) >= sqRadius
						|| (this.currPt.getPoint2D().distanceSq(center) > best.getPoint2D().distanceSq(center)
								&& best != null)) {
					return best;
				} else {
					if (this.currPt.getPoint2D().distanceSq(center) == best.getPoint2D().distanceSq(center)) {
						if (this.currPt.getX() < best.getX()) {
							return currPt;
						} else if (this.currPt.getX() == best.getX() 
								&& this.currPt.getY() < best.getY()) {
							return currPt;
						} else {
							return best;
						}
					} else {
						return currPt;
					}
				}		
			}
		}
	}

	Node root;
	int size;

	public WKDTree() {
		clear();
	}

	public LPoint find(Point2D pt) {
		if (root == null) {
			return null;
		} else {
			return root.find(pt);
		}
	}

	public void insert(LPoint pt) throws Exception {
		if (root == null) {
			root = new ExternalNode(pt);
		} else {
			root = root.insert(pt);
		}
		size++;
	}

	public void delete(Point2D pt) throws Exception {
		if (root == null) {
			throw new Exception("Deletion of nonexistent point");
		} else {
			root = root.delete(pt);
		}

		size--;
	}

	public ArrayList<String> getPreorderList() {
		if (root == null) {
			return new ArrayList<String>();
		} else {
			return root.getPreorderList();
		}
	}

	public void clear() {
		root = null;
		size = 0;
	}

	public int size() {
		return size;
	}

	public LPoint getMinX() {
		if (root == null) {
			return null;
		} else {
			return root.getMinX();
		}
	}

	public LPoint getMaxX() {
		if (root == null) {
			return null;
		} else {
			return root.getMaxX();
		}
	}

	public LPoint getMinY() {
		if (root == null) {
			return null;
		} else {
			return root.getMinY();
		}
	}

	public LPoint getMaxY() {
		if (root == null) {
			return null;
		} else {
			return root.getMaxY();
		}
	}

	public LPoint findSmallerX(float x) {
		LPoint best = null;
		if (root == null) {
			return null;
		} else {
			return root.findSmallerXHelper(x, best);
		}
	}

	public LPoint findLargerX(float x) {
		LPoint best = null;
		if (root == null) {
			return null;
		} else {
			return root.findLargerXHelper(x, best);
		}
	}

	public LPoint findSmallerY(float y) {
		LPoint best = null;
		if (root == null) {
			return null;
		} else {
			return root.findSmallerYHelper(y, best);
		}
	}

	public LPoint findLargerY(float y) {
		LPoint best = null;
		if (root == null) {
			return null;
		} else {
			return root.findLargerYHelper(y, best);
		}
	}

	public ArrayList<LPoint> circularRange(Point2D center, float sqRadius) {
		if (root == null) {
			return new ArrayList<LPoint>();
		} else {
			return root.circularRange(center, sqRadius);
		}
	}

	// -----------------------------------------------------------------
	// New for Programming Assignment 3 - Fixed-radius nearest neighbor
	// -----------------------------------------------------------------

	public LPoint fixedRadNN(Point2D center, double sqRadius) {
		LPoint best = null;
		if (root == null) {
			return null;
		} else {
			return root.fixedRadNNHelper(center, sqRadius, best);
		}
	}

}