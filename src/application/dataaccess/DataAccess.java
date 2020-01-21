package application.dataaccess;

import java.util.HashMap;

import application.pojo.Book;
import application.pojo.LibraryMember;


public interface DataAccess { 
	public HashMap<String,Book> readBooksMap();
	public HashMap<String,User> readUserMap();
	public HashMap<String, LibraryMember> readMemberMap();
	public void saveNewMember(LibraryMember member); 
}
