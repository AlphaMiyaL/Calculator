//Node for Tree

public class Node {
	//variables
	String value;
	Node pointer1;
	Node pointer2;
	Node pointer3;
	
	//constructor
	public Node() {
	}
	public Node(String a) {
		value=a;
	}
	public Node(String a, Node b) {
		value=a;
		pointer1=b;
	}
	public Node(String a, Node b, Node c) {
		value=a;
		pointer1=b;
		pointer2=c;
	}
	
	//methods
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Node getLeft() {
		return pointer1;
	}
	public void setLeft(Node pointer1) {
		this.pointer1 = pointer1;
	}
	public Node getRight() {
		return pointer2;
	}
	public void setRight(Node pointer2) {
		this.pointer2 = pointer2;
	}
	public Node getParent() {
		return pointer3;
	}
	public void setParent(Node pointer3) {
		this.pointer3 = pointer3;
	}
}
