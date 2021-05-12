package cmsc420_s21; // Don't delete this or your file won't pass the autograder

import java.util.ArrayList;
import java.util.Stack;

/** AAXTree (skeleton)
 *
 * MODIFY THE FOLLOWING CLASS.
 *
 * You are free to make whatever changes you like or to create additional
 * classes and files, but avoid reusing/modifying the other files given in this
 * folder.
 */

public class AAXTree<Key extends Comparable<Key>, Value> {

	private abstract class Node { // generic node (purely abstract)
		abstract Value find(Key x);

		abstract Node insert(Key x, Value v) throws Exception;

		abstract Node getLeft();

		abstract Node getRight();

		abstract int getLevel();

		abstract void setLeft(Node v);

		abstract void setRight(Node v);

		abstract void setLevel(int l);

		abstract Node skew();

		abstract Node split();

		abstract void updateHeight();

		abstract Node fixAfterDelete();

		public abstract String toString();

		abstract Value getMin();

		abstract Value getMax();

		abstract Key removeMin();

		abstract Key removeMax();

		abstract Node delete(Key x) throws Exception;

		abstract Value findSmaller(Key x, Stack<String> stk, boolean secnd);
		
		abstract Value findLarger(Key x, Stack<String> stk, boolean secnd);
		
		abstract ArrayList<String> getPreorderList(ArrayList<String> lst);
		
		abstract Node replace(Key k, Value v) throws Exception;
	}

	private class InternalNode extends Node { // internal node
		Key key;
		Node left, right;
		int level;

		/* constructor */
		public InternalNode(Key key, int level) {
			this.key = key;
			this.level = level;
		}

		/*
		 * if x < key, calls find on left node. else calls find on right node
		 */
		Value find(Key x) {
			if (x.compareTo(this.key) < 0) {
				return left.find(x);
			} else {
				return right.find(x);
			}
		}

		/*
		 * if x < key, calls insert on left node. else calls insert on right node
		 */
		Node insert(Key x, Value v) throws Exception {
			if (x.compareTo(this.key) < 0) {
				this.left = left.insert(x, v);
			} else {
				this.right = right.insert(x, v);
			}
			return skew().split();
		}

		/*
		 * skew method (followed the explanation on CMSC 420: Lecture 7 Red-black and AA
		 * trees
		 * http://www.cs.umd.edu/class/spring2021/cmsc420-0101/Lects/lect07-red-black.
		 * pdf)
		 */
		Node skew() {
			if (getLeft().getLevel() == level) {
				Node q = getLeft();
				setLeft(q.getRight());
				q.setRight(this);
				return q;
			} else {
				return this;
			}
		}

		/*
		 * split method (followed the explanation on CMSC 420: Lecture 7 Red-black and
		 * AA trees
		 * http://www.cs.umd.edu/class/spring2021/cmsc420-0101/Lects/lect07-red-black.
		 * pdf)
		 */
		Node split() {
			if (this.getRight().getRight().getLevel() == this.level) {
				Node q = getRight();
				setRight(q.getLeft());
				q.setLeft(this);
				q.setLevel(q.getLevel() + 1);
				return q;
			} else {
				return this;
			}
		}

		/* getters */
		@Override
		AAXTree<Key, Value>.Node getLeft() {
			return this.left;
		}

		@Override
		AAXTree<Key, Value>.Node getRight() {
			return this.right;
		}

		@Override
		int getLevel() {
			return this.level;
		}

		/* setters */
		@Override
		void setLeft(AAXTree<Key, Value>.Node v) {
			this.left = v;
		}

		@Override
		void setRight(AAXTree<Key, Value>.Node v) {
			this.right = v;
		}

		@Override
		void setLevel(int l) {
			this.level = l;
		}

		@Override
		public String toString() {
			String str = "(" + key + ") " + level;
			return str;
		}

		@Override
		Value getMin() {
			return left.getMin();
		}

		@Override
		Value getMax() {
			return right.getMax();
		}

		@Override
		void updateHeight() {
			int idealLevel = 1 + Math.min(this.getLeft().getLevel(), this.getRight().getLevel());
			if (this.getLevel() > idealLevel) {
				this.setLevel(idealLevel);
				if (this.getRight().getLevel() > idealLevel) {
					this.getRight().setLevel(idealLevel);
				}
			}
		}

		@Override
		AAXTree<Key, Value>.Node fixAfterDelete() {
			this.updateHeight();
			Node a = this.skew();
			if (a.getRight() instanceof AAXTree.InternalNode) {
				a.setRight(a.getRight().skew());
			}
			if (a.getRight().getRight() instanceof AAXTree.InternalNode) {
				a.getRight().setRight(a.getRight().getRight().skew());
			}
			a = a.split();
			if (a.getRight() instanceof AAXTree.InternalNode) {
				a.setRight(a.getRight().split());
			}
			return a;
		}

		@Override
		AAXTree<Key, Value>.Node delete(Key x) throws Exception {
			// System.out.println("order: "+counter++ + " curr: "+this.toString());
			if (x.compareTo(this.key) < 0) {
				this.left = left.delete(x);
				if (this.left == null) {
					return this.getRight();
				}
			} else {
				this.right = right.delete(x);
				if (this.right == null) {
					return this.getLeft();
				}
			}
			return this.fixAfterDelete();
		}

		@Override
		Key removeMin() {
			return left.removeMin();
		}

		@Override
		Key removeMax() {
			return right.removeMax();
		}

		@Override
		Value findSmaller(Key x, Stack<String> stk, boolean secnd) {
			if (secnd) {
				if (stk.peek().equals("R")) {
					stk.pop();
					return right.findSmaller(x, stk, secnd);
				} else {
					stk.pop();
					return left.findSmaller(x, stk, secnd);
				}
			} else {
				if (x.compareTo(this.key) < 0) {
					Value v = left.findSmaller(x, stk, secnd);
					if (v != null) {
						return v;
					} else {
						if (x.compareTo(this.key) > 0) {
							secnd = true;
							return right.findSmaller(x, stk, secnd);
						} else {
							stk.push("R");
							return null;
						}
					}
				} else {
					Value v = right.findSmaller(x, stk, secnd);
					if (v != null) {
						return v;
					} else {
						if (x.compareTo(this.key) <= 0) {
							secnd = true;
							return left.findSmaller(x, stk, secnd);
						} else {
							stk.push("L");
							return null;
						}
					}
				}
			}
		}

		@Override
		Value findLarger(Key x, Stack<String> stk, boolean secnd) {
			if (secnd) {
				if (stk.peek().equals("R")) {
					stk.pop();
					return right.findLarger(x, stk, secnd);
				} else {
					stk.pop();
					return left.findLarger(x, stk, secnd);
				}
			} else {
				if (x.compareTo(this.key) < 0) {
					Value v = left.findLarger(x, stk, secnd);
					if (v != null) {
						return v;
					} else {
						if (x.compareTo(this.key) < 0) {
							secnd = true;
							return right.findLarger(x, stk, secnd);
						} else {
							stk.push("R");
							return null;
						}
					}
				} else {
					Value v = right.findLarger(x, stk, secnd);
					if (v != null) {
						return v;
					} else {
						if (x.compareTo(this.key) < 0) {
							secnd = true;
							return left.findLarger(x, stk, secnd);
						} else {
							stk.push("L");
							return null;
						}
					}
				}
			}
		}

		@Override
		ArrayList<String> getPreorderList(ArrayList<String> lst) {
			lst.add(this.toString());
			getLeft().getPreorderList(lst);
			getRight().getPreorderList(lst);
			return lst;
		}

		@Override
		Node replace(Key k, Value v) throws Exception {
			if (k.compareTo(this.key) < 0) {
				left = left.replace(k, v);
			} else {
				right = right.replace(k, v);
			}
			return this;
		}
	}

	private class ExternalNode extends Node { // external node
		Key key;
		Value value;

		/* constrcutor method */
		public ExternalNode(Key key, Value value) {
			this.key = key;
			this.value = value;
		}

		/* if x matches with key, return node's value */
		Value find(Key x) {
			if (x.equals(this.key)) {
				return this.value;
			} else {
				return null;
			}
		}

		/*
		 * insert method (followed the explanation on Supplemental: Extended Search
		 * Trees insertion External
		 * http://www.cs.umd.edu/class/spring2021/cmsc420-0101/Lects/lectX01-ext-aa.pdf)
		 */
		Node insert(Key x, Value v) throws Exception {
			if (x.equals(this.key)) {
				throw new Exception("Insertion of duplicate key");
			} else {
				Node exNode = new ExternalNode(x, v);
				if (x.compareTo(this.key) < 0) {
					Node inNode = new InternalNode(this.key, 1);
					inNode.setLeft(exNode);
					inNode.setRight(this);
					return inNode;
				} else {
					Node inNode = new InternalNode(x, 1);
					inNode.setLeft(this);
					inNode.setRight(exNode);
					return inNode;
				}

			}

		}

		/* getters */
		@Override
		AAXTree<Key, Value>.Node getLeft() {
			return this;
		}

		@Override
		AAXTree<Key, Value>.Node getRight() {
			return this;
		}

		@Override
		int getLevel() {
			return 0;
		}

		/* setters */
		@Override
		void setLeft(AAXTree<Key, Value>.Node v) {
		}

		@Override
		void setRight(AAXTree<Key, Value>.Node v) {
		}

		@Override
		void setLevel(int l) {
		}

		@Override
		AAXTree<Key, Value>.Node skew() {
			return null;
		}

		@Override
		AAXTree<Key, Value>.Node split() {
			return null;
		}

		@Override
		public String toString() {
			String str = "[" + key + " " + value + "]";
			return str;
		}

		@Override
		Value getMin() {
			return this.value;
		}

		@Override
		Value getMax() {
			return this.value;
		}

		@Override
		void updateHeight() {
			// Do Nothing
		}

		@Override
		AAXTree<Key, Value>.Node fixAfterDelete() {
			// Do Nothing
			return null;
		}

		@Override
		AAXTree<Key, Value>.Node delete(Key x) throws Exception {
			if (!this.key.equals(x)) {
				throw new Exception("Delete of nonexistent object");
			} else {
				return null;
			}
		}

		@Override
		Key removeMin() {
			return this.key;
		}

		@Override
		Key removeMax() {
			return this.key;
		}

		@Override
		Value findSmaller(Key x, Stack<String> stk, boolean secnd) {
			if (secnd) {
				return this.value;
			} else {
				if (x.compareTo(this.key) > 0) {
					return this.value;
				} else {
					return null;
				}
			}
		}

		@Override
		Value findLarger(Key x, Stack<String> stk, boolean secnd) {
			if (secnd) {
				return this.value;
			} else {
				if (x.compareTo(this.key) < 0) {
					return this.value;
				} else {
					return null;
				}
			}
		}

		@Override
		ArrayList<String> getPreorderList(ArrayList<String> lst) {
			lst.add(this.toString());
			return lst;
		}

		@Override
		Node replace(Key k, Value v) throws Exception {
			if (!this.key.equals(k)) {
				throw new Exception("Replacement of nonexistent key");
			} else {
				this.value = v;
				return this;
			}	
		}

	}

	Node root; // root node
	int size; // used for size() method

	// ------------------------------------------------------------------
	// Needed for Part a
	// ------------------------------------------------------------------
	public AAXTree() {
		clear();
	}

	/*
	 * checks if the root is null, if so it returns null. else it calls the find
	 * method on the root
	 */
	public Value find(Key k) {
		if (root == null) {
			return null;
		} else {
			return root.find(k);
		}
	}

	/*
	 * checks to see if root is null, if so the root is the new external node. else
	 * it calls the insert function on the root
	 */
	public void insert(Key x, Value v) throws Exception {
		if (root == null) {
			root = new ExternalNode(x, v);
		} else {
			root = root.insert(x, v);
		}
		size++;
	}

	/* clears the tree. ie makes the root null */
	public void clear() {
		root = null;
		size = 0;
	}

	/*
	 * if root is null, returns empty list. else it calls list helper
	 */
	public ArrayList<String> getPreorderList() {
		ArrayList<String> preList = new ArrayList<String>();
		if (root == null) {
			return preList;
		}
		return root.getPreorderList(preList);
	}

	// ------------------------------------------------------------------
	// Needed for Part b
	// ------------------------------------------------------------------
	/* deletes the Key x from the tree */
	public void delete(Key x) throws Exception {
		if (root == null) {
			// System.out.print("here");
			throw new Exception("Delete of nonexistent object");
		} else {
			root = root.delete(x);
			size--;
		}
	}

	/* returns size of the tree */
	public int size() {
		return size;
	}

	/* returns min value of tree */
	public Value getMin() {
		if (root == null) {
			return null;
		} else {
			return root.getMin();
		}
	}

	/* returns max value of tree */
	public Value getMax() {
		if (root == null) {
			return null;
		} else {
			return root.getMax();
		}
	}

	public Value findSmaller(Key x) {
		if (root == null) {
			return null;
		} else {
			Stack<String> stk = new Stack<String>();
			return root.findSmaller(x, stk, false);
		}
	}

	public Value findLarger(Key x) {
		if (root == null) {
			return null;
		} else {
			Stack<String> stk = new Stack<String>();
			return root.findLarger(x, stk, false);
		}
	}

	/* removes the min value of the tree */
	public Value removeMin() {
		if (root == null) {
			return null;
		} else {
			Value v = getMin();
			try {
				root = root.delete(root.removeMin());
				size--;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return v;
		}
	}

	/* removes the max value of the tree */
	public Value removeMax() {
		if (root == null) {
			return null;
		} else {
			Value v = getMax();
			try {
				root = root.delete(root.removeMax());
				size--;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return v;
		}
	}



	// -----------------------------------------------------------------
	// New for Programming Assignment 3 - Replace value for key k. Throw
	// exception if key not found.
	// -----------------------------------------------------------------

	public void replace(Key k, Value v) throws Exception { 
		if (root == null) {
			throw new Exception("Replacement of nonexistent key");
		} else {
			root = root.replace(k, v);
		}
	}

}
