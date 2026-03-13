package com.Matrimony_Matching_System;

//── UserProfile — stores all data about one person ────────────
public class UserProfile {

	private int id;
	private String name;
	private int age;
	private String gender; // "Male" or "Female"
	private double income; // annual income in rupees
	private String education; // "High School", "Graduate", "Post Graduate"
	private String religion;
	private String city;
	private boolean isMatched; // true once a match is found

	// ── Constructor ───────────────────────────────────────────
	public UserProfile(int id, String name, int age, String gender, double income, String education, String religion,
			String city) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.income = income;
		this.education = education;
		this.religion = religion;
		this.city = city;
		this.isMatched = false;
	}

	// ── Getters ───────────────────────────────────────────────
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public String getGender() {
		return gender;
	}

	public double getIncome() {
		return income;
	}

	public String getEducation() {
		return education;
	}

	public String getReligion() {
		return religion;
	}

	public String getCity() {
		return city;
	}

	public boolean isMatched() {
		return isMatched;
	}

	// ── Setter for match status ───────────────────────────────
	public void setMatched(boolean matched) {
		this.isMatched = matched;
	}

	// ── Display profile nicely ────────────────────────────────
	public void displayProfile() {
		System.out.println("  ID        : " + id);
		System.out.println("  Name      : " + name);
		System.out.println("  Age       : " + age);
		System.out.println("  Gender    : " + gender);
		System.out.println("  Income    : ₹" + String.format("%,.0f", income));
		System.out.println("  Education : " + education);
		System.out.println("  Religion  : " + religion);
		System.out.println("  City      : " + city);
	}
}