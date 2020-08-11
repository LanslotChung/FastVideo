package com.lanslot.fastvideo.Http;

public class HttpCommon {

   static public final String URL = "http://45.192.169.3/egv";
  // static public final String URL = "http://192.168.3.7:8888/egv";
  static public final String LOGIN = URL + "/user/login";
    static public final String FORGET_PASSWORD = URL + "/user/forgetPassword";
    static public final String LOGOUT = URL + "/user/logout";
    static public final String CONFIG = URL + "/config/list";
    static public final String INDEX_INFO = URL + "/index/info";
    static public final String USER_GROUP = URL + "/user/getUser";
    static public final String RESET_PASSWORD = URL + "/user/resetPassword";
    static public final String REGISTER = URL + "/user/save";
    static public final String USER_INFO = URL + "/user/userInfo";
    static public final String CHECK_VERSION = URL + "/version/checkVersion";
}
