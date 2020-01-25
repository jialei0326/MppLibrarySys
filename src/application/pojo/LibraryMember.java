package application.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

final public class LibraryMember extends Person implements Serializable {
	private static final long serialVersionUID = -2226197306790714013L;
	private String memberId;
	private List<CheckRecord> list;
	
	public LibraryMember(String memberId, String fname, String lname, String tel,Address add) {
		super(fname,lname, tel, add);
		this.memberId = memberId;
		this.list = new ArrayList<CheckRecord>();
	}
	
	
	public String getMemberId() {
		return memberId;
	}
	
	public void addCheckoutRecordList(CheckRecord checkRecord) {
		list.add(checkRecord);
	}
	
	
	@Override
	public String toString() {
		return "Member Info: " + "ID: " + memberId + ", name: " + getFirstName() + " " + getLastName() + 
				", " + getTelephone() + " " + getAddress();
	}

}
