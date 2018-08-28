package roiattia.com.capstone.ui.newjob;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class JobViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private JobRepository mJobRepository;
    private Long mJobId;

    public JobViewModelFactory(JobRepository jobRepository, Long jobId,
                               JobRepository.GetJobIdHandler callback) {
        mJobRepository = jobRepository;
        mJobRepository.setCallbackListener(callback);
        mJobId = jobId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new JobViewModel(mJobRepository, mJobId);
    }
}
