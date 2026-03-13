package com.Matrimony_Matching_System;

import java.util.ArrayList;
import java.util.List;

import com.Matrimony_Matching_System.MatchResult;

// ── MatchingEngine — the BRAIN of the system ─────────────────
// This is the "rule-based engine" mentioned in your resume
// It applies multiple rules and gives a score to each pair
public class MatchingEngine {

	// ── Education rank helper — converts degree to a number ──
	// Used to compare education levels between two people
	private int educationRank(String education) {
		switch (education) {
		case "Post Graduate":
			return 3;
		case "Graduate":
			return 2;
		case "High School":
			return 1;
		default:
			return 0;
		}
	}

	// ── MAIN METHOD — checks if two profiles match ────────────
	// Returns a MatchResult if they qualify, null if they don't
	public MatchResult checkMatch(UserProfile groom, UserProfile bride) {

		int score = 0;
		String reasons = "";

		// ── RULE 1: Gender must be opposite ───────────────────
		// Groom must be Male, Bride must be Female
		if (!groom.getGender().equals("Male") || !bride.getGender().equals("Female")) {
			return null; // hard reject — genders don't complement
		}

		// ── RULE 2: Age difference must be 0–7 years ──────────
		// Groom should be same age or older (traditional preference)
		int ageDiff = groom.getAge() - bride.getAge();
		if (ageDiff < 0 || ageDiff > 7) {
			return null; // hard reject — age gap too big or bride older
		}

		// Age scoring — closer age = higher score
		if (ageDiff <= 2) {
			score += 25;
			reasons += "Age gap perfect (" + ageDiff + " yrs); ";
		} else if (ageDiff <= 5) {
			score += 15;
			reasons += "Age gap acceptable (" + ageDiff + " yrs); ";
		} else {
			score += 5;
			reasons += "Age gap wide (" + ageDiff + " yrs); ";
		}

		// ── RULE 3: Income — groom must earn >= bride ─────────
		if (groom.getIncome() < bride.getIncome()) {
			return null; // hard reject
		}

		// Income scoring — higher groom income = better match
		double incomeRatio = groom.getIncome() / bride.getIncome();
		if (incomeRatio >= 2.0) {
			score += 25;
			reasons += "Groom income very strong; ";
		} else if (incomeRatio >= 1.5) {
			score += 20;
			reasons += "Groom income strong; ";
		} else {
			score += 10;
			reasons += "Income compatible; ";
		}

		// ── RULE 4: Education compatibility ───────────────────
		int groomEdu = educationRank(groom.getEducation());
		int brideEdu = educationRank(bride.getEducation());
		int eduDiff = Math.abs(groomEdu - brideEdu);

		if (eduDiff == 0) {
			score += 25;
			reasons += "Same education level; ";
		} else if (eduDiff == 1) {
			score += 15;
			reasons += "Compatible education; ";
		} else {
			score += 5;
			reasons += "Education gap exists; ";
		}

		// ── RULE 5: Religion bonus ─────────────────────────────
		if (groom.getReligion().equalsIgnoreCase(bride.getReligion())) {
			score += 15;
			reasons += "Same religion; ";
		} else {
			score += 5;
			reasons += "Different religion; ";
		}

		// ── RULE 6: Same city bonus ────────────────────────────
		if (groom.getCity().equalsIgnoreCase(bride.getCity())) {
			score += 10;
			reasons += "Same city (" + groom.getCity() + "); ";
		}

		// ── MINIMUM SCORE GATE: must score at least 50 ────────
		if (score < 50) {
			return null; // soft reject — not a good enough match
		}

		// Cap score at 100
		score = Math.min(score, 100);

		return new MatchResult(groom, bride, score, reasons.trim());
	}

	// ── FIND ALL MATCHES from two lists ───────────────────────
	// Loops every groom against every bride and collects matches
	public List<MatchResult> findAllMatches(List<UserProfile> grooms, List<UserProfile> brides) {
		List<MatchResult> matches = new ArrayList<>();

		for (UserProfile groom : grooms) {
			for (UserProfile bride : brides) {
				MatchResult result = checkMatch(groom, bride);
				if (result != null) {
					matches.add(result);
					groom.setMatched(true);
					bride.setMatched(true);
				}
			}
		}

		// Sort by score — highest first
		matches.sort((a, b) -> b.getScore() - a.getScore());

		return matches;
	}

	// ── FIND BEST MATCH for one specific person ────────────────
	public MatchResult findBestMatch(UserProfile person, List<UserProfile> candidates) {

		MatchResult best = null;

		for (UserProfile candidate : candidates) {

			// figure out who is groom and who is bride
			UserProfile groom = person.getGender().equals("Male") ? person : candidate;
			UserProfile bride = person.getGender().equals("Male") ? candidate : person;

			MatchResult result = checkMatch(groom, bride);

			if (result != null) {
				if (best == null || result.getScore() > best.getScore()) {
					best = result;
				}
			}
		}

		return best;
	}
}