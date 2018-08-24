package roiattia.com.capstone.ui.finances;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

public class FinancesViewModelFactory extends ViewModelProvider.NewInstanceFactory  {

    Context mContext;

    public FinancesViewModelFactory(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FinancesViewModel(mContext);
    }
}
