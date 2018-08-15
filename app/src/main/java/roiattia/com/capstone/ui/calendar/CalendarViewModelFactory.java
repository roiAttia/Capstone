package roiattia.com.capstone.ui.calendar;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import roiattia.com.capstone.database.Repository;

public class CalendarViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Repository mRepository;

    public CalendarViewModelFactory(Repository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CalendarViewModel(mRepository);
    }
}
