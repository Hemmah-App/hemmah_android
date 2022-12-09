package com.google.hemmah.model;

import com.google.gson.annotations.SerializedName;

public class User{
	public User(String firstName, String lastName, String password, String phoneNumber,  String userType, String userName, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.userType = userType;
		this.userName = userName;
	}

	@SerializedName("firstName")
	private String firstName;

	@SerializedName("lastName")
	private String lastName;

	@SerializedName("password")
	private String password;

	@SerializedName("phoneNumber")
	private String phoneNumber;

	@SerializedName("address")
	private String address;

	@SerializedName("userType")
	private String userType;

	@SerializedName("userName")
	private String userName;



	public String getFirstName(){
		return firstName;
	}

	public String getLastName(){
		return lastName;
	}

	public String getPassword(){
		return password;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}

	public String getUserType(){
		return userType;
	}

	public String getUserName(){
		return userName;
	}
}