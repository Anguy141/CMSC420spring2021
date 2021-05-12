package cmsc420_s21; // Don't delete this or your file won't pass the autograder

import java.util.ArrayList;

public class Tour<LPoint extends LabeledPoint2D> {

	ArrayList<LPoint> Tour;
	AAXTree<String, Integer> Locator;
	WKDTree<LPoint> SpatialIndex;
	int index;

	public Tour() {
		clear();
	}

	public void append(LPoint pt) throws Exception {
		if (Tour.contains(pt)) {
			throw new Exception("Duplicate label");
		} else {
			Tour.add(pt);
			Locator.insert(pt.getLabel(), index);
			SpatialIndex.insert(pt);
			index++;
		}
	}

	public ArrayList<LPoint> list() {
		return Tour;
	}

	public void clear() {
		Tour = new ArrayList<LPoint>();
		Locator = new AAXTree<String, Integer>();
		SpatialIndex = new WKDTree<LPoint>();
		index = 0;
	}

	public double cost() {
		double totalCost = 0;
		if (Tour.size() == 1 || Tour.size() == 0) {
			return totalCost;
		} else {
			for (int i = 0; i < Tour.size() - 1; i++) {
				totalCost += Tour.get(i).getPoint2D().distanceSq(Tour.get(i + 1).getPoint2D());
			}
			return totalCost += Tour.get(Tour.size() - 1).getPoint2D().distanceSq(Tour.get(0).getPoint2D());
		}
	}

	public void reverse(String label1, String label2) throws Exception {
		if (Locator.find(label1) == null || Locator.find(label2) == null) {
			throw new Exception("Label not found");
		} else if (label1.equals(label2)) {
			throw new Exception("Duplicate label");
		} else {

			int min = Math.min(Locator.find(label1), Locator.find(label2)) + 1;
			int max = Math.max(Locator.find(label1), Locator.find(label2));

			int i = min;
			int j = max;
			LPoint temp;

			while (i < j) {
				temp = Tour.get(i);
				Tour.set(i, Tour.get(j));
				Tour.set(j, temp);
				i++;
				j--;
			}
			i = min;
			j = max;
			while (i <= j) {
				Locator.replace(Tour.get(i).getLabel(), i);
				i++;
			}
		}
	}

	public boolean twoOpt(String label1, String label2) throws Exception {
		if (Locator.find(label1) == null || Locator.find(label2) == null) {
			throw new Exception("Label not found");
		} else if (label1.equals(label2)) {
			throw new Exception("Duplicate label");
		} else {
			double change;
			int i = Math.min(Locator.find(label1), Locator.find(label2));
			int j = Math.max(Locator.find(label1), Locator.find(label2));
			if (i == j) {
				return false;
			}
			Point2D pi = Tour.get(i).getPoint2D();
			Point2D pj = Tour.get(j).getPoint2D();
			Point2D piPlus1 = Tour.get(i + 1).getPoint2D();
			Point2D pjPlus1 = j + 1 == Tour.size() ? Tour.get(0).getPoint2D() : Tour.get(j + 1).getPoint2D();

			change = ((pi.distanceSq(pj) + piPlus1.distanceSq(pjPlus1))
					- (pi.distanceSq(piPlus1) + pj.distanceSq(pjPlus1)));

			if (change < 0) {
				reverse(label1, label2);
				return true;
			} else {
				return false;
			}
		}
	}

	public LPoint twoOptNN(String label) throws Exception {
		if (Locator.find(label) == null) {
			throw new Exception("Label not found");
		} else {
			int i = Locator.find(label);
			Point2D pi = Tour.get(i).getPoint2D();
			Point2D piPlus1 = i + 1 == Tour.size() ? Tour.get(0).getPoint2D() : Tour.get(i + 1).getPoint2D();
			double sqRadius = pi.distanceSq(piPlus1);

			LPoint result = SpatialIndex.fixedRadNN(pi, sqRadius);

			if (result != null) {
				if (twoOpt(label, result.getLabel()) == false) {
					return null;
				}
			}
			return result;
		}
	}

	public int allTwoOpt() {
		int count = 0;
		for (int i = 0; i <= Tour.size() - 1; i++) {
			for (int j = i + 1; j <= Tour.size() - 1; j++) {
				try {
					if (twoOpt(Tour.get(i).getLabel(), Tour.get(j).getLabel())) {
						count++;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return count;
	}
}