package roiattia.com.capstone.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.model.PaymentItemModel;

@Dao
public interface PaymentDao {

    // Return payment record with it's expense total number of payments
    @Query("SELECT payment_cost AS mCost, payment_number AS mPaymentNumber, " +
            "number_of_payments AS mTotalPayments, payment_date AS mPaymentDate " +
            "FROM payment JOIN expense ON payment.expense_id = expense.expense_id " +
            "WHERE payment_date BETWEEN :from AND :to AND expense.category_id=:categoryId")
    LiveData<List<PaymentItemModel>> getPaymentsInDateRange(
            LocalDate from, LocalDate to, long categoryId);

    @Insert
    void insertPayments(List<PaymentEntry> paymentEntryList);

    @Query("SELECT * FROM payment")
    List<PaymentEntry> debugLoadPayments();
}
