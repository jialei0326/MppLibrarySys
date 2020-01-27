package application.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class LibraryUtil {
	
   public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH mm ss");
	
   public static LocalDateTime formatLocalDate(LocalDateTime localDateTime) {
	   String text = localDateTime.format(formatter);
	   return LocalDateTime.parse(text, formatter);
   }
	
	public static void alertChooseAMember() {
		Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Information Dialog");
    	alert.setHeaderText(null);
    	alert.setContentText("Please Choose A Member!");
    	alert.showAndWait();
	}
	public static void alertChooseAMemberIsEmpty() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("No Member To Choose!");
		alert.showAndWait();
	}
	public static void alertChooseABook() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Please Choose A Book!");
		alert.showAndWait();
	}
	public static void alertChooseABookIsEmpty() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("No Book To Choose!");
		alert.showAndWait();
	}
	public static void warningChooseABook() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Warning Dialog");
		alert.setHeaderText(null);
		alert.setContentText("This Book Is Unavailable, Please Choose Another One!");
		alert.showAndWait();
	}
	public static void alertNoCheckoutRecord() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("No Checkout Record!");
		alert.showAndWait();
	}
	public static void alertAddBookCopySuccess() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Add BookCopy Sucessfully!");
		alert.showAndWait();
	}
	public static void alertCheckoutBookSuccess() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Checkout Book Successfully!");
		alert.showAndWait();
	}
	
	
	public static void numberTextfield(TextField field) {
		field.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, 
		        String newValue) {
		        if (!newValue.matches("\\d*")) {
		        	field.setText(newValue.replaceAll("[^\\d]", ""));
		        }
		    }
		});
	}
}
