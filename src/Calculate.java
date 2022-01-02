import java.util.*;

public class Calculate {
	public static String add(String x, String y) {
		double nya = Double.parseDouble(x);
		double meow = Double.parseDouble(y);
		return(Double.toString(nya+meow));
	}

	public static String sub(String x, String y) {
		double nya = Double.parseDouble(x);
		double meow = Double.parseDouble(y);
		return(Double.toString(nya-meow));
	}

	public static String mult(String x, String y) {
		double nya = Double.parseDouble(x);
		double meow = Double.parseDouble(y);
		return(Double.toString(nya*meow));
	}

	public static String divi(String x, String y) throws CalcExceptions {
		double nya = Double.parseDouble(x);
		double meow = Double.parseDouble(y);
		if(meow==0) {
			throw new CalcExceptions("Error : Cannot Divide by Zero");
		}
		return(Double.toString(nya/meow));
	}

	public static String exp(String x, String y) {
		double nya = Double.parseDouble(x);
		double meow = Double.parseDouble(y);
		return(Double.toString(Math.pow(nya, meow)));
	}

	// funny enough, despite changing it to Radians, this actually produces the degrees answer. Remove
	// toRadians if you want radians XD----default is degrees for now, may later add in radian
	// functionality
	public static String sine(String x) {
		double nya = Double.parseDouble(x);
		return(Double.toString(Math.sin(Math.toRadians(nya%360))));
	}

	public static String cosine(String x) {
		double nya = Double.parseDouble(x);
		return(Double.toString(Math.cos(Math.toRadians(nya%360))));
	}

	public static String tangent(String x) {
		double nya = Double.parseDouble(x);
		return(Double.toString(Math.tan(Math.toRadians(nya%360))));
	}

	public static ArrayList<String> StringToArrayList(String a) {
		ArrayList<String> nya = new ArrayList<String>();
		String meep = a;
		boolean prevIsOperator = true;
		while(meep.isEmpty()!=true) {
			if(String.valueOf(meep.charAt(0)).matches("[0-9.]")) {
				// numbers
				int neko = 0;
				boolean meow = true;
				while(meow==true) {
					neko++;
					if(neko<meep.length()) {
						if(!String.valueOf(meep.charAt(neko)).matches("[0-9.]")) {
							meow = false;
						}
					}
					else {
						meow = false;
					}
					prevIsOperator = false;
				}
				nya.add(meep.substring(0, neko));
				meep = meep.substring(neko);
			}
			else if(String.valueOf(meep.charAt(0)).matches("[sct]")) {
				// sin cos and tan
				nya.add("1");
				nya.add(meep.substring(0, 3));
				meep = meep.substring(3);
				prevIsOperator = false;
			}
			else {
				// +-*/^
				if(prevIsOperator==false) {
					nya.add(meep.substring(0, 1));
					meep = meep.substring(1);
					prevIsOperator = true;
				}
				else {
					if(String.valueOf(meep.charAt(1)).matches("[0-9.]")) {
						// numbers
						int neko = 1;
						boolean meow = true;
						while(meow==true) {
							neko++;
							if(neko<meep.length()) {
								if(!String.valueOf(meep.charAt(neko)).matches("[0-9.]")) {
									meow = false;
								}
							}
							else {
								meow = false;
							}
							prevIsOperator = false;
						}
						nya.add(meep.substring(0, neko));
						meep = meep.substring(neko);
					}
					prevIsOperator = false;
				}
			}
		}
		return nya;
	}

	public static Node ArrayListToTree(ArrayList<String> a, Node b) {
		Node neko = b;
		while(a.isEmpty()==false) {
			String meep = a.remove(0);
			Node nya = new Node();
			if(meep.matches("[+-]")) {
				while(neko.getParent()!=null) {
					neko = neko.getParent();
				}
				nya.setValue(meep);
				nya.setLeft(neko);
				neko.setParent(nya);
				nya.setRight(new Node(a.remove(0)));
				nya.getRight().setParent(nya);
				ArrayListToTree(a, nya.getRight());
			}
			else if(meep.matches("[*/]")) {
				while(true) {
					if(neko.getParent()==null) {
						break;
					}
					if(!neko.getParent().getValue().matches("[*/sct\\^].*")) {
						break;
					}
					else {
						neko = neko.getParent();
					}
				}
				nya.setValue(meep);
				nya.setLeft(neko);
				if(neko.getParent()!=null) {
					neko.getParent().setRight(nya);
					nya.setParent(neko.getParent());
				}
				neko.setParent(nya);
				nya.setRight(new Node(a.remove(0)));
				nya.getRight().setParent(nya);
				ArrayListToTree(a, nya.getRight());
			}
			else if(meep.matches("[\\^]")) {
				while(true) {
					if(neko.getParent()==null) {
						break;
					}
					if(!neko.getParent().getValue().matches("[sct].*")) {
						break;
					}
					else {
						neko = neko.getParent();
					}
				}
				nya.setValue(meep);
				nya.setLeft(neko);
				if(neko.getParent()!=null) {
					neko.getParent().setRight(nya);
					nya.setParent(neko.getParent());
				}
				neko.setParent(nya);
				nya.setRight(new Node(a.remove(0)));
				nya.getRight().setParent(nya);
				ArrayListToTree(a, nya.getRight());
			}
			else if(meep.matches("[sct].*")) {
				nya.setValue(meep);
				nya.setLeft(neko);
				if(neko.getParent()!=null) {
					neko.getParent().setRight(nya);
					nya.setParent(neko.getParent());
				}
				neko.setParent(nya);
				nya.setRight(new Node(a.remove(0)));
				nya.getRight().setParent(nya);
				ArrayListToTree(a, nya.getRight());
			}
		}
		while(neko.getParent()!=null) {
			neko = neko.getParent();
		}
		return neko;
	}

	public static String solv(Node a) throws CalcExceptions {
		if(a.getLeft().getValue().matches("[+/*\\^sct].*") || a.getLeft().getValue().equals("-")) {
			a.getLeft().setValue(solv(a.getLeft()));
		}
		if(a.getRight().getValue().matches("[+/*\\^sct].*") || a.getRight().getValue().equals("-")) {
			a.getRight().setValue(solv(a.getRight()));
		}
		if(a.getValue().matches("[+]")) {
			return add(a.getLeft().getValue(), a.getRight().getValue());
		}
		else if(a.getValue().matches("[-]")) {
			return sub(a.getLeft().getValue(), a.getRight().getValue());
		}
		else if(a.getValue().matches("[*]")) {
			return mult(a.getLeft().getValue(), a.getRight().getValue());
		}
		else if(a.getValue().matches("[/]")) {
			try {
				return divi(a.getLeft().getValue(), a.getRight().getValue());
			}
			catch(CalcExceptions e) {
				throw new CalcExceptions("Error : Cannot Divide by Zero");
			}
		}
		else if(a.getValue().matches("[\\^]")) {
			return exp(a.getLeft().getValue(), a.getRight().getValue());
		}
		else if(a.getValue().equals("sin")) {
			return sine(a.getRight().getValue());
		}
		else if(a.getValue().matches("cos")) {
			return cosine(a.getRight().getValue());
		}
		else if(a.getValue().matches("tan")) {
			return tangent(a.getRight().getValue());
		}
		throw new CalcExceptions("Error : Syntax");
	}

	public static String fixPar(String a) throws CalcExceptions {
		int opening = 0;
		int closing = 0;
		for(int i = 0; i<a.length(); i++) {
			if(String.valueOf(a.charAt(i)).equals("(")) {
				opening++;
				if(i>0) {
					if(!String.valueOf(a.charAt(i-1)).matches("[+-/*ns(\\^].*")) {
						a = a.substring(0, i) + "*" + a.substring(i);
						i++;
					}
				}
			}
			else if(String.valueOf(a.charAt(i)).equals(")")) {
				closing++;
				if(i<a.length()) {
					if(!String.valueOf(a.charAt(i+1)).matches("[+-/*sct(\\^].*")) {
						a = a.substring(0, i+1) + "*" + a.substring(i+1);
						i++;
					}
				}
			}
			if(i>0) {
				if(String.valueOf(a.charAt(i)).matches("[sct].*")) {
					if(String.valueOf(a.charAt(i-1)).matches("[0-9]")) {
						a = a.substring(0, i) + "*" + a.substring(i);
						i++;
					}
				}
			}
		}
		if(opening>closing) {
			for(int j = 0; j<opening-closing; j++) {
				a = a.concat(")");
			}
		}
		else if(closing>opening) {
			throw new CalcExceptions("Error : Syntax");
		}
		a = a.concat(")");
		return a;
	}

	public static String calc(String a) throws CalcExceptions {
		String meep = a;
		String meep2 = "";
		int index = -1;
		boolean par = true;
		// parenthesis recursion
		while(par==true) {
			index++;
			if(String.valueOf(meep.charAt(index)).equals("(")) {
				meep = meep.substring(0, index)+calc(meep.substring(index+1));
				index=-1;
			}
			else if(String.valueOf(meep.charAt(index)).equals(")")) {
				if(index+1<=meep.length()-1) {
					meep2 = meep.substring(index+1);
				}
				meep = meep.substring(0, index);
				par = false;
			}
		}
		// solving the rest by putting in an ArrayList then solving using a tree
		ArrayList<String> nya = StringToArrayList(meep);
		if(nya.size()==1) {
			meep = nya.get(0);
		}
		else if(nya.size()>=3) {
			Node meow = new Node();
			meow.setLeft(new Node(nya.remove(0)));
			meow.getLeft().setParent(meow);
			meow.setValue(nya.remove(0));
			meow.setRight(new Node(nya.remove(0)));
			meow.getRight().setParent(meow);
			meow = ArrayListToTree(nya, meow.getRight());
			try {
				meep = solv(meow);
			}
			catch(CalcExceptions e) {
				throw new CalcExceptions(e.getMessage());
			}
		}
		else {
			throw new CalcExceptions("Error : Syntax");
		}
		return meep+meep2;
	}
}
