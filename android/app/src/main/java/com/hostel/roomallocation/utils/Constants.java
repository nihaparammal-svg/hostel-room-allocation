package com.hostel.roomallocation.utils;

public class Constants {
    // Android emulator connects to host PC localhost via 10.0.2.2
    public static final String BASE_URL = "http://10.0.2.2:3000";

    // Endpoints
    public static final String ENDPOINT_LOGIN = BASE_URL + "/login";
    public static final String ENDPOINT_REGISTER = BASE_URL + "/register";
    public static final String ENDPOINT_ROOMS = BASE_URL + "/rooms";
    public static final String ENDPOINT_APPLICATIONS = BASE_URL + "/applications";
    public static final String ENDPOINT_LEAVE = BASE_URL + "/leave";

    // SharedPreferences Name
    public static final String PREF_NAME = "HostelManagementPrefs";
}
