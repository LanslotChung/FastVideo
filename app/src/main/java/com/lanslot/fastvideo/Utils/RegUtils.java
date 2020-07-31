package com.lanslot.fastvideo.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegUtils {
    static public boolean isPhoneNumber(String phoneNumber){
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        if(phoneNumber.length() != 11){
            return false;
        }else{
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phoneNumber);
            boolean isMatch = m.matches();
            return isMatch;
        }
    }
}
