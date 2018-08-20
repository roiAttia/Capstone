package roiattia.com.capstone.ui.finances;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.capstone.R;

public class IncomeFragment extends Fragment{

    private IncomeJobsAdapter mIncomeJobsAdapter;
    private FinancesViewModel mFinancesViewModel;

    @BindView(R.id.rv_income) RecyclerView mIncomeListView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_income, container, false);
        ButterKnife.bind(this, rootView);

        mIncomeListView.setHasFixedSize(true);

        return rootView;
    }

    public void calculateNewPeriod(){

    }
}
