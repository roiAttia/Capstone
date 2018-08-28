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
import roiattia.com.capstone.model.ExpensesModel;

public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ExpensesViewHolder> {

    private Context mContext;
    private List<ExpensesModel> mExpensesList;
    private OnExpenseClickHandler mClickCallback;

    ExpensesAdapter(Context context, OnExpenseClickHandler clickCallback) {
        mContext = context;
        mClickCallback = clickCallback;
    }

    public interface OnExpenseClickHandler{
        void onClick(long categoryId, String categoryName);
    }

    public void setData(List<ExpensesModel> expensesList){
        mExpensesList = expensesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExpensesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_expense_category, parent, false);
        return new ExpensesAdapter.ExpensesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpensesViewHolder holder, int position) {
        ExpensesModel expensesModel = mExpensesList.get(position);
        if(expensesModel != null){
            holder.mCategoryNameView.setText(expensesModel.getName());
            holder.mNumberOfTransactionsView.setText(String.valueOf(expensesModel.getCount()));
            holder.mTotalCostView.setText(String.valueOf(expensesModel.getCost()));
        }
    }

    @Override
    public int getItemCount() {
        return mExpensesList == null ? 0 : mExpensesList.size();
    }

    class ExpensesViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{

        @BindView(R.id.tv_expense_category_name) TextView mCategoryNameView;
        @BindView(R.id.tv_expense_category_number_of_transactions) TextView mNumberOfTransactionsView;
        @BindView(R.id.tv_expense_category_total_cost) TextView mTotalCostView;

        ExpensesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            long categoryId = mExpensesList.get(getAdapterPosition()).getCategoryId();
            String categoryName = mExpensesList.get(getAdapterPosition()).getName();
            mClickCallback.onClick(categoryId, categoryName);
        }
    }
}
