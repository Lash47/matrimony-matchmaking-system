package com.Matrimony_Matching_System;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.Matrimony_Matching_System.MatchResult;

// ── MatrimonyApp — Console Entry Point ────────────────────────
public class MatrimonyApp {

	// All registered profiles
	static List<UserProfile> groomProfiles = new ArrayList<>();
	static List<UserProfile> brideProfiles = new ArrayList<>();
	static MatchingEngine engine = new MatchingEngine();
	static Scanner scanner = new Scanner(System.in);
	static int nextId = 1;

	public static void main(String[] args) {

		System.out.println("╔══════════════════════════════════════════════╗");
		System.out.println("║     MATRIMONY MATCHMAKING SYSTEM  v1.0       ║");
		System.out.println("║     Built by Ashish Kommula                  ║");
		System.out.println("╚══════════════════════════════════════════════╝");

		// Load sample profiles so system has data to work with
		loadSampleProfiles();

		boolean running = true;
		while (running) {
			printMenu();
			int choice = readInt("Enter choice: ");

			switch (choice) {
			case 1:
				addNewProfile();
				break;
			case 2:
				viewAllProfiles();
				break;
			case 3:
				runMatchingEngine();
				break;
			case 4:
				searchByName();
				break;
			case 5:
				filterByAge();
				break;
			case 6:
				filterByEducation();
				break;
			case 7:
				filterByIncome();
				break;
			case 8:
				running = false;
				System.out.println("\n Thank you for using Matrimony System. Goodbye!");
				break;
			default:
				System.out.println(" Invalid choice. Try again.");
			}
		}
		scanner.close();
	}

	// ── MENU ─────────────────────────────────────────────────
	static void printMenu() {
		System.out.println("\n╔══════════════════════════╗");
		System.out.println("║        MAIN MENU         ║");
		System.out.println("╠══════════════════════════╣");
		System.out.println("║  1. Add New Profile      ║");
		System.out.println("║  2. View All Profiles    ║");
		System.out.println("║  3. Run Matching Engine  ║");
		System.out.println("║  4. Search by Name       ║");
		System.out.println("║  5. Filter by Age        ║");
		System.out.println("║  6. Filter by Education  ║");
		System.out.println("║  7. Filter by Income     ║");
		System.out.println("║  8. Exit                 ║");
		System.out.println("╚══════════════════════════╝");
	}

	// ── 1. ADD NEW PROFILE ────────────────────────────────────
	static void addNewProfile() {
		System.out.println("\n===== ADD NEW PROFILE =====");

		System.out.print("  Name       : ");
		String name = scanner.nextLine().trim();

		int age = readInt("  Age        : ");

		System.out.print("  Gender (Male/Female) : ");
		String gender = scanner.nextLine().trim();

		double income = readDouble("  Annual Income (₹) : ");

		System.out.println("  Education options: High School / Graduate / Post Graduate");
		System.out.print("  Education  : ");
		String edu = scanner.nextLine().trim();

		System.out.print("  Religion   : ");
		String religion = scanner.nextLine().trim();

		System.out.print("  City       : ");
		String city = scanner.nextLine().trim();

		// Validate age
		if (age < 18 || age > 60) {
			System.out.println(" Age must be between 18 and 60. Profile not added.");
			return;
		}

		UserProfile profile = new UserProfile(nextId++, name, age, gender, income, edu, religion, city);

		if (gender.equalsIgnoreCase("Male")) {
			groomProfiles.add(profile);
		} else if (gender.equalsIgnoreCase("Female")) {
			brideProfiles.add(profile);
		} else {
			System.out.println(" Invalid gender. Profile not added.");
			return;
		}

		System.out.println("\n Profile added successfully! ID = " + profile.getId());
	}

	// ── 2. VIEW ALL PROFILES ──────────────────────────────────
	static void viewAllProfiles() {
		System.out.println("\n===== ALL GROOM PROFILES (" + groomProfiles.size() + ") =====");
		if (groomProfiles.isEmpty()) {
			System.out.println("  No grooms registered.");
		} else {
			for (UserProfile p : groomProfiles) {
				System.out.println("\n  ----------------------------------------");
				p.displayProfile();
				System.out.println("  Matched: " + (p.isMatched() ? "Yes" : "No"));
			}
		}

		System.out.println("\n===== ALL BRIDE PROFILES (" + brideProfiles.size() + ") =====");
		if (brideProfiles.isEmpty()) {
			System.out.println("  No brides registered.");
		} else {
			for (UserProfile p : brideProfiles) {
				System.out.println("\n  ----------------------------------------");
				p.displayProfile();
				System.out.println("  Matched: " + (p.isMatched() ? "Yes" : "No"));
			}
		}
	}

	// ── 3. RUN MATCHING ENGINE ────────────────────────────────
	static void runMatchingEngine() {
		System.out.println("\n===== RUNNING MATCHING ENGINE =====");
		System.out
				.println("  Checking " + groomProfiles.size() + " grooms vs " + brideProfiles.size() + " brides...\n");

		List<MatchResult> matches = engine.findAllMatches(groomProfiles, brideProfiles);

		if (matches.isEmpty()) {
			System.out.println("  No compatible matches found.");
			System.out.println("  Tip: Add more profiles or relax criteria.");
			return;
		}

		System.out.println("  Found " + matches.size() + " match(es)! Sorted by score:\n");

		int count = 1;
		for (MatchResult match : matches) {
			match.displayMatch(count++);
		}
	}

	// ── 4. SEARCH BY NAME ────────────────────────────────────
	static void searchByName() {
		System.out.print("\n  Enter name to search: ");
		String keyword = scanner.nextLine().trim().toLowerCase();

		System.out.println("\n  Search results for \"" + keyword + "\":");
		boolean found = false;

		// Search in both lists
		List<UserProfile> all = new ArrayList<>();
		all.addAll(groomProfiles);
		all.addAll(brideProfiles);

		for (UserProfile p : all) {
			if (p.getName().toLowerCase().contains(keyword)) {
				System.out.println("\n  ----------------------------------------");
				p.displayProfile();

				// Find best match for this person
				List<UserProfile> opposite = p.getGender().equals("Male") ? brideProfiles : groomProfiles;
				MatchResult best = engine.findBestMatch(p, opposite);

				if (best != null) {
					System.out.println("  Best Match: "
							+ (p.getGender().equals("Male") ? best.getPerson2().getName() : best.getPerson1().getName())
							+ " (Score: " + best.getScore() + ")");
				} else {
					System.out.println("  Best Match: No suitable match found yet.");
				}

				found = true;
			}
		}

		if (!found) {
			System.out.println("  No profile found with that name.");
		}
	}

	// ── 5. FILTER BY AGE ─────────────────────────────────────
	static void filterByAge() {
		int minAge = readInt("\n  Min Age: ");
		int maxAge = readInt("  Max Age: ");

		System.out.println("\n  Profiles aged " + minAge + " to " + maxAge + ":");
		boolean found = false;

		List<UserProfile> all = new ArrayList<>();
		all.addAll(groomProfiles);
		all.addAll(brideProfiles);

		for (UserProfile p : all) {
			if (p.getAge() >= minAge && p.getAge() <= maxAge) {
				System.out.println(
						"  -> " + p.getName() + " | Age: " + p.getAge() + " | " + p.getGender() + " | " + p.getCity());
				found = true;
			}
		}

		if (!found)
			System.out.println("  No profiles in this age range.");
	}

	// ── 6. FILTER BY EDUCATION ───────────────────────────────
	static void filterByEducation() {
		System.out.println("\n  Select education level:");
		System.out.println("  1. High School");
		System.out.println("  2. Graduate");
		System.out.println("  3. Post Graduate");
		int choice = readInt("  Choice: ");

		String level;
		switch (choice) {
		case 1:
			level = "High School";
			break;
		case 2:
			level = "Graduate";
			break;
		case 3:
			level = "Post Graduate";
			break;
		default:
			System.out.println("  Invalid.");
			return;
		}

		System.out.println("\n  Profiles with education: " + level);
		boolean found = false;

		List<UserProfile> all = new ArrayList<>();
		all.addAll(groomProfiles);
		all.addAll(brideProfiles);

		for (UserProfile p : all) {
			if (p.getEducation().equalsIgnoreCase(level)) {
				System.out.println("  -> " + p.getName() + " | " + p.getGender() + " | Age: " + p.getAge() + " | ₹"
						+ String.format("%,.0f", p.getIncome()));
				found = true;
			}
		}

		if (!found)
			System.out.println("  No profiles with this education.");
	}

	// ── 7. FILTER BY INCOME ──────────────────────────────────
	static void filterByIncome() {
		double minIncome = readDouble("\n  Min Income (₹): ");
		double maxIncome = readDouble("  Max Income (₹): ");

		System.out.println("\n  Profiles with income ₹" + String.format("%,.0f", minIncome) + " to ₹"
				+ String.format("%,.0f", maxIncome) + ":");
		boolean found = false;

		List<UserProfile> all = new ArrayList<>();
		all.addAll(groomProfiles);
		all.addAll(brideProfiles);

		for (UserProfile p : all) {
			if (p.getIncome() >= minIncome && p.getIncome() <= maxIncome) {
				System.out.println("  -> " + p.getName() + " | " + p.getGender() + " | Age: " + p.getAge()
						+ " | Income: ₹" + String.format("%,.0f", p.getIncome()) + " | " + p.getEducation());
				found = true;
			}
		}

		if (!found)
			System.out.println("  No profiles in this income range.");
	}

	// ── LOAD SAMPLE DATA ─────────────────────────────────────
	// 6 grooms + 6 brides with varied data to demonstrate matching
	static void loadSampleProfiles() {

		// id, name, age, gender, income, education, religion, city
		groomProfiles.add(
				new UserProfile(nextId++, "Rahul Sharma", 28, "Male", 700000, "Post Graduate", "Hindu", "Hyderabad"));
		groomProfiles
				.add(new UserProfile(nextId++, "Kiran Kumar", 30, "Male", 500000, "Graduate", "Hindu", "Bangalore"));
		groomProfiles.add(
				new UserProfile(nextId++, "Mohammed Asif", 27, "Male", 600000, "Post Graduate", "Muslim", "Hyderabad"));
		groomProfiles.add(
				new UserProfile(nextId++, "David Thomas", 32, "Male", 900000, "Post Graduate", "Christian", "Chennai"));
		groomProfiles.add(new UserProfile(nextId++, "Arun Reddy", 26, "Male", 400000, "Graduate", "Hindu", "Vizag"));
		groomProfiles.add(
				new UserProfile(nextId++, "Suresh Babu", 35, "Male", 1200000, "Post Graduate", "Hindu", "Hyderabad"));

		brideProfiles.add(
				new UserProfile(nextId++, "Priya Patel", 25, "Female", 400000, "Post Graduate", "Hindu", "Hyderabad"));
		brideProfiles
				.add(new UserProfile(nextId++, "Ananya Singh", 27, "Female", 350000, "Graduate", "Hindu", "Bangalore"));
		brideProfiles.add(
				new UserProfile(nextId++, "Fatima Khan", 24, "Female", 300000, "Post Graduate", "Muslim", "Hyderabad"));
		brideProfiles.add(new UserProfile(nextId++, "Mary Fernandez", 29, "Female", 500000, "Post Graduate",
				"Christian", "Chennai"));
		brideProfiles
				.add(new UserProfile(nextId++, "Deepika Reddy", 24, "Female", 280000, "Graduate", "Hindu", "Vizag"));
		brideProfiles.add(
				new UserProfile(nextId++, "Sneha Rao", 30, "Female", 600000, "Post Graduate", "Hindu", "Hyderabad"));

		System.out.println("\n  Loaded " + groomProfiles.size() + " grooms and " + brideProfiles.size() + " brides.");
	}

	// ── UTILITY: safe integer input ───────────────────────────
	static int readInt(String prompt) {
		while (true) {
			System.out.print(prompt);
			try {
				int val = Integer.parseInt(scanner.nextLine().trim());
				return val;
			} catch (NumberFormatException e) {
				System.out.println("  Please enter a valid number.");
			}
		}
	}

	// ── UTILITY: safe double input ────────────────────────────
	static double readDouble(String prompt) {
		while (true) {
			System.out.print(prompt);
			try {
				double val = Double.parseDouble(scanner.nextLine().trim());
				return val;
			} catch (NumberFormatException e) {
				System.out.println("  Please enter a valid number.");
			}
		}
	}
}