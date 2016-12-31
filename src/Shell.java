import javafx.scene.paint.Color;

public class Shell {
	private Shell[] neighbours;
	private int truth_value;
	private String adress;
	private Shell fourtuplets[];
	private Color color = Color.BLACK;
	private boolean simplified = false;
	public Shell(int size)
	{
		setNeighbours(new Shell[size]);
	}
	public Shell(Shell[] fourtuplets)
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


	public Shell[] getNeighbours() {
		return neighbours;
	}


	public void setNeighbours(Shell[] neighbours) {
		this.neighbours = neighbours;
	}
	public Shell[] getFourtuplets() {
		return fourtuplets;
	}
	public void setFourtuplets(Shell fourtuplets[]) {
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
