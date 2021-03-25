# Programming Assignment 1: Extended AA Trees

**For this project, I implemeted Part1-Skeleton/cmsc420_s21/AAXTree.java**

# Overview: 
In this assignment you will implement an extended variant of the AA Tree. Recall that
an extended binary tree there are two different node types, internal nodes and external nodes.
Extended trees are used in many applications. By separating node types, we can better tailor
each node to its particular function. Data (that is, the key-value pairs) are stored only in the
external nodes, which internal nodes only store keys, called splitters. Together, the internal
nodes serve as an index, directing the search to the appropriate external node where the data
is stored.

Our extended AA tree, or AAXTree, will be templated by two types Key and Value. Our
only assumption regarding these types is that the Key type implements the Java Comparable
interface, meaning that it defines a function compareTo() for comparing keys. In our test
data, keys will be of type String and values of type Airport.
