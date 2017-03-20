import javafx.scene.paint.Color;

public class Cell {
	private Cell[] neighbours;
	private int truth_value;
	private String adress;
	private Cell fourtuplets[];
	private Color color = Color.BLACK;
	private boolean simplified = false;
	public Cell(int size)
	{
		setNeighbours(new Cell[size]);
	}
	public Cell(Cell[] fourtuplets)
	{
		this.setFourtuplets(fourtuplets);
	}

	public int getTruth_value() {
		return truth_value;
	}
	public void setTruth_value(int truth_value) {
		this.truth_value = truth_value;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}


	public Cell[] getNeighbours() {
		return neighbours;
	}


	public void setNeighbours(Cell[] neighbours) {
		this.neighbours = neighbours;
	}
	public Cell[] getFourtuplets() {
		return fourtuplets;
	}
	public void setFourtuplets(Cell fourtuplets[]) {
		this.fourtuplets = fourtuplets;
	}
	public boolean isSimplified() {
		return simplified;
	}
	public void setSimplified(boolean simplified) {
		this.simplified = simplified;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		if(this.color == Color.BLACK)
			this.color = color;
		else
			this.color = Color.DARKGREEN;
	}

}
