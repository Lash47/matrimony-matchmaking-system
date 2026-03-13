package com.Matrimony_Matching_System;

//── MatchResult — stores a matched pair with their score ──────
public class MatchResult {

	private UserProfile person1;
	private UserProfile person2;
	private int score; // out of 100
	private String reason; // why they matched

	public MatchResult(UserProfile p1, UserProfile p2, int score, String reason) {
		this.person1 = p1;
		this.person2 = p2;
		this.score = score;
		this.reason = reason;
	}

	public UserProfile getPerson1() {
		return person1;
	}

	public UserProfile getPerson2() {
		return person2;
	}

	public int getScore() {
		return score;
	}

	public String getReason() {
		return reason;
	}

	// ── Display match result neatly ───────────────────────────
	public void displayMatch(int matchNumber) {
		System.out.println("\n  ╔══════════════════════════════════════╗");
		System.out.println("  ║         MATCH #" + matchNumber + "                     ║");
		System.out.println("  ╚══════════════════════════════════════╝");
		System.out.println("  ❤  " + person1.getName() + "  +  " + person2.getName());
		System.out.println("  Match Score : " + score + " / 100  " + getStars(score));
		System.out.println("  Reasons     : " + reason);
		System.out.println("  ----------------------------------------");
		System.out.println("  GROOM PROFILE:");
		person1.displayProfile();
		System.out.println("  ----------------------------------------");
		System.out.println("  BRIDE PROFILE:");
		person2.displayProfile();
		System.out.println("  ========================================");
	}

	// ── Star rating based on score ────────────────────────────
	private String getStars(int score) {
		if (score >= 90)
			return "★★★★★ Excellent";
		if (score >= 75)
			return "★★★★☆ Very Good";
		if (score >= 60)
			return "★★★☆☆ Good";
		if (score >= 45)
			return "★★☆☆☆ Average";
		return "★☆☆☆☆ Low";
	}
}