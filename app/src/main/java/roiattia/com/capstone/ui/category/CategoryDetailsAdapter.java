package roiattia.com.capstone.ui.category;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.capstone.R;
import roiattia.com.capstone.database.ExpenseEntry;
import roiattia.com.capstone.utils.AmountUtils;
import roiattia.com.capstone.utils.DateUtils;

public class CategoryDetailsAdapter extends RecyclerView
        .Adapter<CategoryDetailsAdapter.CategoryDetailsViewHolder> {

    private Context mContext;
    private List<ExpenseEntry> mExpensesList;
    private OnExpenseClickHandler mClickHandler;

    public CategoryDetailsAdapter(Context context, OnExpenseClickHandler clickHandler){
        mContext = context;
        mClickHandler = clickHandler;
    }

    /**
     * Interface for delete and update buttons click events
     */
    public interface OnExpenseClickHandler {
        /**
         * Trigger delete operation on selected expense
         * @param expenseEntry the selected expense
         */
        void onDeleteClick(ExpenseEntry expenseEntry);

        /**
         * Trigger update operation on selected expense
         * @param expenseId selected expense's id
         */
        void onEditClick(long expenseId);
    }

    public void setData(List<ExpenseEntry> expensesList){
        mExpensesList = expensesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_expense_detail, parent, false);
        return new CategoryDetailsAdapter.CategoryDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryDetailsViewHolder holder, int position) {
        final ExpenseEntry expenseEntry = mExpensesList.get(position);
        holder.mPaymentDate.setText(String.format(
                "Payment date: %s", DateUtils.getDateStringFormat(expenseEntry.getExpensePaymentDate())));
        holder.mCost.setText(String.format("Cost: %s", AmountUtils.getStringFormatFromDouble(
                expenseEntry.getExpenseCost())));
        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickHandler.onDeleteClick(expenseEntry);
            }
        });
        holder.mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickHandler.onEditClick(expenseEntry.getExpenseId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mExpensesList == null ? 0 : mExpensesList.size();
    }

    class CategoryDetailsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_payment_date) TextView mPaymentDate;
        @BindView(R.id.tv_cost) TextView mCost;
        @BindView(R.id.btn_delete) ImageButton mDeleteButton;
        @BindView(R.id.btn_edit) ImageButton mEditButton;

        CategoryDetailsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
