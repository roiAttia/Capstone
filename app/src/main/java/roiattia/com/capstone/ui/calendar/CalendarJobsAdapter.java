package roiattia.com.capstone.ui.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.capstone.R;
import roiattia.com.capstone.model.JobCalendarModel;

public class CalendarJobsAdapter extends RecyclerView.Adapter<CalendarJobsAdapter.CalendarJobViewHolder> {

    private List<JobCalendarModel> mJobEntries;
    private Context mContext;

    CalendarJobsAdapter(Context context) {
        mContext = context;
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
        holder.jobIncome.setText(String.format("%s", jobEntry.getJobIncome()));
        holder.jobCategoryName.setText(jobEntry.getCategoryName());
        LocalDate localDate = new LocalDate(jobEntry.getJobDate());
        DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");
        holder.jobDate.setText(fmt.print(localDate));
    }

    @Override
    public int getItemCount() {
        return mJobEntries == null ? 0 : mJobEntries.size();
    }


    public class CalendarJobViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_category_name) TextView jobCategoryName;
        @BindView(R.id.tv_date) TextView jobDate;
        @BindView(R.id.tv_income) TextView jobIncome;

        CalendarJobViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setJobs(List<JobCalendarModel> jobEntries) {
        mJobEntries = jobEntries;
        notifyDataSetChanged();
    }
}
