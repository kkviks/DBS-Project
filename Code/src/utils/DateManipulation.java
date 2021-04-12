package utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateManipulation {

    public static String getTodayDate() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();

        return df.format(now);
    }

    public static int getDifference(String startDate) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //String startDate="2021-04-12";
        //System.out.println(startDate);
        int daysBetween = -1;

        try {
            Date d1 = (Date) df.parse(startDate);
            Date d2 = new Date(System.currentTimeMillis());

            long difference = d2.getTime() - d1.getTime();
            daysBetween = (int) TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return daysBetween + 1;

    }


}