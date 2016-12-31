
public class TTRow {
	private String[] col;
	public TTRow(int length){
		col = new String[length+1];
		for (int i = 0; i < col.length; i++) {
			col[i] = "0";
		}
	}
	public void edit(int index,String value){
		col[index] = value;
		System.out.println("de�i�ti");
	}
	public String get0() {
		return col[0];
	}
	public String get1() {
		return col[1];
	}

	public String get2() {
		return col[2];
	}

	public String get3() {
		return col[3];
	}

	public String get4() {
		switch(col.length){
		case 2:
			return col[1];
		case 3:
			return col[2];
		case 4:
			return col[3];
		case 5:
			return col[4];
		default:
			return null;
	}
	}
	
	public String[] getCol(){
		return col;
	}
}
