# Programming Assignment 2: Wrapped k-d Trees

# Overview: 

In this assignment you will implement a variant of the kd-tree data structure, called
a wrapped kd-tree (or WKDTree) to store a set of points in 2-dimensional space. This data
structure will support insertion, deletion, and a number of geometric queries, some of which
will be used later in Part 3 of the programming assignment.

The data structure will be templated with the point type, which is any class that implements
the Java interface LabeledPoint2D, as described in the file LabeledPoint2D.java from the
provided skeleton code. A labeled point is a 2-dimensional point (Point2D from the skeleton
code) that supports an additional function getLabel(). This returns a string associated with
the point.

In our case, the points will be the airports from our earlier projects, and the labels will be
the 3-letter airport codes. The associated point (represented as a Point2D) can be extracted
using the function getPoint2D(). The individual coordinates (which are floats) can be
extracted directly using the functions getX() and getY(), or get(i), where i = 0 for x and
i = 1 for y.

Your wrapped kd-tree will be templated with one type, which we will call LPoint (for “labeled
point”). For example, your file WKDTree will contain the following public class:

public class W KDTree<LPoint extends LabeledPoint2D> { ... }
