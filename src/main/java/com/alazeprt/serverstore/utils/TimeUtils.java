package com.alazeprt.serverstore.utils;

public class TimeUtils {
    public static String formatTime(long hours) {
        long days = hours / 24;
        long hoursLeft = hours % 24;
        if(days == 0 && hoursLeft == 0) {
            return "0";
        } else if(days == 0) {
            return hoursLeft + "小时";
        } else if(hoursLeft == 0) {
            return days + "天";
        } else {
            return days + "天" + hoursLeft + "小时";
        }
    }
}
