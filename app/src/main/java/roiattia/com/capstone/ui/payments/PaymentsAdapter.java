package roiattia.com.capstone.ui.payments;

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
import roiattia.com.capstone.model.PaymentItemModel;
import roiattia.com.capstone.utils.AmountUtils;
import roiattia.com.capstone.utils.DateUtils;

public class PaymentsAdapter extends RecyclerView
        .Adapter<PaymentsAdapter.CategoryDetailsViewHolder> {

    private Context mContext;
    private List<PaymentItemModel> mPaymentsList;

    PaymentsAdapter(Context context){
        mContext = context;
    }


    public void setData(List<PaymentItemModel> paymentsList){
        mPaymentsList = paymentsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_payment, parent, false);
        return new PaymentsAdapter.CategoryDetailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryDetailsViewHolder holder, int position) {
        final PaymentItemModel payment = mPaymentsList.get(position);
        holder.mPaymentDate.setText(String.format(
                mContext.getString(R.string.payment_date), DateUtils.getDateStringFormat(payment.getPaymentDate())));
        holder.mPaymentCost.setText(String.format(mContext.getString(R.string.payment_cost), AmountUtils.getStringFormatFromDouble(
                payment.getCost())));
        if(payment.getTotalPayments() > 1) {
            holder.mPaymentNumber.setText(String.format(Locale.getDefault(), "%s%d/%d",
                    mContext.getString(R.string.payment_number),
                    payment.getPaymentNumber(), payment.getTotalPayments()));
        } else {
            holder.mPaymentNumber.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mPaymentsList == null ? 0 : mPaymentsList.size();
    }

    class CategoryDetailsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_payment_date) TextView mPaymentDate;
        @BindView(R.id.tv_payment_cost) TextView mPaymentCost;
        @BindView(R.id.tv_payment_number) TextView mPaymentNumber;

        CategoryDetailsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
