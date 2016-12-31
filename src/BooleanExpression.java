import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BooleanExpression {
	//Attributes
	private ArrayList<String> inputs;
	private ArrayList<String> inputsV2;
	private String[] splitArray;
	private TruthTable tt;
	private TruthTable ttV2;
	private BooleanExpression beSimplified;
	private KarnaughMap kmap;
	//Constructor
	public BooleanExpression(ArrayList al) {
		
		inputs = al;
		tt = new TruthTable(inputs);
		
	}
	public BooleanExpression(String path){
		inputs = new ArrayList<>();
		inputsV2 = new ArrayList<>();
		splitArray = reader(path);
		if (splitArray.length > 0) {
			inputs = setInputs(splitArray[0]);
		}
		if(inputs.size()<=4){
			tt = new TruthTable(inputs);
			fillTable(tt,inputs,splitArray);
			kmap = new KarnaughMap(tt,inputs.size(),inputs);
			ttV2 = getTtV2();
		}
		else{
			Main.showAlert("Invalid Input Size", "Your input size ("+inputs.size()+") can't be more than 4.");
		}
	}
	public BooleanExpression(String[] array){
		inputs = new ArrayList<>();
		inputsV2 = new ArrayList<>();
		splitArray = array;
		if (splitArray.length > 0) {
			inputs = setInputs(splitArray[0]);
		}
		if(inputs.size()<=4){
			tt = new TruthTable(inputs);
			fillTable(tt,inputs,splitArray);
			kmap = new KarnaughMap(tt,inputs.size(),inputs);
			ttV2 = getTtV2();
			System.out.print(kmap.getFinalform());
		}
		else{
			Main.showAlert("Invalid Input Size", "Your input size ("+inputs.size()+") can't be more than 4.");
		}
	}
	
	public void fillTable(TruthTable m,ArrayList a, String[] split) { // splitArray yerine parametre alabilir miyiz?
		if (split != null) {
			for (String chunk : split) {
				int temp = Integer.parseInt(Integer.toString((int) (Math.pow(2, a.size())-1),2));
				for (int i = 0; i < chunk.length(); i++) {
					if (chunk.charAt(i) == '\'') {
						for (int j = 0; j < a.size(); j++) {
							if ((chunk.charAt(i-1)+"").equals(a.get(j)))
								temp -= Math.pow(10, a.size()-1-j);
							
						}
					}
				}
				temp = Integer.parseInt(temp + "", 2);
				m.getRows()[temp].getCol()[a.size()] = "1";
			}
		}
	}
	
	//Simplifying
	public String simplify (String[] array) {
		String[] binaryForm = new String[array.length];
		for (int i = 0; i < array.length; i++) {
			binaryForm[i] = "";
			int counter = 0;
			while (array[i].length() > counter) {
				if ((array[i].charAt(counter) >= 65 && array[i].charAt(counter) <= 90)
						|| (array[i].charAt(counter) >= 97 && array[i].charAt(counter) <= 122)) {
					binaryForm[i] += 1;
				} else if (array[i].charAt(counter) == 39) {
					binaryForm[i] = binaryForm[i].substring(0, binaryForm[i].length() - 1);
					binaryForm[i] += 0;
				}
				counter++;
			}
		}
		while (isChanged(binaryForm)) {
			binaryForm = simplifier(binaryForm);
		}
		return convert(binaryForm);
	}
	public String[] simplifier(String[] a) {
		ArrayList<String> arrayList = new ArrayList<String>();
		String[] binaryV1 = new String[(a.length * (a.length + 1)) / 2];
		boolean[] checkForm = new boolean[(a.length * (a.length + 1)) / 2];

		int sum = 0;
		int which = 0;
		int howOne = 0;
		int travel = 0;

		for (int i = 0; i < a.length - 1; i++) {
			for (int j = i + 1; j < a.length; j++) {
				which = 0;
				travel = 0;
				howOne = 0;
				if (!(a[i] == null || a[j] == null)) { // �kisinden biri null' a
														// e�itse, girmemesi
														// gerek.
					while (a[i].length() > travel) {
						if (a[i].charAt(travel) != a[j].charAt(travel)) {

							howOne++;
							which = travel;
						}

						travel++;
					}
				}

				if (howOne == 1) {
					String add = "";
					if (which == 0) {
						add += "-";
						add += a[j].substring(which + 1); // Yani 1.
						// checkForm[i] = true;
						// checkForm[j] = true;
					} else {
						add += a[j].substring(0, which);
						add += "-";
						add += a[j].substring(which + 1);
					}
					if (!arrayList.contains(add)) { // Daha �nce eklenmi�se.

						binaryV1[sum] = add;
						sum++;
					}
					arrayList.add(add);
					checkForm[i] = true;
					checkForm[j] = true;
					System.out.println(i + " ve " + j + " = " + binaryV1[sum - 1]);
				}
			}
		}

		for (int i = 0; i < a.length; i++) {

			if (!checkForm[i]) {
				binaryV1[sum] = a[i];
				sum++;
			}
		}

		// AYNI SAYILARDAN OLU�TUMU KONTROL�N� YAP �YLE RETURN ET.
		return binaryV1;
	}
	public boolean isChanged(String[] a) {
		int howOne = 0;
		int travel = 0;
		boolean flag = false;

		for (int i = 0; i < a.length - 1; i++) {
			for (int j = i + 1; j < a.length; j++) {
				travel = 0;
				howOne = 0;
				if (!(a[i] == null || a[j] == null)) {
					while (a[i].length() > travel) {
						if (a[i].charAt(travel) != a[j].charAt(travel)) {
							howOne++;
						}
						travel++;
					}
				}
				if (howOne == 1) {
					flag = true;
					break;
				}
			}
			if (flag) {
				break;
			}
		}
		
		return flag;
	}	
	public String convert(String[] a) {
		String result = "";
		for (int i = 0; i < a.length && a[i] != null; i++) {

			for (int j = 0; j < a[i].length(); j++) {

				if (a[i].charAt(j) == 48) { // 0 inki 48, 1 inki 49.

					result += inputs.get(j)+ "'.";
				} else if (a[i].charAt(j) == 49) {
					result += inputs.get(j) + ".";
				}
			}
			result = result.substring(0, result.length() - 1);
			result += " + ";
		}
		return result.substring(0, result.length() - 3);
	}
	
	//Methods
	public boolean lengthControl(String[] array){
		for (int i = 0; i < array.length; i++) {
			if (array[i].replaceAll("\\'", "").length() != array[array.length-i-1].replaceAll("\\'", "").length()) return false;
		}
		return true;
	}
	public ArrayList<String> setInputs (String str) {
		ArrayList<String> AL = new ArrayList<String>();
		str = str.replaceAll("\\.|\\'","");
		for (int i = 0; i < str.length(); i++) {
			AL.add(str.charAt(i)+"");
		}
		return AL;
	}
	public String[] reader(String path){
		String[] split;
		String line = "";
		String str = "";
		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			while((line=br.readLine())!=null){
				str += line;
			}
			str = str.replaceAll("\\s+", "");
			System.out.println(str);
			br.close();
		}catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(str.split("=").length==1 || str.split("=").length==2){
			split = str.split("=")[str.split("=").length-1].split("\\+");
			if(!lengthControl(split)){
				System.out.println("lenth error");
				return new String[0];
			}
		}
		
		else{
			System.out.println("Invalid input.");
			return new String[0];
		}

		return split;
	}
	public TruthTable getTt(){

		return tt;
	}
	public void setTt(TruthTable m) {
		tt = m;
	}
	
	
	public BooleanExpression getBeSimplified() {
		return beSimplified;
	}
	public void setBeSimplified(BooleanExpression beSimplified) {
		this.beSimplified = beSimplified;
	}
	public TruthTable getTtV2() {

		inputsV2 = new ArrayList<>();
		kmap = new KarnaughMap(tt,inputs.size(),inputs);
		String[] tmp = kmap.getFinalform().split("\\+");
		if (tmp.length > 0) {
			String[] arrayyy = new String[inputs.size()];
			for (int i = 0; i < tmp.length; i++) {
				for (int j = 0; j < tmp[i].length(); j++) {
					if (!(inputsV2.contains(tmp[i].charAt(j)+"")) && inputs.contains(tmp[i].charAt(j)+"")) {
						for (int q = 0; q < inputs.size(); q++) {
							if (inputs.get(q).equals(tmp[i].charAt(j)+"")) {
								arrayyy[q] =  tmp[i].charAt(j)+"";
							}
						}
					}
				}
			}
			for (int i = 0; i < arrayyy.length; i++) {
				if (arrayyy[i] != null) {
					inputsV2.add(arrayyy[i]);
				}
			}
			ttV2 = new TruthTable(inputsV2);
			fillTable(ttV2,inputsV2,tmp);
		}
		return ttV2;
	}
}
