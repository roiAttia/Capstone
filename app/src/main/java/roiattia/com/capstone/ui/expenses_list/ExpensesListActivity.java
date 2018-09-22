package roiattia.com.capstone.ui.expenses_list;

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

import org.joda.time.LocalDate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.capstone.R;
import roiattia.com.capstone.database.entry.ExpenseEntry;
import roiattia.com.capstone.ui.newexpense.ExpenseActivity;
import roiattia.com.capstone.utils.DateUtils;
import roiattia.com.capstone.utils.InjectorUtils;

import static roiattia.com.capstone.ui.newexpense.ExpenseActivity.EXTRA_EXPENSE_ID;

public class ExpensesListActivity extends AppCompatActivity
    implements ExpensesListAdapter.OnExpenseClickHandler {

    private final String TAG = ExpensesListActivity.class.getSimpleName();

    private ExpensesListViewModel mViewModel;
    private ExpensesListAdapter mAdapter;
    private LocalDate mStartDate;
    private LocalDate mEndDate;

    @BindView(R.id.rv_expenses_list) RecyclerView mDetailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses_list);
        ButterKnife.bind(this);

        mStartDate = DateUtils.getStartDateOfTheMonth();
        mEndDate = DateUtils.getEndDateOfTheMonth();

        // initialize jobs_list and it's adapter
        mAdapter = new ExpensesListAdapter(this, this);
        mDetailsList.setAdapter(mAdapter);
        mDetailsList.setLayoutManager(new LinearLayoutManager(this));
        mDetailsList.setHasFixedSize(true);

        setupViewModel();
    }

    private void setupViewModel() {
        // setup factory and view model
        ExpensesListViewModelFactory factory = InjectorUtils
                .provideExpensesListViewModelFactory(this, mStartDate, mEndDate);
        mViewModel = ViewModelProviders.of(this, factory).get(ExpensesListViewModel.class);
        // observe on expenses
        mViewModel.getExpenseDetails().observe(this, new Observer<List<ExpenseEntry>>() {
            @Override
            public void onChanged(List<ExpenseEntry> expenseEntries) {
                mAdapter.setData(expenseEntries);
                for(ExpenseEntry expenseEntry : expenseEntries){
                    Log.i(TAG, expenseEntry.toString());
                }
            }
        });
    }

    @Override
    public void onDeleteClick(final ExpenseEntry expenseEntry) {
        // create alert dialog to confirm delete operation
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.alert_dialog_delete_title)
                .setMessage(expenseEntry.toString())
                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mViewModel.deleteExpense(expenseEntry);
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
