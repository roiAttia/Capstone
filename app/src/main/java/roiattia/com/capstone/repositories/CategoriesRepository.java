package roiattia.com.capstone.repositories;

import android.content.Context;

import java.util.List;

import roiattia.com.capstone.database.AppDatabase;
import roiattia.com.capstone.database.AppExecutors;
import roiattia.com.capstone.database.entry.CategoryEntry;

public class CategoriesRepository {

    private static final String TAG = CategoriesRepository.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static CategoriesRepository sInstance;
    private AppDatabase mDb;
    private AppExecutors mExecutors;

    private CategoriesRepository (Context context){
        mDb = AppDatabase.getsInstance(context);
        mExecutors = AppExecutors.getInstance();
    }

    public synchronized static CategoriesRepository getInstance(Context context){
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new CategoriesRepository(context);
            }
        }
        return sInstance;
    }

    public void insertCategories(final List<CategoryEntry> categoryEntries){
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.categoryDao().insertCategories(categoryEntries);
            }
        });
    }

}
