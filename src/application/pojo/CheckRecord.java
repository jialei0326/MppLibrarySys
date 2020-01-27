package application.pojo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CheckRecord  implements Serializable {
	private static final long serialVersionUID = 8053182934959637139L;
	
	private LocalDateTime checkoutDate;
	private LocalDateTime dueDate;
	private BookCopy bookcopy;
	private LibraryMember libraryMember;
	
	public CheckRecord(LocalDateTime checkoutDate, BookCopy bookcopy) {
		this.checkoutDate = checkoutDate;
		this.bookcopy = bookcopy;
	}
	public LocalDateTime getCheckoutDate() {
		return checkoutDate;
	}
	public void setCheckoutDate(LocalDateTime checkoutDate) {
		this.checkoutDate = checkoutDate;
	}
	public LocalDateTime getDueDate() {
		return dueDate = checkoutDate.plusDays(bookcopy.getBook().getMaxCheckoutLength());
	}
	public void setDueDate(LocalDateTime dueDate) {
		this.dueDate = dueDate;
	}
	public BookCopy getBookcopy() {
		return bookcopy;
	}
	public void setBookcopy(BookCopy bookcopy) {
		this.bookcopy = bookcopy;
	}
	public LibraryMember getLibraryMember() {
		return libraryMember;
	}
	public void setLibraryMember(LibraryMember libraryMember) {
		this.libraryMember = libraryMember;
	}
	@Override
	public String toString() {
		return "[checkoutDate=" + checkoutDate + ", dueDate=" + dueDate + ", bookcopy=" + bookcopy + "]";
	}
	
	
}
