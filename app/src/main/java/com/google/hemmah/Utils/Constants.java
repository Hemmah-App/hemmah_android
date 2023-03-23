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
}
