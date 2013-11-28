package model;

import java.io.Serializable;

public class Invite implements Serializable{
	
	private static final long serialVersionUID = -4514318087681238403L;

	private String issuing;
	
	private String receptor;
	
	private String status;
	
	public static final String ACCPETED = "accepted";
	public static final String PENDENT = "pendent";
	public static final String REFUSED = "refused";
	
	public Invite(String issuing, String recptor) {
		setIssuing(issuing);
		setReceptor(recptor);
		setStatus(PENDENT);
	}

	
	public String getIssuing() {
		return issuing;
	}


	public void setIssuing(String issuing) {
		this.issuing = issuing;
	}


	public String getReceptor() {
		return receptor;
	}


	public void setReceptor(String receptor) {
		this.receptor = receptor;
	}

	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public void accept() {
		setStatus(ACCPETED);
	}
	
	public void refuse() {
		setStatus(REFUSED);
	}
	
	
}
