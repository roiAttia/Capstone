package roiattia.com.capstone.ui.finances;

import android.util.Log;

import org.joda.time.LocalDate;

import roiattia.com.capstone.utils.DateUtils;

public class DateModel {

    private static final String TAG = DateModel.class.getSimpleName();

    private static DateModel sInstance;
    // For Singleton instantiation
    private static final Object LOCK = new Object();

    private LocalDate mCurrentFromDate;
    private LocalDate mCurrentToDate;
    private LocalDate mExpectedFromDate;
    private LocalDate mExpectedToDate;

    public static DateModel getInstance() {
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = new DateModel();
            }
        }
        return sInstance;
    }

    private DateModel() {
        mCurrentFromDate = new LocalDate();
        mCurrentToDate = new LocalDate();
        mExpectedFromDate = new LocalDate();
        mExpectedToDate = new LocalDate();
    }

    public void setDates(int periodSelected){
        switch (periodSelected){
            case 0: // week
                mCurrentFromDate = LocalDate.now().withDayOfWeek(1);
                mCurrentToDate = LocalDate.now();
                mExpectedFromDate = LocalDate.now().plusDays(1);
                mExpectedToDate = LocalDate.now().withDayOfWeek(7);
                break;
            case 1: // month
                mCurrentFromDate = LocalDate.now().withDayOfMonth(1);
                mCurrentToDate = LocalDate.now();
                mExpectedFromDate = LocalDate.now().plusDays(1);
                mExpectedToDate = LocalDate.now().withDayOfMonth(31);
                break;
            case 2: // year
                mCurrentFromDate = LocalDate.now().withDayOfYear(1);
                mCurrentToDate = LocalDate.now();
                mExpectedFromDate = LocalDate.now().plusDays(1);
                mExpectedToDate = LocalDate.now().withDayOfYear(365);
                break;
        }
        logDates();
    }

    private void logDates() {
        Log.i(TAG, "current from date: " + mCurrentFromDate.toString());
        Log.i(TAG, "current to date: " + mCurrentToDate.toString());
        Log.i(TAG, "expected from date: " + mExpectedFromDate.toString());
        Log.i(TAG, "expected to date: " + mExpectedToDate.toString());
    }

    public LocalDate getCurrentFromDate() {
        return mCurrentFromDate;
    }

    public LocalDate getCurrentToDate() {
        return mCurrentToDate;
    }

    public LocalDate getExpectedFromDate() {
        return mExpectedFromDate;
    }

    public LocalDate getExpectedToDate() {
        return mExpectedToDate;
    }
}
