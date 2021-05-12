package cmsc420_s21;

// YOU SHOULD NOT MODIFY THIS FILE

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Command handler. This inputs a single command line, processes the command (by
 * invoking the appropriate method(s) on the WKDTree)) and returns the result as
 * a string.
 */

public class CommandHandler {

	private AAXTree<String, Airport> aaxTree; // the AAX Tree (dictionary)
	private WKDTree<Airport> wkdTree; // the WKD Tree (kd-tree)

	/**
	 * Initialize command handler
	 */
	public CommandHandler() {
		aaxTree = new AAXTree<String, Airport>();
		wkdTree = new WKDTree<Airport>();
	}

	/**
	 * Process a single command and return the string output. Each line begins with
	 * a command (e.g., find, insert, delete) followed by a list of arguments. The
	 * arguments are separated by colons (":"). For example, the command to delete
	 * the LAX airport is "delete:LAX".
	 *
	 * The preorder command gets a listing of the airports in preorder. It prints
	 * this list and also generates a nicely indented (inorder) presentation of the
	 * tree's contents.
	 * 
	 * @param inputLine The input line with the command and parameters.
	 * @return A short summary of the command's execution/result.
	 */

	public String processCommand(String inputLine) throws Exception {
		String output = new String(); // for storing summary output
		Scanner line = new Scanner(inputLine);
		try {
			line.useDelimiter(":"); // use ":" to separate arguments
			String cmd = (line.hasNext() ? line.next() : ""); // next command

			// -----------------------------------------------------
			// INSERT code name city country x y
			// -----------------------------------------------------
			if (cmd.compareTo("insert") == 0) {
				String code = line.next(); // get parameters
				String name = line.next();
				String city = line.next();
				String country = line.next();
				float x = line.nextFloat();
				float y = line.nextFloat();
				Airport ap = new Airport(code, name, city, country, x, y); // create airport object
				output += "insert(" + code + "): ";
				aaxTree.insert(code, ap); // insert into dictionary
				wkdTree.insert(ap); // insert into kd-tree
				output += "successful {" + ap.getString("attributes") + "}" + System.lineSeparator();
			}
			// -----------------------------------------------------
			// DELETE code
			// -----------------------------------------------------
			else if (cmd.compareTo("delete") == 0) {
				String code = line.next(); // get parameters
				output += "delete(" + code + "): ";
				Airport ap = aaxTree.find(code); // look up the airport
				if (ap == null) { // no such airport?
					throw new Exception("Deletion of nonexistent airport code");
				}
				wkdTree.delete(ap.getPoint2D()); // delete from kd-tree
				aaxTree.delete(code); // delete from dictionary
				output += "successful" + System.lineSeparator();
			}
			// -----------------------------------------------------
			// DELETE-POINT
			// -----------------------------------------------------
			else if (cmd.compareTo("delete-point") == 0) {
				float px = line.nextFloat();
				float py = line.nextFloat();
				Point2D pt = new Point2D(px, py);
				output += "delete(" + pt + "): ";
				wkdTree.delete(pt); // delete from kd-tree
				output += "successful (Warning: kd-tree and AA tree are now out of sync!)" + System.lineSeparator();
			}
			// -----------------------------------------------------
			// CLEAR
			// -----------------------------------------------------
			else if (cmd.compareTo("clear") == 0) {
				wkdTree.clear(); // clear the wkd-tree
				aaxTree.clear(); // clear the aax-tree
				output += "clear: successful" + System.lineSeparator();
			}
			// -----------------------------------------------------
			// SIZE
			// -----------------------------------------------------
			else if (cmd.compareTo("size") == 0) {
				int size = wkdTree.size(); // get the tree's current size
				output += "size: " + size + System.lineSeparator();
			}
			// -----------------------------------------------------
			// FIND code
			// -----------------------------------------------------
			else if (cmd.compareTo("find") == 0) {
				float x = line.nextFloat();
				float y = line.nextFloat();
				Point2D pt = new Point2D(x, y);
				Airport result = wkdTree.find(pt); // find in tree
				output += summarizeFind(cmd, pt, result); // summarize result
			}
			// -----------------------------------------------------
			// PREORDER - get a preorder list of entries and print
			// the tree with indentation
			// -----------------------------------------------------
			else if (cmd.compareTo("preorder") == 0) {
				ArrayList<String> list = wkdTree.getPreorderList();
				if (list == null) throw new Exception("Error - getPreorderList returned a null result");
				Iterator<String> iter = list.iterator(); // iterator for the list
				output += "Preorder list:" + System.lineSeparator();
				while (iter.hasNext()) { // output the preorder list (flat)
					output += "  " + iter.next() + System.lineSeparator();
				}
				output += treeStructure(list); // summarize tree contents (indented)
			}
			// -----------------------------------------------------
			// GET-MIN/MAX-X/Y
			// -----------------------------------------------------
			else if (cmd.compareTo("get-min-x") == 0) {
				Airport result = wkdTree.getMinX(); // get min-x in tree
				output += summarizeGet(cmd, result); // summarize result
			}
			else if (cmd.compareTo("get-max-x") == 0) {
				Airport result = wkdTree.getMaxX(); // get max-x in tree
				output += summarizeGet(cmd, result); // summarize result
			}
			else if (cmd.compareTo("get-min-y") == 0) {
				Airport result = wkdTree.getMinY(); // get min-y in tree
				output += summarizeGet(cmd, result); // summarize result
			}
			else if (cmd.compareTo("get-max-y") == 0) {
				Airport result = wkdTree.getMaxY(); // get max-y in tree
				output += summarizeGet(cmd, result); // summarize result
			}
			// -----------------------------------------------------
			// FIND-SMALLER/LARGER-X/Y
			// -----------------------------------------------------
			else if (cmd.compareTo("find-smaller-x") == 0) {
				float x = line.nextFloat();
				Airport result = wkdTree.findSmallerX(x); // find smaller in tree
				output += summarizeFindSL(cmd, x, result); // summarize result
			}
			else if (cmd.compareTo("find-larger-x") == 0) {
				float x = line.nextFloat();
				Airport result = wkdTree.findLargerX(x); // find smaller in tree
				output += summarizeFindSL(cmd, x, result); // summarize result
			}
			else if (cmd.compareTo("find-smaller-y") == 0) {
				float y = line.nextFloat();
				Airport result = wkdTree.findSmallerY(y); // find smaller in tree
				output += summarizeFindSL(cmd, y, result); // summarize result
			}
			else if (cmd.compareTo("find-larger-y") == 0) {
				float y = line.nextFloat();
				Airport result = wkdTree.findLargerY(y); // find smaller in tree
				output += summarizeFindSL(cmd, y, result); // summarize result
			}
			// -----------------------------------------------------
			// CIRCULAR-RANGE - return the points that lie within
			// circular disk. The points are sorted (based on the
			// compareTo function for Airports).
			// -----------------------------------------------------
			else if (cmd.compareTo("circular-range") == 0) {
				float cx = line.nextFloat();
				float cy = line.nextFloat();
				Point2D center = new Point2D(cx, cy);
				float sqRadius = line.nextFloat();
				ArrayList<Airport> list = wkdTree.circularRange(center, sqRadius);
				if (list == null) throw new Exception("Error - circularRange returned a null result");
				Collections.sort(list); // sort by code
				Iterator<Airport> iter = list.iterator(); // iterator for the list
				output += "Points within squared distance " + sqRadius + " (standard distance " + Math.sqrt(sqRadius) + ") of " + center + ":" + System.lineSeparator();
				while (iter.hasNext()) { // output the preorder list (flat)
					output += "  " + iter.next() + System.lineSeparator();
				}
			}
			// 
			// -----------------------------------------------------
			// Invalid command or empty
			// -----------------------------------------------------
			else {
				if (cmd.compareTo("") == 0)
					System.err.println("Error: Empty command line (Ignored)");
				else
					System.err.println("Error: Invalid command - \"" + cmd + "\" (Ignored)");
			}
		} catch (Exception e) { // exception thrown?
			output += "failure due to exception: \"" + e.getMessage() + "\"" + System.lineSeparator();
		} catch (Error e) { // error occurred?
			System.err.print("Operation failed due to error: " + e.getMessage());
			e.printStackTrace(System.err);
		} finally { // always executed
			line.close(); // close the input scanner
		}
		return output; // return summary output
	}

	/**
	 * Summarize the results of the find commands.
	 * 
	 * @param cmd    The command (find)
	 * @param result The find result (null, if not found)
	 * @return String encoding the results
	 */
	static String summarizeFind(String cmd, Point2D pt, Airport result) {
		String output = new String(cmd + "(" + pt + "): ");
		if (result != null) {
			output += "found [" + result + "]" + System.lineSeparator();
		} else {
			output += "not found" + System.lineSeparator();
		}
		return output;
	}

	/**
	 * Summarize the results of the find commands.
	 * 
	 * @param cmd    The command (find-smaller, find-larger)
	 * @param result The find result (null, if not found)
	 * @return String encoding the results
	 */
	static String summarizeFindSL(String cmd, float x, Airport result) {
		String output = new String(cmd + "(" + x + "): ");
		if (result != null) {
			output += "found [" + result + "]" + System.lineSeparator();
		} else {
			output += "not found" + System.lineSeparator();
		}
		return output;
	}

	/**
	 * Summarize the results of the get command.
	 * 
	 * @param cmd    The command (get-min or get-max)
	 * @param result The find result (null, if not found)
	 * @return String encoding the results
	 */
	static String summarizeGet(String cmd, Airport result) {
		String output = new String(cmd + ": ");
		if (result != null) {
			output += "found [" + result + "]" + System.lineSeparator();
		} else {
			output += "no entries" + System.lineSeparator();
		}
		return output;
	}

	/**
	 * Summarize the results of the get command.
	 * 
	 * @param cmd    The command (remove-min or remove-max)
	 * @param result The find result (null, if not found)
	 * @return String encoding the results
	 */
	static String summarizeRemove(String cmd, Airport result) {
		String output = new String(cmd + ": ");
		if (result != null) {
			output += "removed [" + result + "]" + System.lineSeparator();
		} else {
			output += "no entries" + System.lineSeparator();
		}
		return output;
	}

	/**
	 * Print the tree contents with indentation.
	 * 
	 * @param entries List of entries in preorder
	 * @return String encoding the tree structure
	 */
	static String treeStructure(ArrayList<String> entries) {
		String output = "Tree structure:" + System.lineSeparator();
		Iterator<String> iter = entries.iterator(); // iterator for the list
		if (iter.hasNext()) { // tree is nonempty
			output += treeStructureHelper(iter, "  "); // print everything
		}
		return output;
	}

	/**
	 * Recursive helper for treeStructure. The argument iterator specifies the next
	 * node from the preorder list to be printed, and the argument indent indicates
	 * the indentation to be performed (of the form "| | | ...").
	 * 
	 * @param iter   Iterator for the entries in the list
	 * @param indent String indentation for the current line
	 */
	static String treeStructureHelper(Iterator<String> iter, String indent) {
		final String levelIndent = "| "; // the indentation for each level of the tree
		String output = "";
		if (iter.hasNext()) {
			String entry = iter.next(); // get the next entry
			Boolean isExtern = (entry.length() > 0 && entry.charAt(0) == '['); // external?
			if (isExtern) { // print external node entry
				output += indent + entry + System.lineSeparator();
			} else {
				output += treeStructureHelper(iter, indent + levelIndent); // print left subtree
				output += indent + entry + System.lineSeparator(); // print this node
				output += treeStructureHelper(iter, indent + levelIndent); // print right subtree
			}
		} else {
			System.err.println("Unexpected trailing elements in entries list"); // shouldn't get here!
		}
		return output;
	}
}
