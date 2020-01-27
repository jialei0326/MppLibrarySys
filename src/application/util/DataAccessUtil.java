package application.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import application.pojo.Book;
import application.pojo.CheckRecord;
import application.pojo.LibraryMember;
import application.pojo.User;



public class DataAccessUtil {
	
	enum StorageType {
		BOOKS, MEMBERS, USERS, CHECKOUTRECORD;
	}
	public static final String OUTPUT_DIR = System.getProperty("user.dir")
			+ "/src/application/dataaccess/storage";
	public static final String DATE_PATTERN = "MM/dd/yyyy";
	
	//implement: other save operations
	public static void saveNewMember(LibraryMember member) {
		HashMap<String, LibraryMember> mems = readMemberMap();
		String memberId = member.getMemberId();
		mems.put(memberId, member);
		saveToStorage(StorageType.MEMBERS, mems);	
	}
	
	public  static List<String> getMemberIdList() {
		HashMap<String, LibraryMember> mems = readMemberMap();
		
		List<String> list = new ArrayList<String>(mems.keySet());
		return list;
	}
	public static void updateMember(LibraryMember member) {
		HashMap<String, LibraryMember> mems = readMemberMap();
		mems.put(member.getMemberId(), member);
		saveToStorage(StorageType.MEMBERS, mems);
	}
	
	public static void saveNewBook(Book book) {
		HashMap<String, Book> books = readBooksMap();
		String bookId = book.getIsbn();
		books.put(bookId, book);
		saveToStorage(StorageType.BOOKS, books);	
	}
	
	public static void editBook(Book book) {
		HashMap<String, Book> mems = readBooksMap();
		mems.replace(book.getIsbn(), book);
		saveToStorage(StorageType.BOOKS, mems);	
	}
	public static void editUser(User user) {
		HashMap<String, User> mems = readUserMap();
		mems.replace(user.getId(), user);
		saveToStorage(StorageType.USERS, mems);	
	}
	
	
	@SuppressWarnings("unchecked")
	public  static HashMap<String,Book> readBooksMap() {
		HashMap<String, Book> map = new HashMap<String, Book>();
		if(readFromStorage(StorageType.BOOKS) != null) {
			map = (HashMap<String, Book>)  readFromStorage(StorageType.BOOKS);
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, LibraryMember> readMemberMap() {
		HashMap<String, LibraryMember> map = new HashMap<String, LibraryMember>();
		if(readFromStorage(StorageType.MEMBERS) != null) {
			map = (HashMap<String, LibraryMember>)  readFromStorage(StorageType.MEMBERS);
		}
		return map;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, User> readUserMap() {
		HashMap<String, User> map = new HashMap<String, User>();
		if(readFromStorage(StorageType.USERS) != null) {
			map = (HashMap<String, User>)  readFromStorage(StorageType.USERS);
		}
		return map;
	}
	
	public static void createNewCheckoutRecord(List<CheckRecord> list,String memberId) {
		HashMap<String, List<CheckRecord>> mems = readCheckOutRecordMap();
		mems.put(memberId, list);
		saveToStorage(StorageType.CHECKOUTRECORD, mems);	
	}
	public static void editCheckoutRecord(List<CheckRecord> list,String memberId) {
		HashMap<String, List<CheckRecord>> mems = readCheckOutRecordMap();
		mems.replace(memberId, list);
		saveToStorage(StorageType.CHECKOUTRECORD, mems);	
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, List<CheckRecord>> readCheckOutRecordMap() {
		HashMap<String, List<CheckRecord>> map = new HashMap<String, List<CheckRecord>>();
		if(readFromStorage(StorageType.CHECKOUTRECORD) != null) {
			map = (HashMap<String, List<CheckRecord>>)  readFromStorage(StorageType.CHECKOUTRECORD);
		}
		return map;
	}
	
	
	/////load methods - these place test data into the storage area
	///// - used just once at startup  
	//static void loadMemberMap(List<LibraryMember> memberList) {
		
	public static void loadBookMap(List<Book> bookList) {
		HashMap<String, Book> books = new HashMap<String, Book>();
		bookList.forEach(book -> books.put(book.getIsbn(), book));
		saveToStorage(StorageType.BOOKS, books);
	}
	public static void loadUserMap(List<User> userList) {
		HashMap<String, User> users = new HashMap<String, User>();
		userList.forEach(user -> users.put(user.getId(), user));
		saveToStorage(StorageType.USERS, users);
	}
 
	public static void loadMemberMap(List<LibraryMember> memberList) {
		HashMap<String, LibraryMember> members = new HashMap<String, LibraryMember>();
		memberList.forEach(member -> members.put(member.getMemberId(), member));
		saveToStorage(StorageType.MEMBERS, members);
	}
	
	public static void saveToStorage(StorageType type, Object ob) {
		ObjectOutputStream out = null;
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, type.toString());
			File myfile = new File(path.toString());
			FileOutputStream fis = new FileOutputStream(myfile);
        	out = new ObjectOutputStream(fis);
        	out.writeObject(ob);
    	    out.close();
            fis.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Object readFromStorage(StorageType type) {
		ObjectInputStream in = null;
		Object retVal = null;
		try {
			Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, type.toString());
			File myfile = new File(path.toString());
            FileInputStream fis = new FileInputStream(myfile);
            if(myfile.length() != 0){ 
        	   in = new ObjectInputStream(fis);
        	   retVal = in.readObject();  
        	   in.close();
            }
            fis.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}
	
}
