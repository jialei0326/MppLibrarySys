package application.view.addbook;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import application.exception.LibrarySystemException;
import application.pojo.Author;
import application.pojo.Book;
import application.util.DataAccessUtil;
import application.util.LibraryUtil;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AddBookController implements Initializable{

	@FXML
	private Pane treeRoot;
	@FXML
	private Label headLabel;
	@FXML
	private TextField inputIsbn;
	@FXML
	private TextField inputTitle;
	@FXML
	private TextField inputMaxCheckoutLength;
	@FXML
	private Button addAuthor;
	@FXML
	private Button addBook;
	
	public void openAddAuthorWindow() {
		try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/application/view/addbook/AddAuthorOfBook.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 660);
            Stage stage = new Stage();
            stage.setTitle("New Window");
            stage.setScene(scene);
            stage.show();
         }
         catch (IOException ea) {
            ea.printStackTrace();
         }
	}
	
	public void addBook() {
		Alert alert = new Alert(AlertType.INFORMATION);
		
		String isbn = inputIsbn.getText();
		String title = inputTitle.getText();
		List<Author> authorList = AddAuthorController.list;
		List<String> bookIds = new ArrayList<>();
		bookIds = allBookIds();
		if(isbn.isEmpty() || title.isEmpty() || inputMaxCheckoutLength.getText().isEmpty() || authorList.isEmpty()) {
			alert.setContentText("All fileds must be filled!");
			alert.show();
		}else {
			if(!bookIds.contains(isbn)) {
				int maxCheckoutLength = Integer.parseInt(inputMaxCheckoutLength.getText());
				Book newBook = new Book(isbn, title, maxCheckoutLength, authorList);
				DataAccessUtil.saveNewBook(newBook);
				inputIsbn.clear();
				inputTitle.clear();
				inputMaxCheckoutLength.clear();
				authorList.clear();
				alert.setContentText("Add book successfully!");
				alert.showAndWait();
				
				//refresh main window booklist
				LibraryUtil.refreshMainWinBookList();
				
				
			}else {
				new LibrarySystemException("This book is already exist!");
				alert.setContentText("This book is already exist!");
				alert.showAndWait();
			}
		}
		
	}
	
	public List<String> allBookIds() {
		List<String> retval = new ArrayList<>();
		retval.addAll(DataAccessUtil.readBooksMap().keySet());
		return retval;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		headLabel.setGraphic(new ImageView(new Image("/application/icon/Add-Book.png")));
		addAuthor.setGraphic(new ImageView(new Image("/application/icon/author.png")));
		addBook.setGraphic(new ImageView(new Image("/application/icon/add.png")));
		
		DropShadow dropshadow = new DropShadow();
        dropshadow.setRadius(10);
        dropshadow.setOffsetX(0);
        dropshadow.setOffsetY(0);
        dropshadow.setSpread(0.1);
        dropshadow.setColor(Color.GRAY);
        treeRoot.setEffect(dropshadow);
	}
	
}
