package roiattia.com.capstone.ui.expense;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.Repository;

public class CategoriesViewModel extends ViewModel {

    private LiveData<List<CategoryEntry>> mCategories;

    public CategoriesViewModel(Repository repository, CategoryEntry.Type type) {
        if(type == CategoryEntry.Type.EXPENSE) {
            mCategories = repository.getExpensesCategories();
        } else if(type == CategoryEntry.Type.JOB){
            mCategories = repository.getJobsCategories();
        }
    }

    public LiveData<List<CategoryEntry>> getCategories() {
        return mCategories;
    }
}
