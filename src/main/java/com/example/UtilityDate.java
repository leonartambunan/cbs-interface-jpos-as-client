package com.example;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UtilityDate
{

    public UtilityDate()
    {
    }

    public static boolean isTime(String s)
    {
        String as[] = s.split("\\:");
        Calendar calendar = Calendar.getInstance();
        calendar.set(9, 0);
        if(as[0] != null)
            calendar.set(10, Integer.parseInt(as[0]));
        if(as[1] != null)
            calendar.set(12, Integer.parseInt(as[1]));
        if(as.length > 2 && as[2] != null)
            calendar.set(13, Integer.parseInt(as[2]));
        Calendar calendar1 = Calendar.getInstance();
        return calendar.before(calendar1);
    }

    public static String nextTimeInterval(int i)
    {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        long l = date.getTime();
        l += i * 1000;
        date.setTime(l);
        SimpleDateFormat simpledateformat = new SimpleDateFormat("HH:mm");
        return simpledateformat.format(date);
    }

    public static String currentDateTime()
    {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return simpledateformat.format(date);
    }

    public static String currentDate()
    {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd-MM-yyyy");
        return simpledateformat.format(date);
    }

    public static String formatDate( String format )
    {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpledateformat = new SimpleDateFormat( format );
        return simpledateformat.format(date);
    }

    public static String localTransactionTime()
    {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("MMddHHmmss");
        return simpledateformat.format(date);
    }

    private final static SimpleDateFormat ddMMyyDateFormat = new SimpleDateFormat("ddMMyy");
    public static String localDateddMMYY()
    {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        return ddMMyyDateFormat.format(date);
    }

    public static String getDateTime( long timeMillis, String dateFormat )
    {
        Date date = new Date( timeMillis );
        SimpleDateFormat simpledateformat = new SimpleDateFormat( dateFormat );
        return simpledateformat.format(date);
    }



    public static int getCurrentTimeInteger()
    {
        long milliseconds = System.currentTimeMillis();
        int sec = (int)(milliseconds / 1000);
        return sec;
    }

    public static String localTime()
    {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("HHmmss");
        return simpledateformat.format(date);
    }

    public static String localDate()
    {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat simpledateformat = new SimpleDateFormat("MMdd");
        return simpledateformat.format(date);
    }

    public static boolean isValidDate(String strDate){
        String regex = "^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";
        return strDate.matches(regex);
    }

    public static int getSecondFromMinute(int minutes) {
        return minutes*60;
    }

    public static int getSecondFromHour(int hours) {
        return hours*60*60;
    }

    public static int getSecondFromDay(int days) {
        return days*24*60*60;
    }

    public static boolean isValidDatesFormat(String Format, String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Format);
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (Exception pe) {
            return false;
        }
        return true;
    }
}
