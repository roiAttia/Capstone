package roiattia.com.capstone.ui.finances;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.capstone.R;
import roiattia.com.capstone.model.IncomeModel;
import roiattia.com.capstone.utils.AmountUtils;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.IncomeJobsViewHolder> {

    private Context mContext;
    private List<IncomeModel> mIncomeList;

    public IncomeAdapter(Context context) {
        mContext = context;
    }

    public void setData(List<IncomeModel> incomeList){
        mIncomeList = incomeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IncomeJobsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_income_category, parent, false);
        return new IncomeAdapter.IncomeJobsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeJobsViewHolder holder, int position) {
        IncomeModel incomeModel = mIncomeList.get(position);
        if(incomeModel != null){
            holder.mCategoryNameView.setText(incomeModel.getName());
            holder.mNumberOfJobsView.setText(String.format(
                    "%s %s", mContext.getString(R.string.job_category_number_of_jobs),
                    String.valueOf(incomeModel.getCount())));
            holder.mTotalIncomeView.setText(
                    String.format("%s %s",
                            mContext.getString(R.string.job_category_total_income),
                            AmountUtils.getStringFormatFromDouble(incomeModel.getIncome())));
            holder.mTotalProfitsView.setText(String.format("%s %s",
                    mContext.getString(R.string.job_category_total_profits),
                    AmountUtils.getStringFormatFromDouble(incomeModel.getProfit())));
        }
    }

    @Override
    public int getItemCount() {
        return mIncomeList == null ? 0 : mIncomeList.size();
    }

    class IncomeJobsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_income_category_name) TextView mCategoryNameView;
        @BindView(R.id.tv_income_category_number_of_jobs) TextView mNumberOfJobsView;
        @BindView(R.id.tv_income_category_total_income) TextView mTotalIncomeView;
        @BindView(R.id.tv_income_category_total_profits) TextView mTotalProfitsView;

        IncomeJobsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
