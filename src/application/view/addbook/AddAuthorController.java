package application.view.addbook;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.pojo.Address;
import application.pojo.Author;
import application.util.LibraryUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class AddAuthorController implements Initializable{
	
	@FXML
	private Pane treeRoot;
	@FXML
	private Label headLabel;
	@FXML
	private TextField firstName;
	@FXML
	private TextField lastName;
	@FXML
	private TextField phoneNum;
	@FXML
	private TextField street;
	@FXML
	private TextField city;
	@FXML
	private TextField state;
	@FXML
	private TextField zip;
	@FXML
	private TextArea bio;
	@FXML
	private Button cancel;
	@FXML
	private Button save;
	
	static List<Author> list = new ArrayList<>();
	
	public void addAuthor() {
		Alert alert = new Alert(AlertType.INFORMATION);
		String s = street.getText();
		String c = city.getText();
		String st = state.getText();
		String z = zip.getText();
		String f = firstName.getText();
		String l = lastName.getText();
		String p = phoneNum.getText();
		String b = bio.getText();
		if(s.isEmpty() || c.isEmpty() || st.isEmpty() || z.isEmpty() || f.isEmpty() || l.isEmpty() || p.isEmpty() || b.isEmpty()) {
			alert.setContentText("All fileds must be filled!");
			alert.show();
		}else {
			Address addressOfAuthor = new Address(s,c, st, z);
			Author author = new Author(f, l, p, addressOfAuthor, b);
			list.add(author);
			street.clear();
			city.clear();
			state.clear();
			zip.clear();
			firstName.clear();
			lastName.clear();
			phoneNum.clear();
			bio.clear();
			alert.setContentText("Add author successfully!");
			alert.showAndWait();
		}
		
	}
	
	
	public List<Author> getList() {
		return list;
	}

	public void clear() {
		street.clear();
		city.clear();
		state.clear();
		zip.clear();
		firstName.clear();
		lastName.clear();
		phoneNum.clear();
		bio.clear();
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		headLabel.setGraphic(new ImageView(new Image("/application/icon/author.png")));
		save.setGraphic(new ImageView(new Image("/application/icon/add.png")));
		cancel.setGraphic(new ImageView(new Image("/application/icon/clear.png")));
		
		// force the field to be numeric only
		LibraryUtil.numberTextfield(phoneNum);
		LibraryUtil.numberTextfield(zip);
		
		DropShadow dropshadow = new DropShadow();
        dropshadow.setRadius(10);
        dropshadow.setOffsetX(0);
        dropshadow.setOffsetY(0);
        dropshadow.setSpread(0.1);
        dropshadow.setColor(Color.GRAY);
        treeRoot.setEffect(dropshadow);
	}
}
