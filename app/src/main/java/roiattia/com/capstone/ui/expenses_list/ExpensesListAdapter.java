package roiattia.com.capstone.ui.expenses_list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import roiattia.com.capstone.R;
import roiattia.com.capstone.database.entry.ExpenseEntry;
import roiattia.com.capstone.model.ExpenseListModel;
import roiattia.com.capstone.utils.AmountUtils;
import roiattia.com.capstone.utils.DateUtils;

public class ExpensesListAdapter extends RecyclerView
        .Adapter<ExpensesListAdapter.CategoryDetailsViewHolder> {

    private Context mContext;
    private List<ExpenseListModel> mExpensesList;
    private OnExpenseClickHandler mClickHandler;

    ExpensesListAdapter(Context context, OnExpenseClickHandler clickHandler){
        mContext = context;
        mClickHandler = clickHandler;
    }

    /**
     * Interface for delete and update buttons click events
     */
    public interface OnExpenseClickHandler {
        /**
         * Trigger delete operation on selected expense
         * @param expenseId selected expense's id
         */
        void onDeleteClick(long expenseId);

        /**
         * Trigger update operation on selected expense
         * @param expenseId selected expense's id
         */
        void onEditClick(long expenseId);
    }

    public void setData(List<ExpenseListModel> expensesList){
        mExpensesList = expensesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_expense_detail, parent, false);
        return new ExpensesListAdapter.CategoryDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryDetailsViewHolder holder, int position) {
        final ExpenseListModel expenseEntry = mExpensesList.get(position);
        holder.mCategoryName.setText(String.format("%s - %s", expenseEntry.getCategoryName(),
                expenseEntry.getDescription()));
        holder.mFirstPayment.setText(String.format(
                "First payment: %s", DateUtils.getDateStringFormat(expenseEntry.getExpenseFirstPayment())));
        holder.mLastPayment.setText(String.format(
                "Last payment: %s", DateUtils.getDateStringFormat(expenseEntry.getExpenseLastPayment())));
        holder.mNumberOfPayments.setText(String.format(Locale.getDefault(),
                "%d payments of %s", expenseEntry.getNumberOfPayments(),
                AmountUtils.getStringFormatFromDouble(expenseEntry.getMonthlyCost())));
        holder.mCost.setText(String.format("Total Cost: %s", AmountUtils.getStringFormatFromDouble(
                expenseEntry.getExpenseCost())));
        LocalDate currentDate = new LocalDate();
        if(currentDate.isAfter(expenseEntry.getExpenseLastPayment())){
            holder.mStatus.setText(R.string.expense_list_done_expense);
        } else {
            holder.mStatus.setText(R.string.expense_list_active_expense);
        }
        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickHandler.onDeleteClick(expenseEntry.getExpenseId());
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

        @BindView(R.id.tv_expense_category_name) TextView mCategoryName;
        @BindView(R.id.tv_first_payment_date) TextView mFirstPayment;
        @BindView(R.id.tv_last_payment) TextView mLastPayment;
        @BindView(R.id.tv_expense_number_of_payments) TextView mNumberOfPayments;
        @BindView(R.id.tv_cost) TextView mCost;
        @BindView(R.id.tv_expense_status) TextView mStatus;
        @BindView(R.id.btn_delete) ImageButton mDeleteButton;
        @BindView(R.id.btn_edit) ImageButton mEditButton;

        CategoryDetailsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
