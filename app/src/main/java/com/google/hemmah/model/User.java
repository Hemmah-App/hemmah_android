package com.google.hemmah.model;

import com.google.hemmah.model.enums.UserType;

public class User {
	private String userName;
	private String email;
	private String password;
	private String phoneNumber;
	private String firstName;
	private String lastName;
	private UserType userType;

	public User(String userName, String email, String password, String phoneNumber, String firstName, String lastName, UserType userType) {
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userType = userType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		return "User{" +
				"userName='" + userName + '\'' +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", userType=" + userType +
				'}';
	}
}
