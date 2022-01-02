//Tree for Calculator

public class CalcTree {
	//variables
	Node root;
	
	//constructor
	public CalcTree() {
	}
	public CalcTree(Node a) {
		root=a;
	}
	
	//methods
	public Node getRoot() {
		return root;
	}
	public void setRoot(Node root) {
		this.root = root;
	}
}
