package roiattia.com.capstone.ui.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.capstone.R;
import roiattia.com.capstone.model.JobCalendarModel;
import roiattia.com.capstone.utils.AmountUtils;
import roiattia.com.capstone.utils.DateUtils;

public class CalendarJobsAdapter extends RecyclerView.Adapter<CalendarJobsAdapter.CalendarJobViewHolder> {

    private List<JobCalendarModel> mJobEntries;
    private Context mContext;
    private OnJobClickHandler mOnJobClickHandler;

    CalendarJobsAdapter(Context context, OnJobClickHandler onJobClickHandler) {
        mContext = context;
        mOnJobClickHandler = onJobClickHandler;
    }

    public interface OnJobClickHandler{
        void onClick(long jobId);
    }

    @NonNull
    @Override
    public CalendarJobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_job_calendar, parent, false);
        return new CalendarJobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CalendarJobViewHolder holder, int position) {
        final JobCalendarModel jobEntry = mJobEntries.get(position);
        // check if job has a description to show
        if(jobEntry.getJobDescription() != null){
            holder.jobCategoryNameView.setText(String.format("%s - %s",
                    jobEntry.getCategoryName(), jobEntry.getJobDescription()));
        } else {
            holder.jobCategoryNameView.setText(jobEntry.getCategoryName());
        }
        holder.jobIncomeView.setText(String.format("%s",
                AmountUtils.getStringFormatFromDouble(jobEntry.getJobIncome())));
        LocalDate jobDate = new LocalDate(jobEntry.getJobDate());
        holder.jobDateView.setText(DateUtils.getDateStringFormat(jobDate));
    }

    @Override
    public int getItemCount() {
        return mJobEntries == null ? 0 : mJobEntries.size();
    }


    public class CalendarJobViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{

        @BindView(R.id.tv_category_name) TextView jobCategoryNameView;
        @BindView(R.id.tv_date) TextView jobDateView;
        @BindView(R.id.tv_income) TextView jobIncomeView;

        CalendarJobViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnJobClickHandler.onClick(mJobEntries.get(getAdapterPosition()).getJobId());
        }
    }

    public void setJobs(List<JobCalendarModel> jobEntries) {
        mJobEntries = jobEntries;
        notifyDataSetChanged();
    }
}
