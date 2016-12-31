import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
	// Table
	TruthTable tt;
	TruthTable ttV2;
	KarnaughMap kmap;
	BooleanExpression be;

	// GUI Elements
	TableView<TTRow> tableV1, tableV2;
	VBox vcenter;
	HBox tables;
	BorderPane bp;
	Pane pane;
	Scene scene;
	public static boolean isDarkTheme = true;
	// Buttons
	private Button btnChoose;

	// Texts
	private Label txtLabel1;
	private TextField booleanwithHand;
	static Label txtFieldResult;

	// File reading tools
	private FileChooser fc;
	private File file;
	String line;
	String path;

	@Override
	public void start(Stage primaryStage) throws Exception {

		// Menu
		MenuBar mb = new MenuBar();
		Menu files = new Menu("File");
		Menu help = new Menu("Help");
		Menu exit = new Menu("Exit");
		exit.setOnAction(actionEvent -> Platform.exit());
		Menu design = new Menu("Design");
		MenuItem orangeTheme = new MenuItem("Orange Theme");
		orangeTheme.setOnAction(e -> {
			scene.getStylesheets().remove(scene.getStylesheets().size() - 1);
			scene.getStylesheets().add("resources/orangetheme.css");
			isDarkTheme = true;
		});
		MenuItem darkTheme = new MenuItem("Black and White Theme");
		darkTheme.setOnAction(e -> {
			scene.getStylesheets().remove(scene.getStylesheets().size() - 1);
			scene.getStylesheets().add("resources/darktheme.css");
			isDarkTheme = false;
		});
		MenuItem select = new MenuItem("Select File");
		select.setOnAction(e -> {
			booleanwithHand.setText("Enter boolean expression");
			getPath(primaryStage);
		});

		MenuItem savett = new MenuItem("Save TT");
		savett.setOnAction(e -> { 
			if(tt==null)
				showAlert("Save Error", "There is no truth table at the moment.");
			else{
				export(primaryStage,"tt");
			}   
		});
		MenuItem savebe = new MenuItem("Save BE");
		savebe.setOnAction(e -> { if(be==null)
			showAlert("Save Error", "There is no boolean expression at the moment.");
		else{
			export(primaryStage,"be");   }});






		MenuItem clear = new MenuItem("Clear");
		clear.setOnAction(e -> {
			if (vcenter.getChildren().size() == 4)
				vcenter.getChildren().remove(3);
			pane.getChildren().clear();
		});
		MenuItem us = new MenuItem("About Us");
		us.setOnAction(e -> showAlert("About Us", "This application is developed by DEUCENG students: \n Mehmet Alp Sümer \n Deniz Akkuþ \n Kubilay Tek"));
		MenuItem project = new MenuItem("Help");
		project.setOnAction(e -> showAlert("Help","To give inputs you should write your expression in the box in a form like X.Y+X'.Y or you can simply click load and select your files."));
		Menu table_size = new Menu("Generate Table");
		MenuItem twobytwo = new MenuItem("2 Inputs");
		twobytwo.setOnAction(e -> {
			booleanwithHand.setText("2");
			getPath(primaryStage);
			booleanwithHand.setText("Enter boolean expression");
		});
		MenuItem threebythree = new MenuItem("3 Inputs");
		threebythree.setOnAction(e -> {
			booleanwithHand.setText("3");
			getPath(primaryStage);
			booleanwithHand.setText("Enter boolean expression");
		});
		MenuItem fourbyfour = new MenuItem("4 Inputs");
		fourbyfour.setOnAction(e -> {
			booleanwithHand.setText("4");
			getPath(primaryStage);
			booleanwithHand.setText("Enter boolean expression");
		});
		help.getItems().addAll(us, project);
		files.getItems().addAll(select,savett,savebe, table_size, clear);
		design.getItems().addAll(orangeTheme, darkTheme);
		table_size.getItems().addAll(twobytwo, threebythree, fourbyfour);
		mb.getMenus().addAll(files, design, help, exit);

		// Creating the tables
		tableV1 = new TableView<>();
		tableV1.getStyleClass().add("table");
		tableV2 = new TableView<>();
		tableV2.getStyleClass().add("table2");

		// Button
		btnChoose = new Button("Load");
		btnChoose.setOnAction(e -> getPath(primaryStage));
		btnChoose.getStyleClass().add("button");

		// Title
		txtLabel1 = new Label("f.Simplicity");
		txtLabel1.getStyleClass().add("title");

		// Text field settings
		booleanwithHand = new TextField("Enter boolean expression");
		booleanwithHand.setEditable(true);
		booleanwithHand.setMaxWidth(400);
		booleanwithHand.getStyleClass().add("textfield");
		booleanwithHand.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ke) {
				if (ke.getCode().equals(KeyCode.ENTER)) {
					getPath(primaryStage);
				}
			}
		});
		booleanwithHand.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				booleanwithHand.setText("");
			}
		});
		booleanwithHand.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (booleanwithHand.getText().equals(""))
					booleanwithHand.setText("Enter boolean expression");
			}
		});
		// HBox
		tables = new HBox();
		tables.getChildren().addAll(tableV1,tableV2);
		tables.setSpacing(25);
		tables.setAlignment(Pos.CENTER);

		// Center VBox
		vcenter = new VBox();
		vcenter.getChildren().addAll(txtLabel1, btnChoose, booleanwithHand);
		System.out.print(vcenter.getChildren().size());
		vcenter.setPadding(new Insets(20, 10, 0, 10));
		vcenter.setSpacing(10);
		vcenter.setAlignment(Pos.CENTER);

		// Layout & adding elements
		bp = new BorderPane();
		pane = new Pane();
		bp.setBottom(pane);
		bp.setCenter(vcenter);
		bp.setTop(mb);
		scene = new Scene(bp, 1024, 768);	
		scene.getStylesheets().add("resources/orangetheme.css");
		primaryStage.getIcons().add(new Image("resources/icon.jpg"));
		primaryStage.setScene(scene);
		primaryStage.setTitle("f. Simplicity");
		primaryStage.setMaximized(true);
		primaryStage.show();

	}

	// Read files and input
	public void getPath(Stage primaryStage) {
		boolean coksacma = false;
		if (!isInteger(booleanwithHand.getText())) {
			if (booleanwithHand.getText().equalsIgnoreCase("Enter boolean expression")
					|| booleanwithHand.getText().equals(null) || booleanwithHand.getText().equals("")) {
				System.out.println(booleanwithHand.getText());
				fc = new FileChooser();
				fc.getExtensionFilters()
				.addAll(new ExtensionFilter("Truth Table and Boolean Expression", "*tt", "*be"));
				file = fc.showOpenDialog(primaryStage);
				if (file != null) {
					path = file.getAbsolutePath().toString();
					if (path.substring(path.length() - 2).equals("be")) {
						be = new BooleanExpression(path);
						tt = be.getTt();
						refreshTable(tableV1);
						updatetableV2();
						refreshTableV2(tableV2);

					} else if (path.substring(path.length() - 2).equals("tt")) {
						tt = new TruthTable(path);

						refreshTable(tableV1);

						if (!tt.isTrue()) {

							updatetableV2();
							refreshTableV2(tableV2);
						}

					}
				}
			} else {
				be = new BooleanExpression(booleanwithHand.getText().replaceAll("\\s", "").split("\\+"));
				booleanwithHand.setText(booleanwithHand.getText().replaceAll("\\s", ""));
				tt = be.getTt();
				updatetableV2();
				refreshTable(tableV1);
				refreshTableV2(tableV2);
			}
		} else {
			if (booleanwithHand.getText().equalsIgnoreCase("2")) {
				ArrayList<String> al = new ArrayList<>();
				al.add("A");
				al.add("B");
				be = new BooleanExpression(al);
				tt = be.getTt();
				updatetableV2();

				refreshTable(tableV1);
				refreshTableV2(tableV2);
			} else if (booleanwithHand.getText().equalsIgnoreCase("3")) {
				ArrayList<String> al = new ArrayList<>();
				al.add("A");
				al.add("B");
				al.add("C");
				be = new BooleanExpression(al);
				tt = be.getTt();
				updatetableV2();

				refreshTable(tableV1);
				refreshTableV2(tableV2);
			} else if (booleanwithHand.getText().equalsIgnoreCase("4")) {
				ArrayList<String> al = new ArrayList<>();
				al.add("A");
				al.add("B");
				al.add("C");
				al.add("D");
				be = new BooleanExpression(al);
				tt = be.getTt();
				updatetableV2();

				refreshTable(tableV1);
				refreshTableV2(tableV2);
			} else {
				showAlert("Invalid Input", "Your input (" + booleanwithHand.getText() + ") must be between 2 and 4.");
				coksacma = true;
			}

		}
		if (!coksacma) {
			kmap.drawKMap(tt, tt.getInputs().size(), pane, (int)(Screen.getPrimary().getVisualBounds().getWidth()/2)-(15*tt.getInputs().size()), 10);
		}
		booleanwithHand.setText("Enter boolean expression");
	}

	// Create table and its columns and fill the data
	int tableSize = 200;
	public TableView<TTRow> createTable(TruthTable m) {
		TableView<TTRow> table = new TableView<TTRow>();
		String input = "";
		for (int i = 0; i < m.getInputs().size(); i++) {
			input = m.getInputs().get(i).toString();
			table.getColumns().add(createTableColumn(input, i + "",m));
		}
		table.getColumns().add(createTableColumn("Result", "4",m));
		table.setMinWidth(tableSize);
		table.setMaxWidth(tableSize);
		table.setMaxHeight(200);
		table.setMaxHeight(200);
		table.setEditable(true);

		return table;
	}
	public TableColumn<TTRow, String> createTableColumn(String name, String input, TruthTable m) {
		TableColumn<TTRow, String> tc = new TableColumn<TTRow, String>(name);
		tc.setMinWidth((tableSize / (tt.getInputs().size() + 1)) + 1);
		tc.setMaxWidth((tableSize / (tt.getInputs().size() + 1)) + 1);
		tc.setCellValueFactory(new PropertyValueFactory<>(input));

		if (input.equals("4")) {
			if (!tt.isTrue() && m==tt) {

				tc.setCellFactory(TextFieldTableCell.forTableColumn());
				tc.setOnEditCommit(new EventHandler<CellEditEvent<TTRow, String>>() {
					TableView<TTRow> old = tableV1;

					public void handle(CellEditEvent<TTRow, String> t) {

						if (t.getNewValue().equals("1") || t.getNewValue().equals("0")) {
							System.out.println(t.getOldValue());
							((TTRow) t.getTableView().getItems().get(t.getTablePosition().getRow())).getCol()[t
							                                                                                  .getTablePosition().getColumn()] = t.getNewValue();
							tt.edit(t.getTablePosition().getColumn(), t.getTablePosition().getRow(), t.getNewValue());
							updatetableV2();
							kmap.drawKMap(tt, tt.getInputs().size(), pane, (int)(Screen.getPrimary().getVisualBounds().getWidth()/2)-(15*tt.getInputs().size()), 10);
						} else {
							tableV1 = old;
						}
						refreshTable(tableV1);
						refreshTableV2(tableV2);
					}
				});
			}
		}
		tc.getStyleClass().add("tablecolumn");
		return tc;
	}
	public ObservableList<TTRow> generateTableV1() {
		ObservableList<TTRow> rows = FXCollections.observableArrayList();
		for (int i = 0; i < tt.getRows().length; i++) {
			rows.add(tt.getRows()[i]);
		}
		return rows;
	}
	public ObservableList<TTRow> generateTableV2() {
		ObservableList<TTRow> rows = FXCollections.observableArrayList();
		for (int i = 0; i < ttV2.getRows().length; i++) {
			rows.add(ttV2.getRows()[i]);
		}
		return rows;
	}

	// Refresh the tables in GUI
	public void refreshTable(TableView<TTRow> table) {

		table = createTable(tt);
		table.setItems(generateTableV1());

		if (tables.getChildren().size() == 1) {
			tables.getChildren().remove(0);

			tables = new HBox();
			tables.setSpacing(25);
			tables.setAlignment(Pos.CENTER);

			tables.getChildren().add(table);
		}
		else if (tables.getChildren().size() == 2){
			tables.getChildren().remove(1);
			tables.getChildren().remove(0);

			tables = new HBox();
			tables.setSpacing(25);
			tables.setAlignment(Pos.CENTER);


			tables.getChildren().addAll(table);
		}
		if (vcenter.getChildren().size() == 4) {
			vcenter.getChildren().remove(3);
			vcenter.getChildren().add(tables);
		}
		else if (vcenter.getChildren().size() == 3) {
			vcenter.getChildren().add(tables);
		}
	}
	public void refreshTableV2(TableView<TTRow> table) {

		table = createTable(ttV2);
		table.setItems(generateTableV2());


		if (tables.getChildren().size() == 1) {
			Node tbw = tables.getChildren().get(0);

			tables = new HBox();
			tables.setSpacing(25);
			tables.setAlignment(Pos.CENTER);

			tables.getChildren().addAll(tbw,table);
		}
		else if (tables.getChildren().size() == 2){
			Node tbw = tables.getChildren().get(0);
			tables.getChildren().remove(1);
			tables.getChildren().remove(0);

			tables = new HBox();
			tables.setSpacing(25);
			tables.setAlignment(Pos.CENTER);

			tables.getChildren().addAll(tbw,table);
		}

		if (vcenter.getChildren().size() == 4) {
			vcenter.getChildren().remove(3);
			vcenter.getChildren().add(tables);
		}
		else if (vcenter.getChildren().size() == 3) {
			vcenter.getChildren().add(tables);
		}
	}

	// Checks if input is integer or not
	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}
	
	// Something that I need to use;
	public void updatetableV2() {

		if (!tt.isTrue()) {
			tableV2 = new TableView<>();
			kmap = new KarnaughMap(tt, tt.getInputs().size(), tt.getInputs());
			String[] tmp = kmap.getFinalform().split("\\+");

			ArrayList<String> control = new ArrayList<>();
			ArrayList<String> cepte = tt.getInputs();

			if (tmp.length > 0) {
				String[] arrayyy = new String[cepte.size()];
				for (int i = 0; i < tmp.length; i++) {
					for (int j = 0; j < tmp[i].length(); j++) {
						if (!(control.contains(tmp[i].charAt(j) + "")) && cepte.contains(tmp[i].charAt(j) + "")) {
							for (int q = 0; q < cepte.size(); q++) {
								if (cepte.get(q).equals(tmp[i].charAt(j) + "")) {
									arrayyy[q] = tmp[i].charAt(j) + "";
								}
							}
						}
					}
				}
				for (int i = 0; i < arrayyy.length; i++) {
					if (arrayyy[i] != null) {
						control.add(arrayyy[i]);
					}
				}
				ttV2 = new TruthTable(control);
				createtableV2(ttV2, control, tmp);
			}
		}

	}
	public void createtableV2(TruthTable m, ArrayList a, String[] split) {

		if (split != null) {
			for (String chunk : split) {

				int count = 0;
				int[] saver = new int[howmanyDifferent(chunk)];
				String[] value = new String[howmanyDifferent(chunk)];
				for (int j = 0; j < chunk.length(); j++) {

					for (int k = 0; k < a.size(); k++) {

						if ((chunk.charAt(j) + "").equals(a.get(k))) {

							saver[count] = k;
							if (j < chunk.length() - 1) {
								if (chunk.charAt(j + 1) == '\'') {
									value[count] = "0";
								} else {
									value[count] = "1";
								}
							} else {
								value[count] = "1";
							}
							count++;
						}
					}
				}
				for (int i = 0; i < m.getRows().length; i++) {

					if (count == 1) {

						if (m.getRows()[i].getCol()[saver[0]].equals(value[0])) {
							m.getRows()[i].getCol()[a.size()] = "1";
						}
					} else if (count == 2) {

						if (m.getRows()[i].getCol()[saver[0]].equals(value[0])
								&& m.getRows()[i].getCol()[saver[1]].equals(value[1])) {
							m.getRows()[i].getCol()[a.size()] = "1";
						}
					} else if (count == 3) {
						if (m.getRows()[i].getCol()[saver[0]].equals(value[0])
								&& m.getRows()[i].getCol()[saver[1]].equals(value[1])
								& m.getRows()[i].getCol()[saver[2]].equals(value[2])) {
							m.getRows()[i].getCol()[a.size()] = "1";
						}
					}
				}
			}
		}
	}
	public static int howmanyDifferent(String str) {

		ArrayList<String> al = new ArrayList<>();
		for (int j = 0; j < str.length(); j++) {

			if ((str.charAt(j) >= 65 && str.charAt(j) <= 90) || str.charAt(j) >= 97 && str.charAt(j) <= 122) {

				if (!(al.contains(str.charAt(j) + ""))) {
					al.add(str.charAt(j) + "");
				}
			}
		}
		return al.size();
	}
	
	//Save-export files
	private void saveFile(String type, File file){
		try {
			FileWriter fileWriter = null;     
			fileWriter = new FileWriter(file);
			if(type.equals("tt")){
				for (int i = 0; i < tt.getInputs().size(); i++) {
					fileWriter.write(tt.getInputs().get(i));
					if(i == tt.getInputs().size()-1)
						fileWriter.write(";");
					else
						fileWriter.write(",");
				}
				fileWriter.write("F");
				fileWriter.write(System.lineSeparator());
				for (int i = 0; i < tt.getRows().length; i++) {
					for (int j = 0; j < tt.getRows()[i].getCol().length; j++) {
						fileWriter.write(tt.getRows()[i].getCol()[j]);
						if(j != tt.getInputs().size()){
							if(j == tt.getInputs().size()-1)
								fileWriter.write(";");
							else
								fileWriter.write(",");
						}
					}
					fileWriter.write(System.lineSeparator());
				}
			}
			else{
				showAlert("Hoaydaaa", "Kanka bi 3 gün daha verirsen bunu da ekleriz.");
				//	            	fileWriter.write(kmap.getFinalform());
			}
			fileWriter.close();
		} catch (IOException ex) {
			System.out.println("Catch'em all");
		}
	}
	public void export(Stage primaryStage, String type){
		fc = new FileChooser();
		fc.getExtensionFilters().add(type.equals("tt") ? new ExtensionFilter("Truth Table (*.tt)", "*.tt"):new ExtensionFilter("Boolean Expression (*.be)", "*.be"));
		file = fc.showSaveDialog(primaryStage);
		if(file != null){
			saveFile(type,file);
		}
	}


	// Shows alert window
	public static void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch();
	}
}
