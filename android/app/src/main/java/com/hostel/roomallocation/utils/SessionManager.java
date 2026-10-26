package com.hostel.roomallocation.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_STUDENT_ID = "studentId";
    private static final String KEY_STUDENT_NAME = "studentName";
    private static final String KEY_STUDENT_EMAIL = "studentEmail";
    private static final String KEY_STUDENT_PHONE = "studentPhone";
    private static final String KEY_ROOM_NUMBER = "roomNumber";
    private static final String KEY_BLOCK = "block";
    private static final String KEY_REMEMBER_ME = "rememberMe";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    /**
     * Creates login session with user details and saves dynamic student name
     */
    public void createLoginSession(String studentId, String name, String email, String roomNumber, String block) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_STUDENT_ID, studentId);
        editor.putString(KEY_STUDENT_NAME, name);
        editor.putString(KEY_STUDENT_EMAIL, email);
        editor.putString(KEY_ROOM_NUMBER, roomNumber != null ? roomNumber : "Room 304");
        editor.putString(KEY_BLOCK, block != null ? block : "Block B");
        editor.apply();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * Dynamically updates the user's name across the application
     */
    public void setStudentName(String name) {
        editor.putString(KEY_STUDENT_NAME, name);
        editor.apply();
    }

    /**
     * Gets the dynamic student name. NEVER returns "Alex Morgan".
     */
    public String getStudentName() {
        String name = pref.getString(KEY_STUDENT_NAME, "");
        if (name == null || name.trim().isEmpty()) {
            return "Student";
        }
        return name.trim();
    }

    /**
     * Computes 2-letter uppercase initials from the dynamic student name
     */
    public String getStudentInitials() {
        String name = getStudentName();
        if (name.isEmpty() || name.equalsIgnoreCase("Student")) {
            return "ST";
        }
        String[] parts = name.trim().split("\\s+");
        if (parts.length >= 2) {
            return ("" + parts[0].charAt(0) + parts[parts.length - 1].charAt(0)).toUpperCase();
        } else if (parts[0].length() >= 2) {
            return parts[0].substring(0, 2).toUpperCase();
        } else {
            return parts[0].toUpperCase();
        }
    }

    public String getStudentId() {
        return pref.getString(KEY_STUDENT_ID, "STU1024");
    }

    public void setStudentId(String studentId) {
        editor.putString(KEY_STUDENT_ID, studentId);
        editor.apply();
    }

    public String getStudentEmail() {
        return pref.getString(KEY_STUDENT_EMAIL, "student@college.edu");
    }

    public void setStudentEmail(String email) {
        editor.putString(KEY_STUDENT_EMAIL, email);
        editor.apply();
    }

    public String getStudentPhone() {
        return pref.getString(KEY_STUDENT_PHONE, "+91 98765 43210");
    }

    public void setStudentPhone(String phone) {
        editor.putString(KEY_STUDENT_PHONE, phone);
        editor.apply();
    }

    public String getRoomNumber() {
        return pref.getString(KEY_ROOM_NUMBER, "Room 304");
    }

    public String getBlock() {
        return pref.getString(KEY_BLOCK, "Block B");
    }

    public void setRememberMe(boolean remember) {
        editor.putBoolean(KEY_REMEMBER_ME, remember);
        editor.apply();
    }

    public boolean isRememberMe() {
        return pref.getBoolean(KEY_REMEMBER_ME, true);
    }

    public void logoutUser() {
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.apply();
    }
}
