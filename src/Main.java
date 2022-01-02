import java.util.*;

import processing.core.PApplet;

public class Main extends PApplet {
	private int calcState = 0;
	private int keyState = 0;
	private int textBoxState = 0;
	private double scaleX = 1;
	private double scaleY = 1;
	private String str = "";
	private String showStr = "";
	private ArrayList<String> graphStuff = new ArrayList<String>();
	private ArrayList<String> history = new ArrayList<String>();
	private String error = "";

	public void settings() {
		size(1000, 1000);
	}

	public void setup() {
		frameRate(60);
		rectMode(CENTER);
		textAlign(CENTER);
		for(int i = 0; i<14; i++) {
			graphStuff.add("");
		}
	}

	public void draw() {
		background(255, 183, 179);
		fill(179, 255, 249);
		rect((width/2), (height/2), (width/2), (height*4/5), 100);
		fill(218, 255, 179);
		rect((width/2), (height/2-height/5), (width*4/10), (height*3/10), 50);
		// buttons: 0-9 numbers, operations, equals, graph, clear, window??? : total 22-23
		Stack<String> buttons = buttonStack();
		textSize(height/50);
		for(int i = 0; i<5; i++) {
			for(int j = 0; j<5; j++) {
				fill(243, 243, 243);
				rect(width/3+i*width/12, height/2+j*height/12, height/15, height/15, 20);
				fill(0, 0, 0);
				text(buttons.pop(), width/3+i*width/12, height/2+j*height/12);
			}
		}
		if(calcState==0) {// default enter to calculate
			// fill();
			textSize(height/50);
			text(showStr, width/2, height*2/5);
			// display the last 2 history
			for(int i = 0; i<history.size(); i++) {
				text(history.get(i), width/2, height/5+i*height/20);
				text(history.get(i+1), width/2, height/5+height/25+i*height/20);
				i++;
			}
		}
		else if(calcState==1) {// error state
			textSize(height/35);
			text(error, width/2, height*1/5);
		}
		else if(calcState==2) {// y= button enter in graph equations
			fill(171, 248, 89);
			// linear
			rect(width*3/7, height*9/40, width/10, height/20, 20);
			rect(width*4/7, height*9/40, width/10, height/20, 20);
			// quadratic
			rect(width*23/56, height*13/40, width/15, height/20, 10);
			rect(width*29/56, height*13/40, width/15, height/20, 10);
			rect(width*35/56, height*13/40, width/15, height/20, 10);
			// scale
			rect(width*13/28, height*17/40, width/15, height/20, 10);
			rect(width*9/14, height*17/40, width/15, height/20, 10);
			// text
			fill(0, 0, 0);
			text("Linear Equation", width/2, height*15/80);
			text("X +", width/2, height*19/80);
			text("Y =", width*17/48, height*19/80);
			text("Quadratic Equation", width/2, height*23/80);
			text("a=", width*10/28, height*27/80);
			text("b=", width*13/28, height*27/80);
			text("c=", width*16/28, height*27/80);
			text("Scale X=", width*21/56, height*17/40);
			text("Scale Y=", width*31/56, height*17/40);
			text(graphStuff.get(1), width*3/7, height*9/40);
			text(graphStuff.get(3), width*4/7, height*9/40);
			text(graphStuff.get(5), width*23/56, height*13/40);
			text(graphStuff.get(7), width*29/56, height*13/40);
			text(graphStuff.get(9), width*35/56, height*13/40);
			text(graphStuff.get(11), width*13/28, height*17/40);
			text(graphStuff.get(13), width*9/14, height*17/40);
			// Yeah...too lazy for that, and that would mean more textboxes, and 7 is good enough for me :3
			text("Trigonomic Equations...How about NO", width/2, height*31/80);
		}
		else if(calcState==3) {// graph state---graphs the functions from y= if possible(if the equations are possible)
			stroke(129, 132, 125);
			for(int i = 0; i<5; i++) {
				// x lines
				line(width/2+i*width*3/100, height*7/40, width/2+i*width*3/100, height*17/40);
				line(width/2-i*width*3/100, height*7/40, width/2-i*width*3/100, height*17/40);
			}
			for(int i = 0; i<5; i++) {
				// y lines
				line(width/2+4*width*3/100, height*3/10+i*height*3/100, width/2-4*width*3/100, height*3/10+i*height*3/100);
				line(width/2+4*width*3/100, height*3/10-i*height*3/100, width/2-4*width*3/100, height*3/10-i*height*3/100);
			}
			stroke(0, 0, 0);
			strokeWeight(2);
			line(width/2, height*7/40, width/2, height*17/40);
			line(width/2+4*width*3/100, height*3/10, width/2-4*width*3/100, height*3/10);
			strokeWeight(1);
			text("Box Scale X: " + scaleX, width*2/5, height*18/40);
			text("Box Scale Y: " + scaleY, width*3/5, height*18/40);
			if(graphStuff.get(10).isEmpty()) {
				scaleX = 1;
			}
			else {
				try {
					String meep = Calculate.fixPar(graphStuff.get(10));
					meep = Calculate.calc(meep);
					scaleX = Integer.parseInt(meep);
				}
				catch(Exception e) {
					scaleX = 1;
				}
			}
			if(graphStuff.get(12).isEmpty()) {
				scaleY = 1;
			}
			else {
				try {
					String meep = Calculate.fixPar(graphStuff.get(12));
					meep = Calculate.calc(meep);
					scaleY = Integer.parseInt(meep);
				}
				catch(Exception e) {
					scaleY = 1;
				}
			}
			// graph the linear equation if possible
			try {
				double m = 0;
				double b = 0;
				double x1 = 0;
				double x2 = 0;
				double y1 = 0;
				double y2 = 0;
				String meep = Calculate.fixPar(graphStuff.get(0));
				meep = Calculate.calc(meep);
				String beep = Calculate.fixPar(graphStuff.get(2));
				beep = Calculate.calc(beep);
				m = Double.parseDouble(meep)/scaleY;
				b = Double.parseDouble(beep);
				if(m*scaleX*4+b>scaleY*4) {
					x1 = ((scaleY*4)-b)/m;
					y1 = scaleY*4;
				}
				else if(m*scaleX*4+b<-scaleY*4) {
					x1 = ((scaleY*-4)-b)/m;
					y1 = scaleY*-4;
				}
				else {
					x1 = scaleX*4;
					y1 = m*(x1)+b;
				}
				if(m*scaleX*-4+b>scaleY*4) {
					x2 = ((scaleY*4)-b)/m;
					y2 = scaleY*4;
				}
				else if(m*scaleX*-4+b<-scaleY*4) {
					x2 = ((scaleY*-4)-b)/m;
					y2 = scaleY*-4;
				}
				else {
					x2 = scaleX*-4;
					y2 = m*(x2)+b;
				}
				stroke(231, 17, 0);
				strokeWeight(2);
				line((float)(width/2+(x1/scaleX)*width*3/100), (float)(height*3/10-(y1/scaleY)*height*3/100),
				        (float)(width/2+(x2/scaleX)*width*3/100), (float)(height*3/10-(y2/scaleY)*height*3/100));
				stroke(0, 0, 0);
				strokeWeight(1);
			}
			catch(Exception e) {
				// well...hi
			}
			// try the quadratic Equation if possible
			try {
				String aa = Calculate.fixPar(graphStuff.get(4));
				aa = Calculate.calc(aa);
				String bb = Calculate.fixPar(graphStuff.get(6));
				bb = Calculate.calc(bb);
				String cc = Calculate.fixPar(graphStuff.get(8));
				cc = Calculate.calc(cc);
				double a = Double.parseDouble(aa);
				double b = Double.parseDouble(bb);
				double c = Double.parseDouble(cc);
				if(Math.pow(b, 2)<4*a*c) {
					throw new CalcExceptions();
				}
				double x = 0.01;
				double x2= -0.01;
				double prevX = 0;
				double prevY = c;
				double prevX2 = 0;
				double prevY2 = c;
				stroke(21, 80, 242);
				strokeWeight(2);
				while(x<=scaleX*4 && a*Math.pow(x, 2)+b*x+c<=scaleY*4 && a*Math.pow(x, 2)+b*x+c>=scaleY*-4) {
					double thisX = x;
					double thisY = a*Math.pow(x, 2)+b*x+c;
					line((float)(width/2+(prevX/scaleX)*width*3/100), (float)(height*3/10-(prevY/scaleY)*height*3/100),
					        (float)(width/2+(thisX/scaleX)*width*3/100), (float)(height*3/10-(thisY/scaleY)*height*3/100));
					prevX=thisX;
					prevY=thisY;
					x += 0.01;
				}
				while(x2>=scaleX*-4 && a*Math.pow(x2, 2)+b*x2+c<=scaleY*4 && a*Math.pow(x2, 2)+b*x2+c>=scaleY*-4) {
					double thisX=x2;
					double thisY= a*Math.pow(x2, 2)+b*x2+c;
					line((float)(width/2+(prevX2/scaleX)*width*3/100), (float)(height*3/10-(prevY2/scaleY)*height*3/100),
					        (float)(width/2+(thisX/scaleX)*width*3/100), (float)(height*3/10-(thisY/scaleY)*height*3/100));
					prevX2=thisX;
					prevY2=thisY;
					x2 -= 0.01;
				}
				stroke(0, 0, 0);
				strokeWeight(1);
			}
			catch(Exception e) {
				// hi again :)
			}
		}
	}

	public void mousePressed() {
		boolean used = false;
		if(keyState==0) {
			Stack<String> buttons = buttonStack();
			for(int i = 0; i<5; i++) {
				for(int j = 0; j<5; j++) {
					if(mouseX<width/3+i*width/12+height/30 && mouseX>width/3+i*width/12-height/30 && mouseY<height/2+j*height/12+height/30
					        && mouseY>height/2+j*height/12-height/30) {
						if(!buttons.peek().matches("[sctdgeyxÅÄ].*")) {
							str = str.concat(buttons.peek());
							showStr = showStr.concat(buttons.pop());
							used = true;
						}
						else {
							if(buttons.peek().matches("[sct].*")) {
								str = str + buttons.peek() + "(";
								showStr = showStr + buttons.pop() + "(";
								used = true;
							}
							else if(buttons.peek().equals("x")) {
								str = str.concat("*");
								showStr = showStr.concat(buttons.pop());
								used = true;
							}
							else if(buttons.peek().equals("ÅÄ")) {
								str = str.concat("/");
								showStr = showStr.concat(buttons.pop());
								used = true;
							}
							else if(buttons.peek().equals("enter")) {
								history.add(showStr);
								used = true;
								try {
									String meep = Calculate.fixPar(str);
									meep = Calculate.calc(meep);
									history.add("=" + meep);
								}
								catch(CalcExceptions e) {
									if(e.getMessage()==null) {
										history.add("Error : Syntax");
										error = "Error : Syntax";
										calcState = 1;
										keyState = 1;
									}
									else {
										history.add(e.getMessage());
										error = e.getMessage();
									}
									// error state
									calcState = 1;
									keyState = 1;
								}
								catch(Exception e) {
									history.add("Error : Syntax");
									error = "Error : Syntax";
									calcState = 1;
									keyState = 1;
								}
								str = "";
								showStr = "";
								while(history.size()>4) {
									history.remove(0);
								}
							}
							else if(buttons.peek().equals("del")) {
								if(str.length()!=0) {
									str = str.substring(0, str.length()-1);
									showStr = showStr.substring(0, showStr.length()-1);
									used = true;
								}
							}
							else if(buttons.peek().equals("y=")) {
								calcState = 2;
								keyState = 2;
								used = true;
							}
							// graph
							else {
								calcState = 3;
								keyState = 1;
								used = true;
							}
						}
					}
					if(!buttons.empty()) {
						buttons.pop();
					}
				}
			}
		}
		if(keyState==1 && used==false) {
			if(mouseX<width/3+4*width/12+height/30 && mouseX>width/3+4*width/12-height/30 && mouseY<height/2+4*height/12+height/30
			        && mouseY>height/2+4*height/12-height/30) {
				calcState = 0;
				keyState = 0;
				used = true;
			}
			else if(mouseX<width/3+height/30 && mouseX>width/3-height/30 && mouseY<height/2+3*height/12+height/30
			        && mouseY>height/2+3*height/12-height/30) {
				calcState = 2;
				keyState = 2;
				used = true;
			}
		}
		if(keyState==2 && used==false) {
			// if click in box areas, then become that textBoxState
			if(mouseX>width*3/7-width/20 && mouseX<width*3/7+width/20 && mouseY>height*9/40-height/20 && mouseY<height*9/40+height/20) {
				textBoxState = 1;
				used = true;
			}
			else if(mouseX>width*4/7-width/20 && mouseX<width*4/7+width/20 && mouseY>height*9/40-height/20
			        && mouseY<height*9/40+height/20) {
				textBoxState = 3;
				used = true;
			}
			else if(mouseX>width*23/56-width/30 && mouseX<width*23/56+width/30 && mouseY>height*13/40-height/20
			        && mouseY<height*13/40+height/20) {
				textBoxState = 5;
				used = true;
			}
			else if(mouseX>width*29/56-width/30 && mouseX<width*29/56+width/30 && mouseY>height*13/40-height/20
			        && mouseY<height*13/40+height/20) {
				textBoxState = 7;
				used = true;
			}
			else if(mouseX>width*35/56-width/30 && mouseX<width*35/56+width/30 && mouseY>height*13/40-height/20
			        && mouseY<height*13/40+height/20) {
				textBoxState = 9;
				used = true;
			}
			else if(mouseX>width*13/28-width/30 && mouseX<width*13/28+width/30 && mouseY>height*17/40-height/20
			        && mouseY<height*17/40+height/20) {
				textBoxState = 11;
				used = true;
			}
			else if(mouseX>width*9/14-width/30 && mouseX<width*9/14+width/30 && mouseY>height*17/40-height/20
			        && mouseY<height*17/40+height/20) {
				textBoxState = 13;
				used = true;
			}
			else if(textBoxState>0 && used==false) {
				Stack<String> buttons = buttonStack();
				for(int i = 0; i<5; i++) {
					for(int j = 0; j<5; j++) {
						if(mouseX<width/3+i*width/12+height/30 && mouseX>width/3+i*width/12-height/30
						        && mouseY<height/2+j*height/12+height/30 && mouseY>height/2+j*height/12-height/30) {
							if(!buttons.peek().matches("[sctdgeyxÅÄ].*")) {
								graphStuff.set(textBoxState-1, graphStuff.get(textBoxState-1).concat(buttons.peek()));
								graphStuff.set(textBoxState, graphStuff.get(textBoxState).concat(buttons.pop()));
								used = true;
							}
							else {
								if(buttons.peek().matches("[sct].*")) {
									graphStuff.set(textBoxState-1, graphStuff.get(textBoxState-1) + buttons.peek() + "(");
									graphStuff.set(textBoxState, graphStuff.get(textBoxState) + buttons.pop() + "(");
									used = true;
								}
								else if(buttons.peek().equals("x")) {
									graphStuff.set(textBoxState-1, graphStuff.get(textBoxState-1).concat("*"));
									graphStuff.set(textBoxState, graphStuff.get(textBoxState).concat(buttons.pop()));
									used = true;
								}
								else if(buttons.peek().equals("ÅÄ")) {
									graphStuff.set(textBoxState-1, graphStuff.get(textBoxState-1).concat("/"));
									graphStuff.set(textBoxState, graphStuff.get(textBoxState).concat(buttons.pop()));
									used = true;
								}
								else if(buttons.peek().equals("del")) {
									if(graphStuff.get(textBoxState-1).length()!=0) {
										graphStuff.set(textBoxState-1,
										        graphStuff.get(textBoxState-1).substring(0, graphStuff.get(textBoxState-1).length()-1));
										graphStuff.set(textBoxState,
										        graphStuff.get(textBoxState).substring(0, graphStuff.get(textBoxState).length()-1));
										used = true;
									}
								}
							}
						}
						if(!buttons.empty()) {
							buttons.pop();
						}
					}
				}
				// to get out of textBoxStates
				if(used==false) {
					textBoxState = 0;
					used = true;
				}
			}
			// graph---graphs the functions enter---goes back to normal mode
			if(mouseX<width/3+height/30 && mouseX>width/3-height/30 && mouseY<height/2+4*height/12+height/30
			        && mouseY>height/2+4*height/12-height/30) {
				calcState = 3;
				keyState = 1;
			}
			else if(mouseX<width/3+4*width/12+height/30 && mouseX>width/3+4*width/12-height/30 && mouseY<height/2+4*height/12+height/30
			        && mouseY>height/2+4*height/12-height/30) {
				calcState = 0;
				keyState = 0;
				used = true;
			}
		}
	}

	public void keyPressed() {
		boolean used = false;
		if(keyState==0) {
			if(key=='1' || key=='2' || key=='3' || key=='4' || key=='5' || key=='6' || key=='7' || key=='8' || key=='9' || key=='0'
			        || key=='+' || key=='-' || key=='(' || key==')' || key=='^') {
				str = str+key;
				showStr = showStr+key;
				used = true;
			}
			else if(key==ENTER || key=='=') {
				history.add(showStr);
				used = true;
				try {
					String meep = Calculate.fixPar(str);
					meep = Calculate.calc(meep);
					history.add("=" + meep);
				}
				catch(CalcExceptions e) {
					if(e.getMessage()==null) {
						history.add("Error : Syntax");
						error = "Error : Syntax";
						calcState = 1;
						keyState = 1;
					}
					else {
						history.add(e.getMessage());
						error = e.getMessage();
					}
					// error state
					calcState = 1;
					keyState = 1;
				}
				catch(Exception e) {
					history.add("Error : Syntax");
					error = "Error : Syntax";
					calcState = 1;
					keyState = 1;
				}
				str = "";
				showStr = "";
				while(history.size()>4) {
					history.remove(0);
				}
			}
			else if(key==BACKSPACE || key==DELETE) {
				if(str.length()!=0) {
					str = str.substring(0, str.length()-1);
					showStr = showStr.substring(0, showStr.length()-1);
					used = true;
				}
			}
			else if(key=='y' || key=='Y') {
				calcState = 2;
				keyState = 2;
				used = true;
			}
			else if(key=='*') {
				str = str.concat("*");
				showStr = showStr.concat("x");
				used = true;
			}
			else if(key=='/') {
				str = str.concat("/");
				showStr = showStr.concat("ÅÄ");
				used = true;
			}
			else if(key=='g' || key=='G') {
				calcState = 3;
				keyState = 1;
				used = true;
			}
			else if(key=='s' || key=='S') {
				str = str.concat("sin(");
				showStr = showStr.concat("sin(");
				used = true;
			}
			else if(key=='c' || key=='C') {
				str = str.concat("cos(");
				showStr = showStr.concat("cos(");
				used = true;
			}
			else if(key=='t' || key=='T') {
				str = str.concat("tan(");
				showStr = showStr.concat("tan(");
				used = true;
			}
		}
		else if(keyState==1 && used==false) {
			if(key==ENTER || key=='=') {
				calcState = 0;
				keyState = 0;
			}
		}
		else if(keyState==2 && used==false) {
			if(textBoxState>0) {
				if(key=='1' || key=='2' || key=='3' || key=='4' || key=='5' || key=='6' || key=='7' || key=='8' || key=='9' || key=='0'
				        || key=='+' || key=='-' || key=='(' || key==')' || key=='^') {
					graphStuff.set(textBoxState-1, graphStuff.get(textBoxState-1)+key);
					graphStuff.set(textBoxState, graphStuff.get(textBoxState)+key);
					used = true;
				}
				else if(key==BACKSPACE || key==DELETE) {
					if(graphStuff.get(textBoxState).length()!=0) {
						graphStuff.set(textBoxState-1,
						        graphStuff.get(textBoxState-1).substring(0, graphStuff.get(textBoxState-1).length()-1));
						graphStuff.set(textBoxState, graphStuff.get(textBoxState).substring(0, graphStuff.get(textBoxState).length()-1));
						used = true;
					}
				}
				else if(key=='*') {
					graphStuff.set(textBoxState-1, graphStuff.get(textBoxState-1).concat("*"));
					graphStuff.set(textBoxState, graphStuff.get(textBoxState).concat("x"));
					used = true;
				}
				else if(key=='/') {
					graphStuff.set(textBoxState-1, graphStuff.get(textBoxState-1).concat("/"));
					graphStuff.set(textBoxState, graphStuff.get(textBoxState).concat("ÅÄ"));
					used = true;
				}
				else if(key=='s' || key=='S') {
					graphStuff.set(textBoxState-1, graphStuff.get(textBoxState-1).concat("sin("));
					graphStuff.set(textBoxState, graphStuff.get(textBoxState).concat("sin("));
					used = true;
				}
				else if(key=='c' || key=='C') {
					graphStuff.set(textBoxState-1, graphStuff.get(textBoxState-1).concat("cos("));
					graphStuff.set(textBoxState, graphStuff.get(textBoxState).concat("cos("));
					used = true;
				}
				else if(key=='t' || key=='T') {
					graphStuff.set(textBoxState-1, graphStuff.get(textBoxState-1).concat("tan("));
					graphStuff.set(textBoxState, graphStuff.get(textBoxState).concat("tan("));
					used = true;
				}
			}
			// enter-goes back to normal state graph-graphs the functions
			if(key=='=' || key==ENTER) {
				calcState = 0;
				keyState = 0;
			}
			else if(key=='g' || key=='G') {
				calcState = 3;
				keyState = 1;
			}
		}
	}

	public static void main(String[] args) {
		String[] processingArgs = {"Main"};
		Main mySketch = new Main();
		PApplet.runSketch(processingArgs, mySketch);
	}

	// just an abstraction for buttons
	public Stack<String> buttonStack() {
		Stack<String> nya = new Stack<String>();
		nya.add("enter");
		nya.add("+");
		nya.add("-");
		nya.add("x");
		nya.add("ÅÄ");
		nya.add("del");
		nya.add("tan");
		nya.add("cos");
		nya.add("sin");
		nya.add("^");
		nya.add("(");
		nya.add(")");
		nya.add("3");
		nya.add("6");
		nya.add("9");
		nya.add(".");
		nya.add("0");
		nya.add("2");
		nya.add("5");
		nya.add("8");
		nya.add("graph");
		nya.add("y=");
		nya.add("1");
		nya.add("4");
		nya.add("7");
		return nya;
	}
}