package com.google.hemmah.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.hemmah.domain.model.enums.UserType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


public class User implements Parcelable{
	private String userName;
	private String email;
	private String password;
	private String phoneNumber;
	private String firstName;
	private String lastName;
	private UserType userType;
	private String language;

	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public User(String userName, String email, String password, String phoneNumber, String firstName, String lastName, UserType userType) {
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userType = userType;
	}

	public User(String userName, String email, String password, String phoneNumber, String firstName, String lastName) {
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public User(String userName, String email, String password, String phoneNumber, String firstName, String lastName, UserType userType, String language) {
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userType = userType;
		this.language = language;
	}

	public User(String email, String phoneNumber, String firstName, String lastName, String language) {
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.firstName = firstName;
		this.lastName = lastName;
		this.language = language;
	}

	public String getLanguage() {
		return language;
	}

	public String getUserName() {
		return userName;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	protected User(Parcel in) {
		userName = in.readString();
		email = in.readString();
		password = in.readString();
		phoneNumber = in.readString();
		firstName = in.readString();
		lastName = in.readString();
		userType = UserType.valueOf(in.readString());
		language = in.readString();
	}
	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		@Override
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(userName);
		dest.writeString(email);
		dest.writeString(password);
		dest.writeString(phoneNumber);
		dest.writeString(firstName);
		dest.writeString(lastName);
		dest.writeString(userType.name());
		dest.writeString(language);
	}
}
