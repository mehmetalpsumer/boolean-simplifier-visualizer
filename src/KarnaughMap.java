import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;
import java.util.ArrayList;

import javax.swing.GroupLayout.Alignment;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;


public class KarnaughMap {
	
	private ArrayList<String> inputs;
	private Shell[] shells;
	private int[][] truthtable;
	private String finalform;
	private ArrayList<ArrayList<Shell>> forcolor = new ArrayList<>();
 	public String getFinalform() {
		return finalform;
	}
	public void setFinalform(String finalform) {
		this.finalform = finalform;
	}
	public KarnaughMap(TruthTable tt, int size, ArrayList q)
	{
		inputs = q;
		truthtable = new int[(int) Math.pow(2,size)][size+1];
		for(int i=0; i<Math.pow(2,size);i++){
			for (int j=0; j<(size+1); j++){
				truthtable[i][j] = Integer.parseInt(tt.getRows()[i].getCol()[j]);
			}
		}
		create_shells(size);
		for (int i = 0; i < shells.length; i++) {
			for (int j = 0; j < shells.length; j++) {
				if (i != j)
					set_neighbours(shells[i], shells[j], size);
			}
		}

		int count = 0;
		for (int i = 0; i < shells.length; i++) {
			if (shells[i].getTruth_value() == 1)
			{
				count++;

			}
		}
		finalform = simplify(count, size);
		if (!finalform.equals("1")) {
			if (finalform.length() > 0) {
				finalform = finalform.substring(0, finalform.length() - 3);
			}
		}
			
	}
	public int count_simplified(Shell[] sh)
	{
		int count = 0;
		for (int i = 0; i < sh.length; i++) {
			if (sh[i].isSimplified())
				count++;
		}
		return count;
	}
	public void create_shells(int size)
	{
		shells = new Shell[(int) Math.pow(2, size)];
		for (int i = 0; i < shells.length; i++) {
			shells[i] = new Shell(size);
			String add = "";
			for (int j2 = 0; j2 < truthtable[i].length - 1; j2++) {
				add = add.concat(Integer.toString(truthtable[i][j2]));

			}
			shells[i].setAdress(add);
			shells[i].setTruth_value(truthtable[i][truthtable[i].length - 1]);
		}
//		for (int i = 0; i < shells.length; i++) {
//			System.out.println(shells[i].getAdress() + " " + shells[i].getTruth_value());
//		}
	}
	public String simplify(int count, int size)
	{
		String s = "";
		if (count == Math.pow(2, size))
			return "1";
		else
		{
			Shell[] forsimplify = new Shell[count];
			for (int i = 0; i < shells.length; i++) {
				if (shells[i].getTruth_value() == 1)
				{
					for (int j = 0; j < forsimplify.length; j++) {
						if (forsimplify[j] == null)
						{
							forsimplify[j] = shells[i];
							break;
						}
					}
				}
			}
			ArrayList<Shell[]> fours = new ArrayList<>();
			for (int i = 0; i < forsimplify.length; i++) {
				for (int j = 0; j < forsimplify.length; j++) {
					for (int j2 = 0; j2 < forsimplify.length; j2++) {
						for (int k = 0; k < forsimplify.length; k++) {
							
							if (equal(new int[]{i, j, j2, k}))
							{
								Shell[] sq = new Shell[4];
								sq[0] = forsimplify[i];
								sq[1] = forsimplify[j];
								sq[2] = forsimplify[j2];
								sq[3] = forsimplify[k];
								if (square(sq))
								{
									fours.add(sq);
								}
							}
						}
					}
				}
			}
			for (int i = 0; i < fours.size(); i++) {
				for (int j = 0; j < fours.size(); j++) {
					if (i != j)
					{
						if (!all_simplified(fours.get(i)) || !all_simplified(fours.get(j)))
						{
							if (check_eight(fours.get(i), fours.get(j)))
							{
								ArrayList<Shell> forexp = new ArrayList<>();
								for (int j2 = 0; j2 < fours.get(i).length; j2++) {
									forexp.add(fours.get(i)[j2]);
									forexp.add(fours.get(j)[j2]);
								}
								forcolor.add(forexp);
								s = s + expression(forexp);
								for (int k = 0; k < forexp.size(); k++) {
									forexp.get(k).setSimplified(true);
								}
								if (count_simplified(forsimplify) == count)
									return s;
							}
						}
					}
				}
			}

			for (int i = 0; i < fours.size(); i++) {
				if (count_simplified(fours.get(i)) == 0)
				{
					ArrayList<Shell> a = new ArrayList<>();
					for (int j = 0; j < fours.get(i).length; j++) {
						a.add(fours.get(i)[j]);
					}
					forcolor.add(a);
					s = s + expression(a);
					for (int j = 0; j < fours.get(i).length; j++) {
						fours.get(i)[j].setSimplified(true);
					}
					if (count_simplified(forsimplify) == count)
						return s;
				}
			}
			for (int i = 0; i < fours.size(); i++) {
				if (count_simplified(fours.get(i)) == 1)
				{
					ArrayList<Shell> a = new ArrayList<>();
					for (int j = 0; j < fours.get(i).length; j++) {
						a.add(fours.get(i)[j]);
					}
					forcolor.add(a);
					s = s + expression(a);
					for (int j = 0; j < fours.get(i).length; j++) {
						fours.get(i)[j].setSimplified(true);
					}
					if (count_simplified(forsimplify) == count)
						return s;
				}
			}
			for (int i = 0; i < fours.size(); i++) {
				if (count_simplified(fours.get(i)) == 2)
				{
					ArrayList<Shell> a = new ArrayList<>();
					for (int j = 0; j < fours.get(i).length; j++) {
						a.add(fours.get(i)[j]);
					}
					forcolor.add(a);
					s = s + expression(a);
					for (int j = 0; j < fours.get(i).length; j++) {
						fours.get(i)[j].setSimplified(true);
					}
					if (count_simplified(forsimplify) == count)
						return s;
				}
			}
			for (int i = 0; i < fours.size(); i++) {
				if (count_simplified(fours.get(i)) == 3)
				{
					ArrayList<Shell> a = new ArrayList<>();
					for (int j = 0; j < fours.get(i).length; j++) {
						a.add(fours.get(i)[j]);
					}
					forcolor.add(a);
					s = s + expression(a);
					for (int j = 0; j < fours.get(i).length; j++) {
						fours.get(i)[j].setSimplified(true);
					}
					if (count_simplified(forsimplify) == count)
						return s;
				}
			}
			ArrayList<Shell[]> twos = new ArrayList<>();
			for (int i = 0; i < forsimplify.length; i++) {
				for (int j = 0; j < forsimplify.length; j++) {
					if (i != j)
					{
						if (isNeighbour(forsimplify[i], forsimplify[j]))
						{
							twos.add(new Shell[] {forsimplify[i], forsimplify[j]});
						}
					}
				}
			}
			for (int i = 0; i < twos.size(); i++) {
				if (count_simplified(twos.get(i)) == 0)
				{
					ArrayList<Shell> a = new ArrayList<>();
					for (int j = 0; j < twos.get(i).length; j++) {
						a.add(twos.get(i)[j]);
					}
					forcolor.add(a);
					s = s + expression(a);
					for (int j = 0; j < twos.get(i).length; j++) {
						twos.get(i)[j].setSimplified(true);
					}
					if (count_simplified(forsimplify) == count)
						return s;
				}
			}
			for (int i = 0; i < twos.size(); i++) {
				if (count_simplified(twos.get(i)) == 1)
				{
					ArrayList<Shell> a = new ArrayList<>();
					for (int j = 0; j < twos.get(i).length; j++) {
						a.add(twos.get(i)[j]);
					}
					forcolor.add(a);
					s = s + expression(a);
					for (int j = 0; j < twos.get(i).length; j++) {
						twos.get(i)[j].setSimplified(true);
					}
					if (count_simplified(forsimplify) == count)
						return s;
				}
			}
			for (int i = 0; i < forsimplify.length; i++) {
				if (!forsimplify[i].isSimplified())
				{
					s = s + expression(forsimplify[i].getAdress());
					forsimplify[i].setSimplified(true);
				}
			}
			return s;
		}

	}
	public String expression(ArrayList<Shell> kar)
	{
		String addr = "";
		for (int i = 0; i < kar.get(0).getAdress().length(); i++) {
			int[] eq = new int[kar.size()];
			for (int j = 0; j < eq.length; j++) {
				eq[j] = Integer.parseInt(kar.get(j).getAdress().substring(i, i + 1));
			}
			if (all_equal(eq))
				addr += Integer.toString(eq[0]);
			else
				addr += "2";
		}
		return expression(addr);
	}
	public String expression(String adress)
	{
		String a = "";
		for (int i = 0; i < adress.length(); i++) {
			
			if (adress.charAt(i) == 48) { // char(48) = 0, char(49) = 1;
				a += inputs.get(i) + "'";
			}
			else if (adress.charAt(i) == 49) {
				a += inputs.get(i);
			}
		}
		a += " + ";
		return a;
		
		
//		if (adress.substring(0, 1).equals("0"))
//			a += "A'";
//		else if (adress.substring(0, 1).equals("1"))
//			a += "A";
//		if (adress.substring(1, 2).equals("0"))
//			a += "B'";
//		else if (adress.substring(1, 2).equals("1"))
//			a += "B";
//		if (adress.length() > 2)
//		{
//			if (adress.substring(2, 3).equals("0"))
//				a += "C'";
//			else if (adress.substring(2, 3).equals("1"))
//				a += "C";
//			if (adress.length() == 4)
//			{
//				if (adress.substring(3, 4).equals("0"))
//					a += "D'";
//				else if (adress.substring(3, 4).equals("1"))
//					a += "D";
//			}
//
//		}
//
//		a = a + " + ";
//		return a;
	}
	public boolean check_eight(Shell[] sa1, Shell[] sa2)
	{
		for (int i = 0; i < sa1.length; i++) {
			for (int j = 0; j < sa2.length; j++) {
				if (sa1[i].getAdress().equalsIgnoreCase(sa2[j].getAdress()))
					return false;
			}
		}
		ArrayList<Shell> sa = new ArrayList<>();
		for (int i = 0; i < sa2.length; i++) {
			sa.add(sa1[i]);
			sa.add(sa2[i]);
		}
		for (int i = 0; i < sa.size(); i++) {
			int neighbour = 0;
			for (int j = 0; j < sa.size(); j++) {
				if (i != j)
				{
					if (isNeighbour(sa.get(i), sa.get(j)))
						neighbour++;
				}
			}
			if (neighbour != 3)
				return false;
		}
		return true;
	}
	public boolean equal(int[] a)
	{
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a.length; j++) {
				if (i != j)
				{
					if (a[i] == a[j])
						return false;
				}

			}
		}
		return true;
	}

	public boolean all_equal(int[] a)
	{
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a.length; j++) {
				if (i != j)
				{
					if (a[i] != a[j])
						return false;
				}

			}
		}
		return true;
	}
	public boolean all_simplified(Shell[] simp)
	{
		for (int i = 0; i < simp.length; i++) {
			if (!simp[i].isSimplified())
				return false;
		}
		return true;
	}
	public boolean square(Shell[] s)
	{

		for (int i = 0; i < s.length; i++) {
			int neighbour = 0;
			for (int j = 0; j < s.length; j++) {
				if (i != j && isNeighbour(s[i], s[j]))
					neighbour++;
			}
			if (neighbour != 2)
				return false;
		}
		return true;
	}
	public boolean isNeighbour(Shell s1, Shell s2)
	{

		for (int i = 0; i < s1.getNeighbours().length; i++) {
			if (s1.getNeighbours()[i] == s2)
				return true;
		}
		return false;
	}
	public void set_neighbours(Shell shell_1, Shell shell_2, int size)
	{
		int count = 0;
		for (int i = 0; i < shell_1.getAdress().length(); i++) {
			if (!shell_1.getAdress().substring(i, i + 1).equalsIgnoreCase(shell_2.getAdress().substring(i, i + 1)))
				count++;
		}
		if (count == 1)
		{
			for (int i = 0; i < shell_1.getNeighbours().length; i++) {
				if (shell_1.getNeighbours()[i] == shell_2)
				{
					break;
				}
				if (shell_1.getNeighbours()[i] == null)
				{
					shell_1.getNeighbours()[i] = shell_2;
					break;
				}
			}
			for (int i = 0; i < shell_2.getNeighbours().length; i++) {
				if (shell_2.getNeighbours()[i] == shell_1)
				{
					break;
				}
				if (shell_2.getNeighbours()[i] == null)
				{
					shell_2.getNeighbours()[i] = shell_1;
					break;
				}
			}
			
		}
	}
	
	
	
	
	
	
	
	public void drawKMap(TruthTable tt, int size, Pane root, int x, int y){
		root.getChildren().clear();
		Text inputs = null;
		
		int startX = x;
		int startY = y;
		double scale = size/4;
		Line lineY = new Line((startX+35),(startY),(startX+35),(size==2?startY+65:startY+105));
		Line lineX = new Line((startX),(startY+20),(size==4?startX+170:startX+100),(startY+20));
		if(Main.isDarkTheme){
			lineX.setStroke(Color.ANTIQUEWHITE);
			lineY.setStroke(Color.ANTIQUEWHITE);
		}

		Text[] verticalTexts = {new Text("00"),new Text("01"),new Text("11"),new Text("10")};
		for (int i = 0; i<verticalTexts.length; i++){
			verticalTexts[i].setX(size == 3 ? lineY.getStartX()-30:lineY.getStartX()-((size/2)*15));
			verticalTexts[i].setY((lineX.getStartY()+20)+(i*20));
	 		verticalTexts[i].setStyle("-fx-font-weight: bold");
	 		verticalTexts[i].setFont(Font.font ("Verdana", 20));
	 		if(Main.isDarkTheme)
	 			verticalTexts[i].setFill(Color.ANTIQUEWHITE);

		}
		Text[] horizontalTexts = {new Text("00"),new Text("01"),new Text("11"),new Text("10")};
		for (int i = 0; i<horizontalTexts.length; i++){
			horizontalTexts[i].setY(lineX.getStartY()-5);
			horizontalTexts[i].setX((lineY.getStartX()+10)+(i*30));
			horizontalTexts[i].setStyle("-fx-font-weight: bold");
			horizontalTexts[i].setFont(Font.font ("Verdana", 20));
			if(Main.isDarkTheme)
				horizontalTexts[i].setFill(Color.ANTIQUEWHITE);
		}

		for (int i = 0; i < forcolor.size(); i++) {
			Color clr = new Color(Math.random(), Math.random(), Math.random(),1);
			for (int j = 0; j < forcolor.get(i).size(); j++) {
				forcolor.get(i).get(j).setColor(clr);
			}
		}

		switch (size){
			case 2:
				// Initial
				inputs = new Text(tt.getInputs().get(0)+"/"+tt.getInputs().get(1));
				inputs.setX(startX+7);
				inputs.setY(startY+15);
				verticalTexts[0].setText("0");
				verticalTexts[1].setText("1");
				horizontalTexts[0].setText("0");
				horizontalTexts[1].setText("1");

				
				//Values
				for (int i=0; i<Math.pow(2,size); i++){
					Text text = new Text(tt.getRows()[i].getCol()[size]);
					if (tt.getRows()[i].getCol()[size].equals("1"))
					{
						for (int j = 0; j < shells.length; j++) {
							boolean right_shell = true;
							for (int j2 = 0; j2 < shells[j].getAdress().length(); j2++) {
								if (!shells[j].getAdress().substring(j2, j2 + 1).equals(tt.getRows()[i].getCol()[j2]))
									right_shell = false;
							}
							if (right_shell)
							{
								text.setFill(shells[j].getColor()==Color.BLACK ? (Main.isDarkTheme ? Color.ANTIQUEWHITE:Color.BLACK):shells[j].getColor());
								break;
							}
						}
					}
					else{
						text.setFill(Main.isDarkTheme ? Color.ANTIQUEWHITE:Color.BLACK);
					}
					text.setFont(Font.font ("Verdana", 20));
					text.setX(horizontalTexts[bitAt(i,1,size)].getX());
					text.setY(verticalTexts[bitAt(i,0,size)].getY());
					root.getChildren().add(text);
				}
				//Add to GUI
				root.getChildren().addAll(inputs,verticalTexts[0], verticalTexts[1],horizontalTexts[0],horizontalTexts[1]);
				break;
			case 3:
				//Initial
				inputs = new Text(tt.getInputs().get(0)+tt.getInputs().get(1)+"/"+tt.getInputs().get(2));
				inputs.setX(startX-3);
				inputs.setY(startY+15);
				horizontalTexts[0].setText("0");
				horizontalTexts[1].setText("1");

				//Values
				for (int i=0; i<Math.pow(2,size); i++){
					Text text = new Text(tt.getRows()[i].getCol()[size]);
					
					if (tt.getRows()[i].getCol()[size].equals("1"))
					{
						for (int j = 0; j < shells.length; j++) {
							boolean right_shell = true;
							for (int j2 = 0; j2 < shells[j].getAdress().length(); j2++) {
								if (!shells[j].getAdress().substring(j2, j2 + 1).equals(tt.getRows()[i].getCol()[j2]))
									right_shell = false;
							}
							if (right_shell)
							{
								text.setFill(shells[j].getColor()==Color.BLACK ? (Main.isDarkTheme ? Color.ANTIQUEWHITE:Color.BLACK):shells[j].getColor());
								break;
							}
						}
					}
					else{
						text.setFill(Main.isDarkTheme ? Color.ANTIQUEWHITE:Color.BLACK);
					}
					text.setX(horizontalTexts[bitAt(i,size-1,size)].getX());
					text.setY(verticalTexts[decToGray(i,size,true)].getY());
					text.setFont(Font.font ("Verdana", 20));
					root.getChildren().add(text);
				}
				//Add to GUI
				root.getChildren().addAll( inputs, verticalTexts[0], verticalTexts[1],verticalTexts[2],verticalTexts[3]
						,horizontalTexts[0],horizontalTexts[1]);
				break;
			case 4:
				//Initial
				inputs = new Text(tt.getInputs().get(0)+tt.getInputs().get(1)+"/"+tt.getInputs().get(2)+tt.getInputs().get(3));
				inputs.setX(startX-8);
				inputs.setY(startY+15);
				for (int i=0; i<Math.pow(2,size); i++) {
					Text text = new Text(tt.getRows()[i].getCol()[size]);
					if (tt.getRows()[i].getCol()[size].equals("1"))
					{
						for (int j = 0; j < shells.length; j++) {
							boolean right_shell = true;
							for (int j2 = 0; j2 < shells[j].getAdress().length(); j2++) {
								if (!shells[j].getAdress().substring(j2, j2 + 1).equals(tt.getRows()[i].getCol()[j2]))
									right_shell = false;
							}
							if (right_shell)
							{
								text.setFill(shells[j].getColor()==Color.BLACK ? (Main.isDarkTheme ? Color.ANTIQUEWHITE:Color.BLACK):shells[j].getColor());
								break;
							}
						
						}
					}
					else{
						text.setFill(Main.isDarkTheme ? Color.ANTIQUEWHITE:Color.BLACK);
					}
					text.setFont(Font.font ("Verdana", 20));
					text.setX(horizontalTexts[decToGray(i,size,false)].getX()+6);
					text.setY(verticalTexts[decToGray(i,size,true)].getY());
					root.getChildren().add(text);
				}


				root.getChildren().addAll(inputs,verticalTexts[0], verticalTexts[1],verticalTexts[2],verticalTexts[3],horizontalTexts[0],horizontalTexts[1]
						,horizontalTexts[2],horizontalTexts[3]);
				break;
			default:
				System.out.print("Invalid size");
				break;
		}
		inputs.setFont(Font.font("Verdana",15));
		inputs.setFill(Main.isDarkTheme ? Color.ANTIQUEWHITE:Color.BLACK);
		root.getChildren().addAll(lineY,lineX);
		Text output = new Text("F = "+getFinalform());
		output.setX(Screen.getPrimary().getVisualBounds().getWidth()/2-(5*(output.getText().length()/2)));
		output.setFont(Font.font ("Verdana", 15));
		output.setFill(Main.isDarkTheme ? Color.ANTIQUEWHITE:Color.BLACK);
		root.getChildren().add(output);

	}
	public int bitAt(int number, int index, int length) {
		String m = Integer.toBinaryString(number);
		String result = "";
		for (int i = 0; i < length - m.length(); i++) {
			result += 0;
		}
		result += m;

		if (index == result.length()) {
			return Integer.parseInt(result.substring(index));
		} else {
			return Integer.parseInt(result.substring(index, index + 1));
		}

	}
	public int grayCode(int number){
		if (number==3)
			return 2;
		else if (number==2)
			return 3;
		else
			return number;
	}
	public int decToGray(int number, int length, boolean reversed){
		if (number==1)
			number=1;
		String s = Integer.toBinaryString(number);
		int size = s.length();
		for (int i = 0; i < length - size; i++) {
			s = "0" + s;
		}
		if (reversed) {
			s = s.substring(0, 2);
			int num = ((2 * (Integer.parseInt(s.charAt(0) + ""))) + (Integer.parseInt(s.charAt(1) + "")));
			return grayCode(num);
		}
		else{
			s = s.substring(2,4);
			int num = ((2 * (Integer.parseInt(s.charAt(0) + ""))) + (Integer.parseInt(s.charAt(1) + "")));
			return grayCode(num);
		}

	}
	public ArrayList<ArrayList<Shell>> getForcolor() {
		return forcolor;
	}
	public void setForcolor(ArrayList<ArrayList<Shell>> forcolor) {
		this.forcolor = forcolor;
	}
}
