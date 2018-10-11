package roiattia.com.capstone.ui.expenseslist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.capstone.R;
import roiattia.com.capstone.model.ExpenseListModel;
import roiattia.com.capstone.ui.expense.ExpenseActivity;

import static roiattia.com.capstone.utils.Constants.EXTRA_EXPENSE_ID;

public class ExpensesListActivity extends AppCompatActivity
    implements ExpensesListAdapter.OnExpenseClickHandler {

    private final String TAG = ExpensesListActivity.class.getSimpleName();

    private ExpensesListViewModel mViewModel;
    private ExpensesListAdapter mAdapter;

    @BindView(R.id.rv_expenses_list) RecyclerView mDetailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_list);
        ButterKnife.bind(this);

        setTitle(getString(R.string.expenses_list_title));

        setupRecyclerView();

        setupViewModel();

    }

    private void setupRecyclerView() {
        // initialize jobs_list and it's adapter
        mAdapter = new ExpensesListAdapter(this, this);
        mDetailsList.setAdapter(mAdapter);
        mDetailsList.setLayoutManager(new LinearLayoutManager(this));
        mDetailsList.setHasFixedSize(true);
    }

    private void setupViewModel() {
        mViewModel = ViewModelProviders.of(this).get(ExpensesListViewModel.class);
        // observe on expenses
        mViewModel.getExpenses().observe(this, new Observer<List<ExpenseListModel>>() {
            @Override
            public void onChanged(List<ExpenseListModel> expenseEntries) {
                mAdapter.setData(expenseEntries);
                for(ExpenseListModel expenseEntry : expenseEntries){
                    Log.i(TAG, expenseEntry.toString());
                }
            }
        });
    }

    @Override
    public void onDeleteClick(final long expenseId) {
        // create alert dialog to confirm delete operation
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.alert_dialog_delete_title)
                .setMessage("message")
                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mViewModel.deleteExpense(expenseId);
                    }
                })
                .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onEditClick(long expenseId) {
        // open ExpenseActivity to edit expense details
        Intent intent =  new Intent(this, ExpenseActivity.class);
        intent.putExtra(EXTRA_EXPENSE_ID, expenseId);
        startActivity(intent);
    }
}
