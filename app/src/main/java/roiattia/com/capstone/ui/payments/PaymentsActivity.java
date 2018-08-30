package roiattia.com.capstone.ui.payments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import roiattia.com.capstone.model.PaymentItemModel;
import roiattia.com.capstone.ui.finances.FinancesActivity;
import roiattia.com.capstone.utils.DateUtils;
import roiattia.com.capstone.utils.InjectorUtils;

public class PaymentsActivity extends AppCompatActivity {

    private final String TAG = PaymentsActivity.class.getSimpleName();

    private PaymentsViewModel mViewModel;
    private PaymentsAdapter mPaymentsAdapter;

    @BindView(R.id.tv_header) TextView mCategoryHeader;
    @BindView(R.id.rv_job_list) RecyclerView mDetailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_details);
        ButterKnife.bind(this);

        mPaymentsAdapter = new PaymentsAdapter(this);
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
            PaymentsViewModelFactory factory = InjectorUtils
                    .provideCategoryDetailsViewModelFactory(this, categoryId, startDate, endDate);
            mViewModel = ViewModelProviders.of(this, factory).get(PaymentsViewModel.class);
            // observe on payments
            mViewModel.getPaymentsDetails().observe(this, new Observer<List<PaymentItemModel>>() {
                @Override
                public void onChanged(List<PaymentItemModel> paymentsList) {
                    mPaymentsAdapter.setData(paymentsList);
                    for(PaymentItemModel paymentItemModel : paymentsList){
                        Log.i(TAG, paymentItemModel.toString());
                    }
                }
            });
        }
    }

}
