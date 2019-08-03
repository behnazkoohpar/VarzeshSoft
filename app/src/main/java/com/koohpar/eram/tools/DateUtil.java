package com.koohpar.eram.tools;

import com.koohpar.eram.fDate.FDate;
import com.koohpar.eram.fDate.ShamsiCalendar;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.StringTokenizer;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Behpardaz Jahan</p>
 *
 * @author Reza Asadollahi
 * @version 1.0
 */

public class DateUtil {

    public static String getDateDifferenceInDDMMYYYY(Date from, Date to) {
        Calendar fromDate = Calendar.getInstance();
        Calendar toDate = Calendar.getInstance();
        fromDate.setTime(from);
        toDate.setTime(to);
        int increment = 0;
        int year, month, day;
        System.out.println(fromDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        if (fromDate.get(Calendar.DAY_OF_MONTH) > toDate.get(Calendar.DAY_OF_MONTH)) {
            increment = fromDate.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        System.out.println("increment" + increment);
// DAY CALCULATION
        if (increment != 0) {
            day = (toDate.get(Calendar.DAY_OF_MONTH) + increment) - fromDate.get(Calendar.DAY_OF_MONTH);
            increment = 1;
        } else {
            day = toDate.get(Calendar.DAY_OF_MONTH) - fromDate.get(Calendar.DAY_OF_MONTH);
        }

// MONTH CALCULATION
        if ((fromDate.get(Calendar.MONTH) + increment) > toDate.get(Calendar.MONTH)) {
            month = (toDate.get(Calendar.MONTH) + 12) - (fromDate.get(Calendar.MONTH) + increment);
            increment = 1;
        } else {
            month = (toDate.get(Calendar.MONTH)) - (fromDate.get(Calendar.MONTH) + increment);
            increment = 0;
        }

// YEAR CALCULATION
        year = toDate.get(Calendar.YEAR) - (fromDate.get(Calendar.YEAR) + increment);
        return year + "/" + month + "/" + day;
    }

    public static String getCurrentDate() {
        FDate curDate = new FDate(System.currentTimeMillis());
        return curDate.toString();
    }

    public static String getCurrentTime() {
        FDate curDate = new FDate(System.currentTimeMillis());
//    String time = curDate.getHour() + ":"+curDate.getMinute()+":"+curDate.getSecond();
        return getCompleteTimeString(curDate);
    }

    public static String getCurrentTimeString() {
        return getCompleteTimeString(new FDate(System.currentTimeMillis()));
    }

    public static String getCompleteTimeString(FDate fdate) {
        StringBuffer b = new StringBuffer();
        b.append((fdate.getHour() < 10) ? "0" + (fdate.getHour()) :
                String.valueOf(fdate.getHour()));
        b.append(":");
        b.append((fdate.getMinute() < 10) ? "0" + (fdate.getMinute()) :
                String.valueOf(fdate.getMinute()));
        b.append(":");
        b.append((fdate.getSecond() < 10) ? "0" + (fdate.getSecond()) :
                String.valueOf(fdate.getSecond()));
        return b.toString();
    }

    public static int getCurrentYear() {
        FDate curDate = new FDate(System.currentTimeMillis());
        return curDate.getYear();
    }

    public static int getCurrentMonth() {
        FDate curDate = new FDate(System.currentTimeMillis());
        return curDate.getMonth();
    }

    public static int getCurrentDay() {
        FDate curDate = new FDate(System.currentTimeMillis());
        return curDate.getDate();
    }

    public static String gatMiladiDate(int year, int month, int day) {
        GregorianCalendar gc = new GregorianCalendar(year, month, day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(gc.getTime());
    }

    public static Date toDate(String formattedDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        ParsePosition pos = new ParsePosition(0);
        Date d = dateFormat.parse(formattedDate, pos);
        return d;
    }

    //reverse the order of dd and yyyy in a farsi date string just for fixing farsi presentation problem
    //suppose that input date is in the form of yyyy/mm/dd
    //the output would be as dd/mm/yyyy
    public static String invertDate(String fdate) {
        String yyyy = null;
        String mm = null;
        String dd = null;

        if (fdate == null || fdate.length() == 0)
            return "";
        StringTokenizer strTokenizer = new StringTokenizer(fdate, "/");
        if (strTokenizer.hasMoreTokens()) {
            yyyy = strTokenizer.nextToken();
            if (strTokenizer.hasMoreTokens()) {
                mm = strTokenizer.nextToken();
                if (strTokenizer.hasMoreTokens()) {
                    dd = strTokenizer.nextToken();
                    return dd + "/" + mm + "/" + yyyy;
                }
            }
        }
        return fdate;
    }

    public static String changeFarsiToMiladi(String farsiDate) {
        Date miladiDate = ShamsiCalendar.shamsiToMiladi(farsiDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        return dateFormat.format(miladiDate);
    }

    public static String changeMiladiToFarsi(String miladiDate) {
        return ShamsiCalendar.miladiToShamsi(toDate(miladiDate));
    }

    public static int getTotalDays(FDate startDate, int durationMonth, int durationDay) {
        FDate endDate = new FDate(startDate.toMiladi());
        int i;

        for (i = 0; i < durationMonth; i++)
            endDate.nextMonth();

        for (i = 0; i < durationDay; i++)
            endDate.nextDay();

        int totalDays = endDate.minusDate(startDate.toString());

        return totalDays;
    }

    public static int getDurationMonth(FDate startDate, int totalDays) {

        FDate endDate = new FDate(startDate.toString());
        FDate curDate = new FDate(startDate.toString());
        int i;

//    endDate.plusDay(totalDays);

        for (i = 0; i < totalDays; i++)
            endDate.nextDay();

        System.out.println("END DATE IS " + endDate.toString());

        int durationMonthVal = 0;


        while (curDate.getYear() < endDate.getYear()
                || (curDate.getYear() == endDate.getYear() && curDate.getMonth() < endDate.getMonth())) {
            durationMonthVal++;
            curDate.nextMonth();
        }
        if (curDate.after(endDate)) {
            durationMonthVal--;
            curDate.prevMonth();
        }
        return durationMonthVal;
    }

    public static int getDurationMonth(FDate startDate, FDate endDate) {
        int durationMonth = 0;

        while (startDate.compareTo(endDate) < 0) {
            startDate.nextMonth();
            durationMonth++;
        }
        return durationMonth;
    }

    public static int getDurationMonth(String startDate, String endDate) {
        return getDurationMonth(new FDate(startDate), new FDate(endDate));
    }

    public static int getDurationDay(FDate startDate, int totalDays) {
        FDate endDate = new FDate(startDate.toString());
        FDate curDate = new FDate(startDate.toString());
        int i;

        int durationMonthVal = 0;

//    endDate.plusDay(totalDays);

        for (i = 0; i < totalDays; i++)
            endDate.nextDay();

        while (curDate.getYear() < endDate.getYear()
                || (curDate.getYear() == endDate.getYear() && curDate.getMonth() < endDate.getMonth())) {
            durationMonthVal++;
            curDate.nextMonth();
        }
        if (curDate.after(endDate)) {
            durationMonthVal--;
            curDate.prevMonth();
        }

        int durationDayVal = 0;
        while (curDate.before(endDate)) {
            durationDayVal++;
            curDate.nextDay();
        }

        return durationDayVal;
    }

    public static int getDurationMonth(String startDate, int totalDays) {
        FDate fDate = new FDate(startDate);
        return getDurationMonth(fDate, totalDays);
    }

    public static int getDurationDay(String startDate, int totalDays) {
        FDate fDate = new FDate(startDate);
        return getDurationDay(fDate, totalDays);
    }

    public static String stringDayMountYear() {
        return ShamsiCalendar.weekDayName(ShamsiCalendar.dayOfWeek(DateUtil.getCurrentDate())) + " " +
                ShamsiCalendar.monthDayName(DateUtil.getCurrentDay()) +
                ShamsiCalendar.monthName(DateUtil.getCurrentMonth()) + " ��� " +
                String.valueOf(DateUtil.getCurrentYear());
    }

    public static String decreaseYear(String tavalodDate, int cnt) {
        String year = tavalodDate.substring(0, 4);
        int ny = Integer.decode(year) - cnt;
        return String.valueOf(ny) + tavalodDate.substring(4);
    }

    public static String decreaseCurrentYear(int cnt) {
        String cur = getCurrentDate();
        String year = cur.substring(0, 4);
        int ny = Integer.decode(year) - cnt;
        return String.valueOf(ny) + cur.substring(4);
    }

    public static String increaseYear(String tavalodDate, int cnt) {
        String year = tavalodDate.substring(0, 4);
        int ny = Integer.decode(year) + cnt;
        return String.valueOf(ny) + tavalodDate.substring(4);
    }

    public static String increaseCurrentYear(int cnt) {
        String cur = getCurrentDate();
        String year = cur.substring(0, 4);
        int ny = Integer.decode(year) + cnt;
        return String.valueOf(ny) + cur.substring(4);
    }

    public static String BeautifulDate(String aDateStr) {
        String Year_str = "";
        String Month_str = "";
        String Day_str = "";

        int l = aDateStr.length();
        if (l < 6) {
            return "";
        }
        int pos1 = aDateStr.indexOf('/');
        if ((pos1 == 0) && IsNumeric(aDateStr) && ((l == 6) || (l == 8))) {
            if (l == 6) {
//                aDateStr.(5, "/");
                aDateStr = new StringBuilder(aDateStr).insert(5, "/").toString();
                aDateStr = new StringBuilder(aDateStr).insert(3, "/").toString();
//                aDateStr.Insert(3, "/");
            } else if (l == 8) {
                aDateStr = new StringBuilder(aDateStr).insert(7, "/").toString();
                aDateStr = new StringBuilder(aDateStr).insert(5, "/").toString();
//                aDateStr.Insert(7, "/");
//                aDateStr.Insert(5, "/");
            }
        }
        pos1 = aDateStr.indexOf('/') + 1;
        if (pos1 != 1) {

            String temp_string = aDateStr;

//            temp_string = temp_string.charRemoveAt(0, pos1);
            temp_string =temp_string.substring(0, pos1) + temp_string.substring(pos1 + 1);
            int pos2 = temp_string.indexOf('/') + 1 + pos1 + 1;


            if ((pos2 != pos1 + 1) && (pos2 != 0)) {
                Year_str = aDateStr.substring(0, pos1 - 1);
                Month_str = aDateStr.substring(pos1, pos2 - pos1 - 2);
                Day_str = aDateStr.substring(pos2 - 1, aDateStr.length() - pos2 + 1);
                int Year_int = Integer.parseInt(Year_str);
                int Month_int = Integer.parseInt(Month_str);
                int Day_int = Integer.parseInt(Day_str);
                Year_str = String.valueOf(Year_int);
                Month_str = String.valueOf(Month_int);
                Day_str = String.valueOf(Day_int);

                if ((Year_int != 0) && (Month_int != 0) && (Day_int != 0)) {
                    if ((Year_int < 100) && (Year_str.length() < 3))
                        Year_str = "13" + Year_str;

                    if ((Month_int < 10) && (Month_str.length() < 2))
                        Month_str = '0' + String.valueOf(Month_int);


                    if ((Day_int < 10) && ((Day_str.length()) < 2))
                        Day_str = '0' + String.valueOf(Day_int);
                }
            }
            String result = Year_str + "/" + Month_str + "/" + Day_str;
            return result;
        } else
            return aDateStr;

    }
    public static String charRemoveAt(String str, int p) {
        return str.substring(0, p) + str.substring(p + 1);
    }
//    private boolean IsNumeric(String input) {
//        int test;
//        return int.TryParse(input, test);
//    }

    public static boolean IsNumeric(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }

    public static String MiladiToShamsi(Date aDate) {
        Date datevalue = aDate;
        String wD = String.valueOf(datevalue.getDay());
        String wM = String.valueOf(datevalue.getTime());
        String wY = String.valueOf(datevalue.getYear());
        int Year = Integer.parseInt(wY);
        int Month = Integer.parseInt(wM);
        int Day = Integer.parseInt(wD);
        MiladiToShamsi_Seperator(Year, Month, Day);
        return String.valueOf(Year) + "/" + String.valueOf(Month) + "/" + String.valueOf(Day);
    }

    public static void MiladiToShamsi_Seperator(int aYear, int aMonth, int aDay) {

        Byte[] A = {0, 10, 11, 9, 11, 10, 10, 9, 9, 9, 8, 9, 9, 10, 11, 9};
        Byte[] B = {0, 20, 19, 20, 20, 21, 21, 22, 22, 22, 22, 21, 21, 20, 19, 20};


        if ((aYear % 4 == 0) && (aMonth > 2) || (aYear % 4 == 1) && (aMonth <= 2))
            aDay += 1;

        if (aDay > B[aMonth]) {
            aDay = aDay - B[aMonth];
            aMonth += 1;
        } else {
            aDay = aDay + A[aMonth];
            if (aMonth == 3)
                if (aYear % 4 == 1)
                    aDay += 1;
        }
        aMonth = aMonth + 9;
        if (aMonth > 12) {
            aMonth = aMonth - 12;
            aYear += 1;
        }

        if (aYear >= 1000)
            aYear = aYear - 622;
        else if (aYear >= 22)
            aYear = aYear - 22;
        else
            aYear = aYear + 78;
    }

    public static void ShamsiToMiladi(int aYear, int aMonth, int aDay) {
        Byte[] A = {0, 10, 11, 9, 11, 10, 10, 9, 9, 9, 8, 9, 9, 10, 11, 9};
        Byte[] B = {0, 20, 19, 20, 20, 21, 21, 22, 22, 22, 22, 21, 21, 20, 19, 20};

        if (aYear % 4 == 3)
            aDay -= 1;
        if (aYear % 4 == 2)
            A[15] = 10;
        aMonth = aMonth + 3;
        if (aDay > A[aMonth])
            aDay = aDay - A[aMonth];
        else {
            aMonth -= 1;
            aDay = aDay + B[aMonth];
        }
        if (aMonth > 12)
            aMonth = aMonth - 12;
        else
            aYear -= 1;

        if (aYear < 1000)
            aYear = (aYear + 22) % 100;
        else
            aYear = aYear + 622;

        A[15] = 9;
    }
    public static String patternStr = "yyyy-MM-dd";
    public static SimpleDateFormat  dateFormat = new SimpleDateFormat(patternStr, Locale.ENGLISH);
    public static String GetCurrentShamsiDate() {

        String dateStr = dateFormat.format(new Date());

        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return BeautifulDate(MiladiToShamsi(date));
    }

    public static void ShamsiToGhamary(int aYear, int aMonth, int aDay) {
        int y1 = aYear;
        int M1 = aMonth;
        int D1 = aDay + 5;
//        ShamsiToGhamaryCalculate(y1, M1, D1);
        if (D1 > 5) {
            aYear = y1;
            aMonth = M1;
            aDay = D1 - 5;
        } else {
            y1 = aYear;
            M1 = aMonth;
            D1 = aDay - 5;
//            ShamsiToGhamaryCalculate(y1, M1, D1);
            aYear = y1;
            aMonth = M1;
            aDay = D1 + 5;
        }
    }

//    private void ShamsiToGhamaryCalculate(int aYear, int aMonth, int aDay) {
//        int r = 0;
//        if (aMonth > 6)
//            r = (aMonth - 7) * 30 + aDay + 186;
//        else
//            r = (aMonth - 1) * 31 + aDay;
//
//        double s = (int) (r + (aYear + 131) * 365.242347 + 2.0013);
//        int sTemp = (int) ((s + 8.5) / 29.53059072);
//        double Y2 = sTemp * 29.53059072 + 8.5;
//        s = s + 0.44 * Math.sin(Y2 / 65.576 + 1.17) - 0.095 * Math.sin((aYear - 1216) * 0.336) - 0.13 * Math.sin((Y2 - 493574.0) / 55.16) - 27425.5;
//        if (aYear > 1336)
//            s = s + (aYear - 1336) * 0.004;
//        else
//            s = s + (aYear - 1336) * 0.001;
//
//        double M1 = Math.Truncate(s / 29.53059072);
//        aDay = (int) Math.Truncate(s - M1 * 29.53059072) + 1;
//        aYear = (int) M1 / 12 - 58;
//        aMonth = (int) (M1 % 12) + 1;
//    }

    public static int Farsi_Week_Day(int aYear, int aMonth, int aDay) {
        byte[] MD = {0, 0, 3, 6, 2, 5, 1, 4, 6, 1, 3, 5, 0};
        int X = aYear + aYear / 4 + MD[aMonth] + aDay;
        return (X % 7);
    }

    public static int Latin_Week_Day(int aYear, int aMonth, int aDay) {
        byte[] MD = {0, 0, 3, 3, 6, 1, 4, 6, 2, 5, 0, 3, 5};
        int X = aYear + aYear / 4 + MD[aMonth] + aDay + 5;
        if ((aMonth < 3) && (aYear % 4 == 0))
            X -= 1;
        return (X % 7);
    }

    public static String[] PersianMonthName = {"", "فروردین", "ارديبهشت", "خرداد", "تير", "مرداد", "شهريور", "مهر", "آبان", "آذر", "دي", "بهمن", "اسفند"};
    public static String[] LunarMonthName = {"", "محرم", "صفر", "ربيع الاول", "ربيع الثاني", "جمادي الاول", "جمادي الثاني", "رجب", "شعبان", "رمضان", "شوال", "ذيقعده", "ذيحجه"};
    public static String[] GregorianMonthName = {"", "January", "february", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    public static String[] GregorianWeekName = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    public static String[] PersianWeekName = {"شنبه", "يكشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنج شنبه", "جمعه"};

    public static String[] HafteGhabl(String date) {

        String[] roozHayeHafteGhabl = new String[7];
        String dateShanbe = AddDate(date, -7);
        for (int i = 1; i <= 7; i++) {
            roozHayeHafteGhabl[i-1] = AddDate(dateShanbe, i - 1);
        }
        return roozHayeHafteGhabl;
    }

    public static String[] hafteJari(){

        Calendar c = Calendar.getInstance();
        //c.setTime(yourDate);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 7)
            dayOfWeek = 0;
        else
            dayOfWeek = -1 * dayOfWeek;
        String[] roozHayeHafteJari = new String[7];
        String date = AddDate(DateUtil.getCurrentDate(), dayOfWeek);
        for (int i = 1; i <= 7; i++) {
            roozHayeHafteJari[i-1] = AddDate(date, i - 1);
        }
        return roozHayeHafteJari;

    }

    public static String[] HafteBadi(String date ) {
        String[] roozHayeHafteBadi = new String[7];
        String dateShanbe = AddDate(date, 7);
        for (int i = 1; i <= 7; i++) {
            roozHayeHafteBadi[i-1] = AddDate(dateShanbe, i - 1);
        }

        return roozHayeHafteBadi;
    }

    public static String AddDate(String date_string, Integer n) {

        Integer y, m, d;
        String yst, mst, dst;
        Byte i;
        byte maxdays[] = new byte[13];

        maxdays[1] = 31;
        maxdays[2] = 31;
        maxdays[3] = 31;
        maxdays[4] = 31;
        maxdays[5] = 31;
        maxdays[6] = 31;
        maxdays[7] = 30;
        maxdays[8] = 30;
        maxdays[9] = 30;
        maxdays[10] = 30;
        maxdays[11] = 30;
        maxdays[12] = 29;


        y = Integer.parseInt(date_string.substring(0, 4));
        m = Integer.parseInt(date_string.substring(5, 7));
        d = Integer.parseInt(date_string.substring(8, 10));
        if (n >= 0) {
            d = d + n;
            if (y % 4 == 3)
                maxdays[12] = 30;
            while (d > maxdays[m]) {
                d = d - maxdays[m];

                m++;
                if (m > 12) {
                    m = 1;

                    y++;
                    if (y % 4 == 3)
                        maxdays[12] = 30;
                    else
                        maxdays[12] = 29;
                }
            }
        } else {
            d = d + n;
            while (d < 1) {
                m--;
                if (m < 1) {
                    m = 12;
                    y--;
                    if (y % 4 == 3)
                        maxdays[12] = 30;
                    else
                        maxdays[12] = 29;

                }
                d = d + maxdays[m];

            }
        }

        yst = String.format("%1$04d", y);
        mst = String.format("%1$02d", m);
        dst = String.format("%1$02d", d);
        date_string = yst + "/" + mst + "/" + dst;
        return date_string;
    }

}

