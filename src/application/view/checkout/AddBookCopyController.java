package application.view.checkout;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import application.pojo.Book;
import application.pojo.BookCopy;
import application.pojo.LibraryMember;
import application.util.DataAccessUtil;
import application.util.LibraryUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class AddBookCopyController implements Initializable {
	
	@FXML
	private Pane treeRoot;
	@FXML
	private Label headLabel;
	@FXML
	private ListView<Book> bookList;
	@FXML
	private Button addBookCopy;
	@FXML
	private Button checkBookInfo;
	@FXML
	private TextArea bookInfo;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		headLabel.setGraphic(new ImageView(new Image("/application/icon/add-bookshelf.png")));
		addBookCopy.setGraphic(new ImageView(new Image("/application/icon/add.png")));
		checkBookInfo.setGraphic(new ImageView(new Image("/application/icon/info.png")));
		
		DropShadow dropshadow = new DropShadow();
        dropshadow.setRadius(10);
        dropshadow.setOffsetX(0);
        dropshadow.setOffsetY(0);
        dropshadow.setSpread(0.1);
        dropshadow.setColor(Color.GRAY);
        treeRoot.setEffect(dropshadow);
		
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
	    
		//addBookCopy click action
	    addBookCopy.setOnAction(event -> {
	    	Book book = null;
	    	String bookCopyInfo ="";
			//No book to Choose
			if(bookList.getItems().size() == 0) {
				LibraryUtil.alertChooseABookIsEmpty();
				return;
			}
			// clear Textarea
  			bookInfo.clear();
			
			ObservableList<Book> selectedIndices = bookList.getSelectionModel().getSelectedItems();
			if(selectedIndices.isEmpty()) {
				LibraryUtil.alertChooseABook();
				return;
			}else {
				for(Book o : selectedIndices){
					book =o;
				}
			}
			book.addCopy();
			DataAccessUtil.editBook(book);
			
			//refresh bookList status
			bookList.setItems(null);
			HashMap<String, Book> bookmap22 = DataAccessUtil.readBooksMap();
			List<Book> result2 = new ArrayList<Book>(bookmap22.values());
			ObservableList<Book> observableList = FXCollections.observableList(result2);
			bookList.setItems(observableList);
			
			//alert
			LibraryUtil.alertAddBookCopySuccess();
			
			String text = "Book[ ISBN: "+book.getIsbn()+", Title: "+book.getTitle()+" ] Add BookCopy Sucessfully!\n";
			//add bookInfo
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
  	       
			text += "Book Info\n{\n\tISBN[ " + book.getIsbn()+" ],\n\tTitle[ "+book.getTitle()+" ],\n\tAuthors"+book.getAuthors().toString()+
  	        		",\n\tmaxCheckoutLength[ "+ book.getMaxCheckoutLength()+" ],\n\t"+ (book.isAvailable() == true?"Avalicble":"Unavalicble")+",\n"+
  	        		"\tBookCopy Info\n\t[ "+bookCopyInfo+"\n\t]\n}";
			
        	bookInfo.setText(text);
        	
        	//refresh main window booklist
			LibraryUtil.refreshMainWinBookList();
        	
		});
	    
	    //checkBook click action
	    checkBookInfo.setOnAction(event -> {
	    	Book book = null;
  			String textarea = "";
  			String bookCopyInfo = "";
  			//No book to Choose
  			if(bookList.getItems().size() == 0) {
  				LibraryUtil.alertChooseAMemberIsEmpty();
  				return;
  			}
  			// clear Textarea
  			bookInfo.clear();
  			
  			ObservableList<Book> selectedIndices = bookList.getSelectionModel().getSelectedItems();
  	        if(selectedIndices.isEmpty()) {
  	        	LibraryUtil.alertChooseABook();
  	        	return;
  	        }else {
  	        	for(Book o : selectedIndices){
  	        		book = o;
  	        	}
  	        }
  	        
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
  			
  			
  		});
	  	    
		
		
	}
	
}
