package ma.sid.services;

import ma.sid.utils.DateUtils;

import java.util.Date;

public class Test {
    public static void main(String[] args) {
        Date d = new Date();
        int week = DateUtils.getWeekNumberOfYear(d);
        System.out.println(week);
    }
}
