package com.google.hemmah.Utils;

public class Constants {
    public static final String BASE_URL = "https://api.hemmah.live";

    public static final String STOMP_API = "wss://api.hemmah.live/ws";
    public static final String DISABLED_SEND_TOPIC = "/app/help_call/ask";
    public static final String DISABLED_SUBSCRIBE_TOPIC = "/user/help_call/ask";
    public static final String VOLUNTEER_SEND_TOPIC = "/app/help_call/answer";
    public static final String VOLUNTEER_SUBSCRIBE_TOPIC = "/user/help_call/answer";

    public static final String signupPath  = "/v1/auth/signup";
    public static final String loginPath  = "/v1/auth/signin";
    public static final String getUserPath = "/v1/auth/@me";

    public static final String helpRequestPath = "/v1/help-request";
    public static final String markHelpRequestPath = helpRequestPath+"/complete";
    public static final String helpRequestsFeedPath = helpRequestPath+"/feed";

    public static final String getProfilePicturePath = "/v1/user/profile-pic";
    public static final String updateProfilePicturePath = "/v1/user/profile-pic";

    public static final String CHANGE_PASSWORD_PATH = "/v1/user/change_password";
    public static final String CHANGE_LANGUAGE_PATH = "/v1/user/change_language";
    public static final String CHANNEL_1_ID = "call notification";
    public static final String CHANNEL_1_NAME = "Call Notification";
    public static final String CHANNEL_1_DESCRIPTION = "This notification appears when a" +
            " disabled needs a video call help";
    public static final String USER_INTENT_TAG = "USER";
}
