package roiattia.com.capstone.ui.calendar;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import roiattia.com.capstone.ui.newjob.JobRepository;

public class CalendarViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private CalendarRepository mRepository;

    public CalendarViewModelFactory(CalendarRepository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CalendarViewModel(mRepository);
    }
}
