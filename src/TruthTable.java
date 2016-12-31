import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TruthTable {
	private ArrayList<String> inputs;
	private TTRow[] rows;
	String booleanExpression;
	BooleanExpression be;
	private boolean isTrue;

	//Constructors
	public TruthTable(String path) {
		booleanExpression="";
		inputs = new ArrayList<>();
		try {

			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			if (setInputs(line)) {

				rows = new TTRow[(int) Math.pow(2, inputs.size())];
				createTable();
				int counter = 0;
				boolean MAE = false;
				while((line=br.readLine())!=null){
					String[] temp = line.split(",|;");
					for(int i=0; i<temp.length; i++){
						if (i != temp.length-1 && !rows[counter].getCol()[i].equals(temp[i])) {
							MAE = true;
							System.out.println("Invalid term in the input file at "+counter+". row in "+inputs.get(i));
						}
						else if (!MAE && i == temp.length-1) {
							rows[counter].getCol()[i] = temp[i];
							if(rows[counter].getCol()[i].equals("1")){
								for (int j = 0; j < i; j++) {
									booleanExpression += rows[counter].getCol()[j].equals("1") ? (inputs.get(j)+".") : inputs.get(j)+"'.";

								}
								booleanExpression = booleanExpression.substring(0, booleanExpression.length()-1);
								booleanExpression+="+";
							}
						}
					}
					counter++;
				}
				if (MAE) {
					clearTable();
					booleanExpression="";
					be = new BooleanExpression(inputs);
				}
				else{
					//System.out.println(booleanExpression.substring(0, booleanExpression.length()-1));
					be = new BooleanExpression(booleanExpression.split("\\+"));
				}
				isTrue = MAE;
			}

			br.close();

		}catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public TruthTable(ArrayList<String> al){
		inputs = al;
		rows = new TTRow[(int) Math.pow(2, inputs.size())];
		createTable();

	}
	//Methods
	public void edit(int col, int row, String value){
		rows[row].getCol()[col] = value;
		booleanExpression = "";
		for (int i = 0; i < Math.pow(2, inputs.size()); i++) {
			if(rows[i].getCol()[inputs.size()].equals("1")){
				for (int j = 0; j < inputs.size(); j++) {
					booleanExpression += rows[i].getCol()[j].equals("1") ? (inputs.get(j)+".") : inputs.get(j)+"'.";
				}
				booleanExpression = booleanExpression.substring(0, booleanExpression.length()-1);
				booleanExpression+="+";
			}
		}
		BooleanExpression be = new BooleanExpression(booleanExpression.split("\\+"));
	}
	public void createTable() {
		TTRow ttr;
		for (int i = 0; i < rows.length; i++) {
			ttr = new TTRow(inputs.size());
			for (int j = 0; j < inputs.size(); j++) {
				ttr.getCol()[j] = bitAt(i, j, inputs.size())+"";
				System.out.print(bitAt(i, j, inputs.size()));
			}
			rows[i] = ttr;
			System.out.println();
		}
	}
	public void clearTable(){
		for (int i = 0; i < rows.length; i++) {
			rows[i].getCol()[inputs.size()] = "";
			System.out.println();
		}
	}
	public boolean setInputs(String str) {
		boolean flag = true;
		str = str.replaceAll("\\;|\\,", "");
		for (int i = 0; i < str.length() - 1; i++) {
			if (!inputs.contains(str.substring(i, i + 1))) {
				inputs.add(str.substring(i, i + 1));
			} else {
				System.out.println("Wrong input in txt");
				flag = false;
			}
		}
		return flag;
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

	public ArrayList<String> getInputs(){
		return inputs;
	}
	public TTRow[] getRows(){
		return rows;
	}
	public boolean isTrue() {
		return isTrue;
	}
	public void setTrue(boolean isTrue) {
		this.isTrue = isTrue;
	}

}
