package roiattia.com.capstone.ui.newexpense;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import roiattia.com.capstone.R;
import roiattia.com.capstone.database.CategoryEntry;
import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.ui.finances.FinanceExpensesFragment;
import roiattia.com.capstone.ui.newjob.ExpenseFragment;
import roiattia.com.capstone.utils.InjectorUtils;

public class ExpenseActivity extends AppCompatActivity
    implements ExpenseFragment.ConfirmExpenseHandler{

    private final String TAG = ExpenseActivity.class.getSimpleName();

    public static final String EXTRA_EXPENSE_ID = "expense_id";
    private static final long DEFAULT_EXPENSE_ID = -1;

    private long mExpenseId = DEFAULT_EXPENSE_ID;
    private ExpenseFragment mExpensesFragment;
    private FragmentManager mFragmentManager;
    private ExpenseViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        mExpensesFragment = new ExpenseFragment();

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(EXTRA_EXPENSE_ID)) {
            if(mExpenseId == DEFAULT_EXPENSE_ID){
                mExpenseId = intent.getLongExtra(EXTRA_EXPENSE_ID, DEFAULT_EXPENSE_ID);
            }
        }

        ExpenseRepository repository = InjectorUtils.provideExpenseRepository(this);
        ExpenseViewModelFactory viewModelFactory =
                new ExpenseViewModelFactory(repository, mExpenseId);
        mViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ExpenseViewModel.class);

        mViewModel.getCategories().observe(this, new Observer<List<CategoryEntry>>() {
            @Override
            public void onChanged(@Nullable List<CategoryEntry> categoryEntries) {
                if(categoryEntries != null){
                    mExpensesFragment.setCategoriesData(categoryEntries);
                    for(CategoryEntry categoryEntry : categoryEntries){
                        Log.i(TAG, categoryEntry.toString());
                    }
                }
            }
        });

        if(mExpenseId != DEFAULT_EXPENSE_ID){
            mViewModel.getExpense().observe(this, new Observer<ExpenseEntry>() {
                @Override
                public void onChanged(@Nullable ExpenseEntry expenseEntry) {
                    if(expenseEntry != null) {
                        mExpensesFragment.setExpense(expenseEntry);
                        Log.i(TAG, expenseEntry.toString());
                    }
                }
            });
        }

        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction()
                .add(R.id.expense_fragment_placeholder, mExpensesFragment)
                .commit();
    }

    @Override
    public void onConfirmExpenseClick(ExpenseEntry expenseEntry, String newCategoryName) {
        Log.i(TAG, expenseEntry.toString() + "   " + newCategoryName);
        // Insert new category
        if(expenseEntry.getCategoryId() == ExpenseFragment.NEW_CATEGORY_ID_INDICATOR){
            mViewModel.setExpense(expenseEntry);
            mViewModel.insertNewCategory(newCategoryName);
        }
        // Existed category selected
        else{
            mViewModel.insertNewExpense(expenseEntry);
        }

        mFragmentManager.popBackStack();
    }

}
