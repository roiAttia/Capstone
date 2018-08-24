package roiattia.com.capstone.ui.finances;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.capstone.R;
import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.model.ExpensesModel;

public class FinanceExpensesFragment extends BaseFinancialFragment {

    public static final String TAG = FinanceExpensesFragment.class.getSimpleName();

    private ExpensesAdapter mExpensesAdapter;
    private List<ExpensesModel> mExpensesModelList;

    @BindView(R.id.rv_expenses) RecyclerView mExpensesListView;

    public FinanceExpensesFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_expenses, container, false);
        ButterKnife.bind(this, rootView);

        mExpensesAdapter = new ExpensesAdapter(mListener);
        mExpensesListView.setHasFixedSize(true);
        mExpensesListView.setAdapter(mExpensesAdapter);
        mExpensesListView.setLayoutManager(new LinearLayoutManager(mListener));
        if(mExpensesModelList != null){
            mExpensesAdapter.setData(mExpensesModelList);
        } else{
            Log.i(TAG, "mExpensesModelList is null");
        }

        return rootView;
    }

    public void setData(List<ExpensesModel> expensesModelList) {
       mExpensesModelList = expensesModelList;
       if(mExpensesAdapter != null){
           mExpensesAdapter.setData(mExpensesModelList);
       } else {
           Log.i(TAG, "mExpensesAdapter is null");
       }
    }

}
