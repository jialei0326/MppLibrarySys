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
import application.pojo.CheckRecord;
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
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class CheckoutForLirMemController implements Initializable {
	
	@FXML
	private Pane treeRoot;
	@FXML
	private ListView<LibraryMember> memberList;
	@FXML
	private ListView<Book> bookList;
	@FXML
	private Button chooseMember;
	@FXML
	private Button chooseBook;
	@FXML
	private Button checkout;
	@FXML
	private Button checkoutRedord;
	@FXML
	private TextField memberName;
	@FXML
	private TextField bookName;
	@FXML
	private TextArea checkoutRecordInfo;
	@FXML
	private Label headLabel;
	
	private LibraryMember member;
	private Book book;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		headLabel.setGraphic(new ImageView(new Image("/application/icon/checkout.png")));
		checkoutRedord.setGraphic(new ImageView(new Image("/application/icon/record.png")));
		checkout.setGraphic(new ImageView(new Image("/application/icon/checkout.png")));
		chooseMember.setGraphic(new ImageView(new Image("/application/icon/choose.png")));
		chooseBook.setGraphic(new ImageView(new Image("/application/icon/choose.png")));
		
		DropShadow dropshadow = new DropShadow();
        dropshadow.setRadius(10);
        dropshadow.setOffsetX(0);
        dropshadow.setOffsetY(0);
        dropshadow.setSpread(0.1);
        dropshadow.setColor(Color.GRAY);
        treeRoot.setEffect(dropshadow);
		
		//set textfield uneditable
		memberName.setEditable(false);
		bookName.setEditable(false);
		checkoutRecordInfo.setEditable(false);
		
		//initialize memberList to pane
		HashMap<String, LibraryMember> membermap = DataAccessUtil.readMemberMap();
		Collection<LibraryMember> memberCollection = membermap.values();
	    memberList.getItems().addAll(memberCollection);
	    
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
	    
	    
	    //chooseMember click action
		chooseMember.setOnAction(event -> {
			//No Member to Choose
			if(memberList.getItems().size() == 0) {
				LibraryUtil.alertChooseAMemberIsEmpty();
				return;
			}
			
	        ObservableList<LibraryMember> selectedIndices = memberList.getSelectionModel().getSelectedItems();
	        if(selectedIndices.isEmpty()) {
	        	LibraryUtil.alertChooseAMember();
	        }else {
	        	for(LibraryMember o : selectedIndices){
	        		this.memberName.setText("MemberID:"+o.getMemberId()+";MemberName:"+ o.getFirstName() + " " + o.getLastName());
	        		member = o;
	        	}
	        }
	    });
		
		//chooseBook click action
		chooseBook.setOnAction(event -> {
			//No book to Choose
			if(bookList.getItems().size() == 0) {
				LibraryUtil.alertChooseABookIsEmpty();
				return;
			}
			
			ObservableList<Book> selectedIndices = bookList.getSelectionModel().getSelectedItems();
			if(selectedIndices.isEmpty()) {
				LibraryUtil.alertChooseABook();
			}else {
				for(Book o : selectedIndices){
					if(!o.isAvailable()) {
						LibraryUtil.warningChooseABook();
					}else {
						this.bookName.setText("BookISBN:"+o.getIsbn()+";BookTitle:"+o.getTitle());
						book = o;
					}
				}
			}
		});
		
		//checkout click action
		checkout.setOnAction(event -> {
			//No Member to Choose
			if(memberList.getItems().size() == 0) {
				LibraryUtil.alertChooseAMemberIsEmpty();
				return;
			}
			//No book to Choose
			if(bookList.getItems().size() == 0) {
				LibraryUtil.alertChooseABookIsEmpty();
				return;
			}
			//choose book and member are not empty
			if(member == null) {
				LibraryUtil.alertChooseAMember();
				return;
			}
			if(book == null) {
				LibraryUtil.alertChooseABook();
				return;
			}
			
			
			
			HashMap<String, List<CheckRecord>> recordmap = DataAccessUtil.readCheckOutRecordMap();
			LocalDateTime now = LocalDateTime.now();
			BookCopy bookcopy = book.getNextAvailableCopy();
			CheckRecord newCheckRecord = new CheckRecord(LibraryUtil.formatLocalDate(now), bookcopy);
			newCheckRecord.setLibraryMember(member);
			
			//set next bookcopy is unavailable
			//write into database
			HashMap<String, Book> oldbookmap =DataAccessUtil.readBooksMap();
			Book newbook = oldbookmap.get(book.getIsbn());
			BookCopy[] copys = newbook.getCopies();
			for(int i =0;i<copys.length;i++) {
				if(copys[i].getCopyNum() == bookcopy.getCopyNum()) {
					copys[i].changeAvailability();
					copys[i].setCheckRecord(newCheckRecord);
					break;
				}
			}
			DataAccessUtil.editBook(newbook);
			
			//write CheckRecord to database
			if(recordmap.isEmpty() || !recordmap.containsKey(member.getMemberId())) {
				List<CheckRecord> list = new ArrayList<CheckRecord>();
				list.add(newCheckRecord);
				DataAccessUtil.createNewCheckoutRecord(list, member.getMemberId());
			}else {
				List<CheckRecord> oldlist = recordmap.get(member.getMemberId());
				oldlist.add(newCheckRecord);
				DataAccessUtil.createNewCheckoutRecord(oldlist, member.getMemberId());
			}
			
			//set Textarea checkouRecordInfo
			String text = "Checkout Book Successfully!";
			text += "Member[ ID:" +member.getMemberId() +", Name:"+member.getFirstName()+" "+member.getLastName()+
					" ] Checkout Book[ ISBN:" + book.getIsbn()+", Title:"+book.getTitle()+" ]\n"+
					"CheckouDate[ "+newCheckRecord.getCheckoutDate()+" ], DueDate[ "+ newCheckRecord.getDueDate()+" ]";
			checkoutRecordInfo.setText(text);
			
			//refresh bookList status
			bookList.setItems(null);
			HashMap<String, Book> bookmap22 = DataAccessUtil.readBooksMap();
			List<Book> result2 = new ArrayList<Book>(bookmap22.values());
			ObservableList<Book> observableList = FXCollections.observableList(result2);
			bookList.setItems(observableList);
			
			LibraryUtil.alertCheckoutBookSuccess();
			//clear choose book and member
			member = null;
			book = null;
			
			//clear  memberName and bookName
			memberName.clear();
			bookName.clear();
			
			//refresh main window booklist
			LibraryUtil.refreshMainWinBookList();
			
		});
		
		
		//checkoutRedord click action
		checkoutRedord.setOnAction(event -> {
			LibraryMember libmember = null;
			String textarea = "";
			//No Member to Choose
			if(memberList.getItems().size() == 0) {
				LibraryUtil.alertChooseAMemberIsEmpty();
				return;
			}
			// clear Textarea
			checkoutRecordInfo.clear();
			
			HashMap<String, List<CheckRecord>>  map = DataAccessUtil.readCheckOutRecordMap();
			
			ObservableList<LibraryMember> selectedIndices = memberList.getSelectionModel().getSelectedItems();
	        if(selectedIndices.isEmpty()) {
	        	LibraryUtil.alertChooseAMember();
	        	return;
	        }else {
	        	for(LibraryMember o : selectedIndices){
	        		libmember = o;
	        	}
	        }
	       
	        //check if this member has checkout record
	        if(map.containsKey(libmember.getMemberId())) {
	        	List<CheckRecord> listr = map.get(libmember.getMemberId());
	        	textarea += "Member[ ID:" +libmember.getMemberId() +", Name:"+libmember.getFirstName()+" "+libmember.getLastName()+" ]\n";
	        	for(int i =0;i<listr.size();i++) {
	        		textarea += "Record "+(i+1)+":\n{\n";
	        		textarea += "\tCheckout Book[ ISBN:" + listr.get(i).getBookcopy().getBook().getIsbn()+", Title:"+listr.get(i).getBookcopy().getBook().getTitle()+" ]\n"+
	        				"\tBookCopy[ copy number:"+listr.get(i).getBookcopy().getCopyNum()+" ]\n"+
	        				"\tCheckouDate[ "+listr.get(i).getCheckoutDate()+" ], DueDate[ "+ listr.get(i).getDueDate()+" ]\n"+
	        				"\tIsOverDue[ "+(listr.get(i).getDueDate().compareTo(LocalDateTime.now()) > 0?"No!":"Yes!")+ " ]\n}\n";
	        	}
	        }else {
	        	LibraryUtil.alertNoCheckoutRecord();
	        	textarea += "\tNo Checkout Record";
	        }
			checkoutRecordInfo.setText(textarea);
			
			
		});
	    
		
		
	}
	
}
