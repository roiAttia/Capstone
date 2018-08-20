package roiattia.com.capstone.ui.newjob;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.Repository;

public class NewJobViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Repository mRepository;
    private CategoryEntry.Type mType;

    public NewJobViewModelFactory(Repository repository, CategoryEntry.Type type) {
        mRepository = repository;
        mType = type;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new NewJobViewModel(mRepository);
    }
}
