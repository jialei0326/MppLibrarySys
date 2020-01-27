package application.view.login;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.pojo.Address;
import application.pojo.FxController;
import application.pojo.LibraryMember;
import application.util.DataAccessUtil;
import application.util.StageManageUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AddMemberController implements Initializable{
	@FXML
	private Pane treeRoot;
	@FXML
	private Label headLabel;
	@FXML
	private TextField id;
	@FXML
	private TextField fn;
	@FXML
	private TextField ln;
	@FXML
	private TextField st;
	@FXML
	private TextField ct;
	@FXML
	private TextField ste;
	@FXML
	private TextField zp;
	@FXML
	private TextField ph;
	@FXML
	private Button saveBtn;
	@FXML
	private Button backL;
	
	public void addMember(ActionEvent e) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Adding's Confirmation");
		
		if(fn.getText().isEmpty()||ln.getText().isEmpty()||ph.getText().isEmpty()||
				st.getText().isEmpty()||ct.getText().isEmpty()||ste.getText().isEmpty()||zp.getText().isEmpty()) {
			alert.setContentText("All feild must be filled.");
			alert.show();
		}
		else if(ph.getText().length()!=10){
			alert.setContentText("Invalid phone number. Format:6412330189");
			alert.show();
		}
		else {
			String idM=fn.getText().substring(0, 2)+ln.getText().substring(0, 2)+ph.getText().substring(5,9);
			LocalDateTime l = LocalDateTime.now();
			
			//id.setText(idM);
			try {
				Address newAddress = new Address(st.getText(), ct.getText(), ste.getText(), zp.getText());
				LibraryMember newMember = null;
				if(saveBtn.getText().equals("Edit")) {
					newMember = new LibraryMember(id.getText(), fn.getText(), ln.getText(), ph.getText(), newAddress);
					DataAccessUtil.updateMember(newMember);
					alert.setContentText("Member's editting Successfully...");
					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK){
						Stage stage = (Stage)saveBtn.getScene().getWindow();
						stage.close();
					}
				}else {
					newMember = new LibraryMember(idM, fn.getText(), ln.getText(), ph.getText(), newAddress);
					DataAccessUtil.saveNewMember(newMember);
					alert.setContentText("Member's adding Successfully...");
					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == ButtonType.OK){
						Stage stage = (Stage)saveBtn.getScene().getWindow();
						stage.close();
					}
				}
				//refresh memberlist
				HashMap<String, LibraryMember> membermap = DataAccessUtil.readMemberMap();
				List<LibraryMember> result2 = new ArrayList<LibraryMember>(membermap.values());
				ObservableList<LibraryMember> observableList = FXCollections.observableList(result2);
				MainMenuController s =(MainMenuController) StageManageUtil.CONTROLLER.get(FxController.MainMenuController);
				s.refreshMemberList(observableList);
		        
			} catch (Exception ex) {
				ex.printStackTrace();
				if(saveBtn.getText().equals("Edit")) {
					alert.setContentText("Error on editting new Member...");
				}else {
					alert.setContentText("Error on adding new Member...");
				}
				alert.showAndWait();
			}
		}
    }
	public void phoneTest(KeyEvent kv) throws IOException{
		if (!"0123456789".contains(kv.getCharacter())) {
		      kv.consume();
		   }
	}
	public void zipTest(KeyEvent kv) throws IOException{
		if (!"0123456789".contains(kv.getCharacter())) {
		      kv.consume();
		   }
	}
	public void backToMain(ActionEvent actionEvent) throws IOException {
		Stage stage = (Stage) backL.getScene().getWindow();
	    stage.close();
      
	    Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));    
        Scene scene = new Scene(root);

        Stage stage1 = new Stage();            
        stage1.setScene(scene);
        stage1.show(); 
	}
	public void fileText(LibraryMember libmem) {
		id.setText(libmem.getMemberId());
		fn.setText(libmem.getFirstName());
		ln.setText(libmem.getLastName());
		st.setText(libmem.getAddress().getStreet());
		ct.setText(libmem.getAddress().getCity());
		ste.setText(libmem.getAddress().getState());
		zp.setText(libmem.getAddress().getZip());
		ph.setText(libmem.getTelephone());
		saveBtn.setText("Edit");
		
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		headLabel.setGraphic(new ImageView(new Image("/application/icon/add_member.png")));
		saveBtn.setGraphic(new ImageView(new Image("/application/icon/add.png")));
		DropShadow dropshadow = new DropShadow();
        dropshadow.setRadius(10);
        dropshadow.setOffsetX(0);
        dropshadow.setOffsetY(0);
        dropshadow.setSpread(0.1);
        dropshadow.setColor(Color.GRAY);
        treeRoot.setEffect(dropshadow);
	}
}
