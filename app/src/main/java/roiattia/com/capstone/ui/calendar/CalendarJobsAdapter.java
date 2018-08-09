package roiattia.com.capstone.ui.calendar;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import roiattia.com.capstone.database.JobEntry;

public class CalendarJobsAdapter extends RecyclerView.Adapter<CalendarJobsAdapter.CalendarJobViewHolder> {

    private List<JobEntry> mJobEntries;

    @NonNull
    @Override
    public CalendarJobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarJobViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mJobEntries == null ? 0 : mJobEntries.size();
    }


    public class CalendarJobViewHolder extends RecyclerView.ViewHolder{

        public CalendarJobViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setJobs(List<JobEntry> jobEntries) {
        mJobEntries = jobEntries;
        notifyDataSetChanged();
    }
}
