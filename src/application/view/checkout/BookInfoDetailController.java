package application.view.checkout;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.ResourceBundle;

import application.pojo.Book;
import application.pojo.BookCopy;
import application.pojo.LibraryMember;
import application.util.DataAccessUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BookInfoDetailController implements Initializable {
	
	@FXML
	private Pane treeRoot;
	@FXML
	private Label headLabel;
	@FXML
	private Button close;
	@FXML
	private TextArea bookInfo;
	
	public void setBook(Book book) {
		String textarea = "";
		String bookCopyInfo = "";
		
		// clear Textarea
		bookInfo.clear();
        for(BookCopy bc:book.getCopies()) {
        	LibraryMember libmem =null;
        	if(bc.getCheckRecord() != null) {
        		String libraryMemberId = bc.getCheckRecord().getLibraryMember().getMemberId();
        		HashMap<String, LibraryMember> map = DataAccessUtil.readMemberMap();
        		libmem = map.get(libraryMemberId);
        	}
        	
        	bookCopyInfo+="\n\t\tBookCopy number:"+bc.getCopyNum()+"--"+(bc.isAvailable() == true?"Avalicble; ":"Unavalicble; \n\t\t\t\t")+
        			(bc.getCheckRecord() == null ? "No Checkout Record!": "Checkout Record[ CheckoutDate: "+bc.getCheckRecord().getCheckoutDate()+
        					", DueDate: "+bc.getCheckRecord().getDueDate()+", IsOverDue: "+(bc.getCheckRecord().getDueDate().compareTo(LocalDateTime.now()) > 0?"No!":"Yes!")+" ]"+
        			"\n\t\t\t\tCheckOutMember[ MemberId: "+libmem.getMemberId()+", MemberName: "+libmem.getFirstName()+
        			" "+libmem.getLastName()+" ]");
        }
       
        textarea += "Book Info\n{\n\tISBN[ " + book.getIsbn()+" ],\n\tTitle[ "+book.getTitle()+" ],\n\tAuthors"+book.getAuthors().toString()+
        		",\n\tmaxCheckoutLength[ "+ book.getMaxCheckoutLength()+" ],\n\t"+ (book.isAvailable() == true?"Avalicble":"Unavalicble")+",\n"+
        		"\tBookCopy Info\n\t[ "+bookCopyInfo+"\n\t]\n}";
    	
    	bookInfo.setText(textarea);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		headLabel.setGraphic(new ImageView(new Image("/application/icon/record.png")));
		close.setGraphic(new ImageView(new Image("/application/icon/close.png")));
		
		DropShadow dropshadow = new DropShadow();
        dropshadow.setRadius(10);
        dropshadow.setOffsetX(0);
        dropshadow.setOffsetY(0);
        dropshadow.setSpread(0.1);
        dropshadow.setColor(Color.GRAY);
        treeRoot.setEffect(dropshadow);
		
        close.setOnAction(event -> {
        	Stage stage = (Stage)close.getScene().getWindow();
        	stage.close();
	    });
  			
		
	}
	
}
