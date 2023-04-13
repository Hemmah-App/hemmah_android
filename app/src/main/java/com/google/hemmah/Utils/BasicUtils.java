package com.google.hemmah.Utils;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import java.util.Locale;

public class BasicUtils {
    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String date = dateFormat.format(calendar.getTime());
        return date;
    }


}
