package roiattia.com.capstone.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import roiattia.com.capstone.database.dao.CategoryDao;
import roiattia.com.capstone.database.dao.ExpenseDao;
import roiattia.com.capstone.database.dao.JobDao;
import roiattia.com.capstone.database.dao.PaymentDao;
import roiattia.com.capstone.database.entry.CategoryEntry;
import roiattia.com.capstone.database.entry.ExpenseEntry;
import roiattia.com.capstone.database.entry.JobEntry;
import roiattia.com.capstone.database.entry.PaymentEntry;

@Database(entities = {JobEntry.class, CategoryEntry.class, ExpenseEntry.class, PaymentEntry.class},
        version = 8, exportSchema = false)
@TypeConverters({DateTypeConverter.class, CategoryTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "mywork";
    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context){
        if(sInstance == null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return sInstance;
    }

    public abstract JobDao jobDao();

    public abstract CategoryDao categoryDao();

    public abstract ExpenseDao expenseDao();

    public abstract PaymentDao paymentDao();
}
