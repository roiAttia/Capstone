package roiattia.com.capstone.utils;

import org.joda.time.LocalDate;

public class DateUtils {

    public static LocalDate getStartDateOfTheWeek(){
        LocalDate date = new LocalDate();
        date = date.withDayOfWeek(1);
        return date;
    }

    public static LocalDate getEndDateOfTheWeek(){
        LocalDate date = new LocalDate();
        date = date.withDayOfWeek(7);
        return date;
    }

    public static LocalDate getStartDateOfTheMonth(){
        LocalDate date = new LocalDate();
        date = date.plusMonths(0).withDayOfMonth(1);
        return date;
    }

    public static LocalDate getEndDateOfTheMonth(){
        LocalDate date = new LocalDate();
        date = date.plusMonths(1).withDayOfMonth(1);
        return date;
    }

    public static LocalDate getStartDateOfTheYear(){
        LocalDate date = new LocalDate();
        date = date.plusYears(0).plusMonths(0).withDayOfYear(1);
        return date;
    }

    public static LocalDate getEndDateOfTheYear(){
        LocalDate date = new LocalDate();
        date = date.plusYears(1).plusMonths(0).withDayOfYear(1);
        return date;
    }
}
