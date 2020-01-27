package application.view.login;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import application.exception.LoginException;
import application.pojo.Auth;
import application.pojo.Book;
import application.pojo.FxController;
import application.pojo.LibraryMember;
import application.pojo.User;
import application.util.DataAccessUtil;
import application.util.LibraryUtil;
import application.util.StageManageUtil;
import application.view.checkout.BookInfoDetailController;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.Alert.AlertType;

public class MainMenuController implements Initializable{
	
	@FXML
	private Pane treeRoot;
	@FXML
	private AnchorPane root;
	@FXML
	private Button lg;
	@FXML
	private Button bk;
	@FXML
	private Label lu;
	@FXML
	private Label lp;
	@FXML
	private TextField un;
	@FXML
	private PasswordField pw;
	@FXML
	private Button subm;
	@FXML
	private Button cls;
	@FXML
	private Button clsL;
	@FXML
	private Button clsA;
	@FXML
	private Button clsB;
	@FXML
	private Button addL;
	@FXML
	private Button editL;
	@FXML
	private Button addB;
	@FXML
	private Button addC;
	@FXML
	private Label lib;
	@FXML
	private Label adm;
	@FXML
	private Label both;
	@FXML
	private Button backL;
	@FXML
	private Label welcome;
	@FXML
	private Label copyRight;
	@FXML
	private Label headPic;
	@FXML
	private Label librarySystem;
	@FXML
	private CheckBox remeberMe;
	@FXML
	private Circle logoImage;
	@FXML
	private ListView<LibraryMember> memberList;
	@FXML
	private ListView<Book> bookList;
	@FXML
	private Label members;
	@FXML
	private Label books;
	@FXML
	private Button logout;
	@FXML
	private Label unavaliable;
	@FXML
	private Label avaliable;
	@FXML
	private Label unavaliableColor;
	@FXML
	private Label avaliableColor;
	@FXML
	private Label hintmember;
	@FXML
	private Label hintmemberLib;
	@FXML
	private Label hintBook;
	@FXML
	private Separator sepa1;
	
	private boolean turnFlag = false;
	
	public static Auth currentAuth = null;
	
	public String login(String id, String password,Boolean isRemeberMe) throws LoginException {
		HashMap<String, User> map = DataAccessUtil.readUserMap();
		if(!map.containsKey(id)) {
			throw new LoginException("UserName " + id + " not found");
		}
		String passwordFound = map.get(id).getPassword();
		if(!passwordFound.equals(password)) {
			throw new LoginException("Password incorrect");
		}
		List<User> userlist = new ArrayList<User>(map.values());
		for(User u:userlist) {
			u.setRememberMe(false);
		}
		if(isRemeberMe) {
			User user= map.get(id);
			user.setRememberMe(true);
		}
		DataAccessUtil.loadUserMap(userlist);
		
		currentAuth = map.get(id).getAuthorization();
		return map.get(id).getId();
	}
	
	
	public void logAccess(ActionEvent e) {
		try {
			headPic.setText("User: "+login(un.getText().trim(), pw.getText().trim(),remeberMe.isSelected()));
			
			memberList.setVisible(true);
			bookList.setVisible(true);
			members.setVisible(true);
			books.setVisible(true);
			logout.setVisible(true);
			unavaliable.setVisible(true);
			unavaliableColor.setVisible(true);
			avaliable.setVisible(true);
			avaliableColor.setVisible(true);
			sepa1.setVisible(true);
			hintmember.setVisible(true);
			hintBook.setVisible(true);
			if (currentAuth == Auth.LIBRARIAN) {
				// disable ADMIN options
				hide();
				bk.setVisible(true);
				lib.setVisible(true);
				hintmemberLib.setVisible(true);
				hintmember.setVisible(false);
			} else if (currentAuth == Auth.ADMIN) {
				// disable LIBRARIAN options
				hide();
				addL.setVisible(true);
				addB.setVisible(true);
				addC.setVisible(true);
				editL.setVisible(true);
				adm.setVisible(true);
			}else if(currentAuth == Auth.BOTH) {
				hide();
				bk.setVisible(true);
				addB.setVisible(true);
				both.setVisible(true);
				addL.setVisible(true);
				editL.setVisible(true);
				addC.setVisible(true);
			}

			//un.setText("");
			//pw.setText("");
		} catch (LoginException ex) {
			System.out.println(ex.getMessage());
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Login Error");
			alert.setContentText("Incorrect username and/or password..");
			alert.showAndWait();
		}

	}
	
	//
	public void closeMain(ActionEvent actionEvent) throws IOException {
		Stage stage = (Stage) cls.getScene().getWindow();
	    stage.close();
	}
	public void closeBut(ActionEvent actionEvent) throws IOException {
		Stage stage = (Stage) cls.getScene().getWindow();
	    stage.close();
	}
	
	public void libraryMember(ActionEvent actionEvent) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("LibraryMember.fxml"));    
        Scene scene = new Scene(root);
        
        Stage stage1 = new Stage();     
        //StageManager
        StageManageUtil.STAGE.put(FxController.MainMenuStage, stage1);
        StageManageUtil.CONTROLLER.put(FxController.MainMenuController, this);
        stage1.setTitle("LibraryMember");
        stage1.setScene(scene);
        stage1.show();  
      
	}
	
	public void editLibraryMember(ActionEvent actionEvent) throws IOException {
		LibraryMember libmember = null;
		//No Member to Choose
		if(memberList.getItems().size() == 0) {
			LibraryUtil.alertChooseAMemberIsEmpty();
			return;
		}
		
		ObservableList<LibraryMember> selectedIndices = memberList.getSelectionModel().getSelectedItems();
        if(selectedIndices.isEmpty()) {
        	LibraryUtil.alertChooseAMember();
        	return;
        }else {
        	for(LibraryMember o : selectedIndices){
        		libmember=o;
        	}
        }
        
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("LibraryMember.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        AddMemberController control = fxmlLoader.getController();
        turnFlag = false;
        if(currentAuth == Auth.LIBRARIAN) {
        	turnFlag = true;
        }
        control.fileText(libmember,turnFlag);
        Stage stage = new Stage();
        stage.setTitle("LibraryMember");
        //StageManager
        StageManageUtil.STAGE.put(FxController.MainMenuStage, stage);
        StageManageUtil.CONTROLLER.put(FxController.MainMenuController, this);
        stage.setScene(scene);
        stage.show();  
      
	}
	
	public void submitLog(ActionEvent e) {
		if(un.getText().equals("darphe")&&pw.getText().equals("jesus")) {
			hide();
			bk.setVisible(true);
			lib.setVisible(true);
		}else if(un.getText().equals("carl")&&pw.getText().equals("admin")) {
			hide();
			addL.setVisible(true);
			editL.setVisible(true);
			adm.setVisible(true);
		}else if(un.getText().equals("jialei")&&pw.getText().equals("both")) {
			hide();
			bk.setVisible(true);
			addB.setVisible(true);
			adm.setVisible(true);
			addL.setVisible(true);
			editL.setVisible(true);
			addC.setVisible(true);
			
		}else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Login Error");
			alert.setContentText("Incorrect username and/or password..");
			alert.showAndWait();
		}
	}
	public void login(ActionEvent e) {
		// request focus
		Platform.runLater(new Runnable(){
		      @Override
		      public void run(){
		           un.requestFocus();
		      }
		});
		hide();
		show();
	}
	

	public void hide() {
		//un.setText("");
		//pw.setText("");
		lg.setVisible(false);
		lu.setVisible(false);
		un.setVisible(false);
		lp.setVisible(false);
		pw.setVisible(false);
		subm.setVisible(false);
		lib.setVisible(false);
		adm.setVisible(false);
		both.setVisible(false);
		bk.setVisible(false);
		addB.setVisible(false);
		addC.setVisible(false);
		addL.setVisible(false);
		editL.setVisible(false);
		welcome.setVisible(false);
		copyRight.setVisible(false);
		logoImage.setVisible(false);
		librarySystem.setVisible(false);
		remeberMe.setVisible(false);
		
	}
	
	public void show() {
		lu.setVisible(true);
		un.setVisible(true);
		lp.setVisible(true);
		pw.setVisible(true);
		subm.setVisible(true);
		headPic.setVisible(true);
		copyRight.setVisible(true);
		librarySystem.setVisible(true);
		remeberMe.setVisible(true);
	}
	
	public void refreshMemberList(ObservableList<LibraryMember> observableList) {
	    memberList.setItems(null);
	    memberList.setItems(observableList);
	}
	public void refreshBookList(ObservableList<Book> observableList) {
		bookList.setItems(null);
		bookList.setItems(observableList);
	}
	public void saveToStageManage(Stage stage) {
		 StageManageUtil.STAGE.put(FxController.MainMenuStage, stage);
         StageManageUtil.CONTROLLER.put(FxController.MainMenuController, this);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		Image im =new Image("/application/icon/logo.png");
		logoImage.setFill(new ImagePattern(im));
		logoImage.setEffect(new DropShadow(+25d,0d,+2d,Color.DARKSEAGREEN));
		
		//btn
        lg.setGraphic(new ImageView(new Image("/application/icon/login.png")));
        logout.setGraphic(new ImageView(new Image("/application/icon/logout.png")));
        //logout.setStyle("-fx-alignment:center;");
        logout.setContentDisplay(ContentDisplay.TOP);
        cls.setGraphic(new ImageView(new Image("/application/icon/close-circle.png")));
        subm.setGraphic(new ImageView(new Image("/application/icon/login.png")));
        bk.setGraphic(new ImageView(new Image("/application/icon/checkout.png")));
        editL.setGraphic(new ImageView(new Image("/application/icon/edit.png")));
        addB.setGraphic(new ImageView(new Image("/application/icon/Add-Book.png")));
        addC.setGraphic(new ImageView(new Image("/application/icon/add-bookshelf.png")));
        addL.setGraphic(new ImageView(new Image("/application/icon/add_member.png")));
        
        //label
        lu.setGraphic(new ImageView(new Image("/application/icon/user_name.png")));
        lp.setGraphic(new ImageView(new Image("/application/icon/password.png")));
        headPic.setGraphic(new ImageView(new Image("/application/icon/main.png")));
        librarySystem.setGraphic(new ImageView(new Image("/application/icon/book.png")));
		
		DropShadow dropshadow = new DropShadow();
        dropshadow.setRadius(10);
        dropshadow.setOffsetX(0);
        dropshadow.setOffsetY(0);
        dropshadow.setSpread(0.1);
        dropshadow.setColor(Color.GRAY);
        treeRoot.setEffect(dropshadow);//
        
//        remeberMe.selectedProperty().addListener(new ChangeListener<Boolean>() {
//            public void changed(ObservableValue<? extends Boolean> ov,
//              Boolean old_val, Boolean new_val) {
//            	if(remeberMe.isSelected()) {
//            		
//            	}else {
//            		
//            		
//            	}
//           }
//         });
        
        //initialize checkbox and username pwd
	    HashMap<String, User> userMap = DataAccessUtil.readUserMap();
	    List<User> userlist = new ArrayList<User>(userMap.values());
	    for(User u:userlist) {
	    	if(u.getRememberMe()) {
	    		un.setText(u.getId());
	    		pw.setText(u.getPassword());
	    		remeberMe.setSelected(true);
	    		break;
	    	}
	    }
	    
	    //initialize memberList to pane
		HashMap<String, LibraryMember> membermap = DataAccessUtil.readMemberMap();
		Collection<LibraryMember> memberCollection = membermap.values();
	    memberList.getItems().addAll(memberCollection);
//	    memberList.setCellFactory(new Callback<ListView<LibraryMember>, ListCell<LibraryMember>>() {
//            @Override
//            public ListCell<LibraryMember> call(ListView<LibraryMember> param) {
//                return new ListCell<LibraryMember>() {
//                	@Override
//        	    	protected void updateItem(LibraryMember item, boolean empty) {
//        	    		super.updateItem(item, empty);
//        	    		if (item == null || empty) {
//                            setText(null);
//                        } else {
//                            setText(item.toString());
//                            if(getIndex() % 2 == 1)
//                                setStyle("-fx-background-color: #c9c8c8");
//                            else
//                                setStyle("");
//                        }
//        	    	}
//                };
//            }
//
//        });
	    
	    //initialize bookList to pane
	    HashMap<String, Book> bookmap = DataAccessUtil.readBooksMap();
	    Collection<Book> bookCollection = bookmap.values();
	    bookList.getItems().addAll(bookCollection);
	    
	    
	    bookList.setCellFactory(new Callback<ListView<Book>, ListCell<Book>>() {
            @Override
            public ListCell<Book> call(ListView<Book> param) {
                return new ListCell<Book>() {
                	@Override
        	    	protected void updateItem(Book item, boolean empty) {
        	    		super.updateItem(item, empty);
        	    		if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.toString());
                            if(!item.isAvailable()) {
                            	setStyle("-fx-background-color: #ff6868;");
                            }else {
                            	setStyle("-fx-background-color: #a6ff8e;");
                            }
                        }
        	    	}
                };
            }

        });
	    
	    
	    //memberList item double click action
	    memberList.setOnMouseClicked(new EventHandler<MouseEvent>() {
	    	LibraryMember libmemberdou =null;
	        @Override
	        public void handle(MouseEvent click) {
	            if (click.getClickCount() == 2) {
	               ObservableList<LibraryMember> selectedIndices = memberList.getSelectionModel().getSelectedItems();
	               	for(LibraryMember o : selectedIndices){
	               		libmemberdou =o;
	               	}
	               	
		            FXMLLoader fxmlLoader = new FXMLLoader();
		            fxmlLoader.setLocation(getClass().getResource("LibraryMember.fxml"));
		            Parent root = null;
					try {
						root = fxmlLoader.load();
					} catch (IOException e) {
						e.printStackTrace();
					}
		            Scene scene = new Scene(root);
		            AddMemberController control = fxmlLoader.getController();
		            turnFlag = false;
		            if(currentAuth == Auth.LIBRARIAN) {
		            	turnFlag = true;
		            }
		            control.fileText(libmemberdou,turnFlag);
		            Stage stage = new Stage();
		            stage.setTitle("LibraryMember");
		            //StageManager
		            saveToStageManage(stage);
		            stage.setScene(scene);
		            stage.show();  
	            }
	        }
	    });
	    
	    //bookList item double click action
	    bookList.setOnMouseClicked(new EventHandler<MouseEvent>() {
	    	Book bookdou =null;
	    	@Override
	    	public void handle(MouseEvent click) {
	    		if (click.getClickCount() == 2) {
	    			ObservableList<Book> selectedIndices = bookList.getSelectionModel().getSelectedItems();
	    			for(Book o : selectedIndices){
	    				bookdou =o;
	    			}
	    			
	    			FXMLLoader fxmlLoader = new FXMLLoader();
	    			fxmlLoader.setLocation(getClass().getResource("/application/view/checkout/BookInfoDetail.fxml"));
	    			Parent root = null;
	    			try {
	    				root = fxmlLoader.load();
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}
	    			Scene scene = new Scene(root);
	    			BookInfoDetailController control = fxmlLoader.getController();
	    			control.setBook(bookdou);
	    			
	    			Stage stage = new Stage();
	    			stage.setTitle("BookInfo Detail");
	    			//StageManager
	    			saveToStageManage(stage);
	    			stage.setScene(scene);
	    			stage.show();  
	    		}
	    	}
	    });

	    logout.setOnAction(event -> {
	    	login(event);
	    	unavaliable.setVisible(false);
			unavaliableColor.setVisible(false);
			avaliable.setVisible(false);
			avaliableColor.setVisible(false);
	    	memberList.setVisible(false);
			bookList.setVisible(false);
			members.setVisible(false);
			books.setVisible(false);
			sepa1.setVisible(false);
			logout.setVisible(false);
			hintmember.setVisible(false);
			hintBook.setVisible(false);
			hintmemberLib.setVisible(false);
			headPic.setText("User");
	    });
		bk.setOnAction(event -> {
			try {
	            FXMLLoader fxmlLoader = new FXMLLoader();
	            fxmlLoader.setLocation(getClass().getResource("/application/view/checkout/CheckoutForLibMem.fxml"));
	            Scene scene = new Scene(fxmlLoader.load(),820,720);
	            Stage stage = new Stage();
	            //StageManager
	            StageManageUtil.STAGE.put(FxController.MainMenuStage, stage);
	            StageManageUtil.CONTROLLER.put(FxController.MainMenuController, this);
	            stage.setTitle("CheckoutForLibraryMember");
	            stage.setResizable(false);//window size immutable
	            stage.setScene(scene);
	            stage.show();
	         }
	         catch (IOException ea) {
	            ea.printStackTrace();
	         }
	    });
		
		addB.setOnAction(event -> {
			try {
	            FXMLLoader fxmlLoader = new FXMLLoader();
	            fxmlLoader.setLocation(getClass().getResource("/application/view/addbook/AddBook.fxml"));
	            Scene scene = new Scene(fxmlLoader.load(),550,480);
	            Stage stage = new Stage();
	            //StageManager
	            StageManageUtil.STAGE.put(FxController.MainMenuStage, stage);
	            StageManageUtil.CONTROLLER.put(FxController.MainMenuController, this);
	            stage.setTitle("Addbook");
	            stage.setResizable(false);//window size immutable
	            stage.setScene(scene);
	            stage.show();
	         }
	         catch (IOException ea) {
	            ea.printStackTrace();
	         }
		});
		addC.setOnAction(event -> {
			try {
				FXMLLoader fxmlLoader = new FXMLLoader();
				fxmlLoader.setLocation(getClass().getResource("/application/view/checkout/AddBookCopy.fxml"));
				Scene scene = new Scene(fxmlLoader.load(),820,720);
				Stage stage = new Stage();
				//StageManager
		        StageManageUtil.STAGE.put(FxController.MainMenuStage, stage);
		        StageManageUtil.CONTROLLER.put(FxController.MainMenuController, this);
				stage.setTitle("Add BookCopy");
				stage.setResizable(false);//window size immutable
				stage.setScene(scene);
				stage.show();
			}
			catch (IOException ea) {
				ea.printStackTrace();
			}
		});
		
	}
	
}
