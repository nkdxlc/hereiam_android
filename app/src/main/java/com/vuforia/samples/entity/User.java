package com.vuforia.samples.entity;

import java.io.Serializable;

public class User implements Serializable {
	
	private int userid;
	private String username;
	private String password;
	private int occupation;
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getOccupation() {
		return occupation;
	}
	public void setOccupation(int occupation) {
		this.occupation = occupation;
	}
	

	public String toString(){
		return "User [userid=" + userid + ", username=" + username
				+ ", password=" + password + ", occupation=" + occupation + "]";
	}


}
