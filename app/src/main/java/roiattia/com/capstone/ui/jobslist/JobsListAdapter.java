package roiattia.com.capstone.ui.jobslist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.capstone.R;
import roiattia.com.capstone.database.entry.JobEntry;
import roiattia.com.capstone.model.PaymentItemModel;
import roiattia.com.capstone.utils.AmountUtils;
import roiattia.com.capstone.utils.DateUtils;

public class JobsListAdapter extends RecyclerView
        .Adapter<JobsListAdapter.JobViewHolder> {

    private Context mContext;
    private List<JobEntry> mJobs;

    JobsListAdapter(Context context){
        mContext = context;
    }


    public void setData(List<JobEntry> jobs){
        mJobs = jobs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_job_list, parent, false);
        return new JobsListAdapter.JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        JobEntry job = mJobs.get(position);
        if(job.getJobDescription() != null){
            holder.mJobDescription.setText(job.getJobDescription());
        } else holder.mJobDescription.setText(R.string.no_decription);
        holder.mJobPaymentDate.setText(String.format("%s %s",mContext.getString(R.string.payment_date),
                DateUtils.getDateStringFormat(job.getJobDateOfPayment())));
        holder.mJobProfit.setText(String.format("%s %s", mContext.getString(R.string.job_profit),
                AmountUtils.getStringFormatFromDouble(job.getJobProfits())));
    }

    @Override
    public int getItemCount() {
        return mJobs == null ? 0 : mJobs.size();
    }

    class JobViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_job_list_description) TextView mJobDescription;
        @BindView(R.id.tv_job_payment_date) TextView mJobPaymentDate;
        @BindView(R.id.tv_job_profit) TextView mJobProfit;

        JobViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
