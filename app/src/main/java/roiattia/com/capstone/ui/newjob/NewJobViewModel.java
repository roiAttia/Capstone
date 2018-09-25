package roiattia.com.capstone.ui.newjob;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import roiattia.com.capstone.database.AppExecutors;
import roiattia.com.capstone.repositories.CategoriesRepository;
import roiattia.com.capstone.repositories.ExpensesRepository;
import roiattia.com.capstone.repositories.JobsRepository;

public class NewJobViewModel extends AndroidViewModel {

    private static final String TAG = NewJobViewModel.class.getSimpleName();
    private JobsRepository mJobsRepository;
    private CategoriesRepository mCategoriesRepository;
    private ExpensesRepository mExpensesRepository;
    private AppExecutors mExecutors;


    public NewJobViewModel(@NonNull Application application) {
        super(application);
        mJobsRepository = JobsRepository.getInstance(application.getApplicationContext());
        mCategoriesRepository = CategoriesRepository.getInstance(application.getApplicationContext());
        mExpensesRepository = ExpensesRepository.getInstance(application.getApplicationContext());
        mExecutors = AppExecutors.getInstance();
    }

    
}
