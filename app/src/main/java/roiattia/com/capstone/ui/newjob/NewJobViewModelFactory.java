package roiattia.com.capstone.ui.newjob;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

public class NewJobViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Context mContext;

    public NewJobViewModelFactory(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new NewJobViewModel(mContext);
    }
}
