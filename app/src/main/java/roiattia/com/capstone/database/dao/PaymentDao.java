package roiattia.com.capstone.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.entry.PaymentEntry;
import roiattia.com.capstone.model.OverallExpensesModel;
import roiattia.com.capstone.model.PaymentItemModel;

@Dao
public interface PaymentDao {

    // Return payment record with it's expense total number of payments
    @Query("SELECT payment_cost AS mCost, payment_number AS mPaymentNumber, " +
            "number_of_payments AS mTotalPayments, payment_date AS mPaymentDate, description AS mDescription " +
            "FROM payment JOIN expense ON payment.expense_id = expense.expense_id " +
            "WHERE payment_date BETWEEN :from AND :to AND expense.category_id=:categoryId")
    List<PaymentItemModel> loadPaymentByCategoryBetweenDates(
            LocalDate from, LocalDate to, long categoryId);

    @Insert
    void insertPayments(List<PaymentEntry> paymentEntryList);

    @Query("SELECT * FROM payment")
    List<PaymentEntry> debugLoadPayments();

    @Query("DELETE FROM payment")
    void deleteAllPayments();

    @Query("SELECT SUM(payment_cost) AS mCost FROM payment " +
            "WHERE payment_date BETWEEN :from AND :to")
    OverallExpensesModel getPaymentsBetweenDates(LocalDate from, LocalDate to);
}
