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
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.capstone.R;
import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.model.PaymentItemModel;
import roiattia.com.capstone.ui.finances.FinancesActivity;
import roiattia.com.capstone.ui.newexpense.ExpenseActivity;
import roiattia.com.capstone.ui.payments.PaymentsAdapter;
import roiattia.com.capstone.utils.DateUtils;
import roiattia.com.capstone.utils.InjectorUtils;

import static roiattia.com.capstone.ui.newexpense.ExpenseActivity.EXTRA_EXPENSE_ID;

public class CategoryDetailsActivity extends AppCompatActivity
    implements CategoryDetailsAdapter.OnExpenseClickHandler {

    private final String TAG = CategoryDetailsActivity.class.getSimpleName();

    private CategoryDetailsViewModel mViewModel;
    private CategoryDetailsAdapter mAdapter;
    private PaymentsAdapter mPaymentsAdapter;

    @BindView(R.id.tv_header) TextView mCategoryHeader;
    @BindView(R.id.rv_job_list) RecyclerView mDetailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);
        ButterKnife.bind(this);

//        mAdapter = new CategoryDetailsAdapter(this, this);
        mPaymentsAdapter = new PaymentsAdapter(this);
        // initialize jobs_list and it's adapter
//        mDetailsList.setAdapter(mAdapter);
        mDetailsList.setAdapter(mPaymentsAdapter);
        mDetailsList.setLayoutManager(new LinearLayoutManager(this));
        mDetailsList.setHasFixedSize(true);

        Intent intent = getIntent();
        // check for intent extra as bundle
        if(intent != null && intent.hasExtra(FinancesActivity.BUNDLE_CATEGORY_DATA)){
            // extract all data from bundle
            Bundle categoryDataBundle = intent.getBundleExtra(FinancesActivity.BUNDLE_CATEGORY_DATA);
            String categoryName = categoryDataBundle.getString(FinancesActivity.BUNDLE_CATEGORY_NAME);
            long categoryId = categoryDataBundle.getLong(FinancesActivity.BUNDLE_CATEGORY_ID);
            LocalDate startDate = new LocalDate(
                    categoryDataBundle.getString(FinancesActivity.BUNDLE_START_DATE));
            LocalDate endDate = new LocalDate(
                    categoryDataBundle.getString(FinancesActivity.BUNDLE_END_DATE));

            // set head_line as follows: *category_name* transactions <br> *start_date*
            // - *end_date*. example: Fuel transactions <br> 20/08/2018 - 26/08/2018
            mCategoryHeader.setText(Html.fromHtml(categoryName + getString(R.string.payments_list_headline)
                    + "<br>" + DateUtils.getDateStringFormat(startDate) + " - " +
                    DateUtils.getDateStringFormat(endDate)));

            // setup factory and view model
//            CategoryDetailsViewModelFactory factory = InjectorUtils
//                    .provideCategoryDetailsViewModelFactory(this, categoryId, startDate, endDate);
//            mViewModel = ViewModelProviders.of(this, factory).get(CategoryDetailsViewModel.class);
////            // observe on expenses
//////            mViewModel.getExpenseDetails().observe(this, new Observer<List<ExpenseEntry>>() {
//////                @Override
//////                public void onChanged(List<ExpenseEntry> expenseEntries) {
//////                    mAdapter.setData(expenseEntries);
//////                    for(ExpenseEntry expenseEntry : expenseEntries){
//////                        Log.i(TAG, expenseEntry.toString());
//////                    }
//////                }
//////            });
//            // observe on payments
//            mViewModel.getPaymentsDetails().observe(this, new Observer<List<PaymentItemModel>>() {
//                @Override
//                public void onChanged(List<PaymentItemModel> paymentsList) {
//                    mPaymentsAdapter.setData(paymentsList);
//                    for(PaymentItemModel paymentItemModel : paymentsList){
//                        Log.i(TAG, paymentItemModel.toString());
//                    }
//                }
//            });
        }
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
