package roiattia.com.capstone.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.database.entry.CategoryEntry;
import roiattia.com.capstone.database.entry.ExpenseEntry;
import roiattia.com.capstone.model.ExpandableListChild;
import roiattia.com.capstone.model.ExpenseListModel;
import roiattia.com.capstone.model.ExpensesModel;
import roiattia.com.capstone.model.OverallExpensesModel;

@Dao
public interface ExpenseDao {

    @Query("SELECT * FROM expense")
    LiveData<List<ExpenseEntry>> loadAllExpenses();

    @Query("SELECT * FROM expense")
    List<ExpenseEntry> debugLoadAllExpenses();

    @Insert
    long[] insertExpenses(List<ExpenseEntry> expenseEntries);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertExpense(ExpenseEntry expenseEntrie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateExpense(ExpenseEntry expenseEntry);

    @Query("DELETE FROM expense WHERE expense_id = :expenseId")
    void deleteExpense(long expenseId);

    @Query("SELECT expense.category_id as mCategoryId, COUNT(category_name) as mCount, " +
            "category_name as mName, SUM(expense_cost) as mCost " +
            "FROM expense JOIN category ON expense.category_id = category.category_id " +
            "WHERE expense_first_payment_date BETWEEN :from AND :to " +
            "AND category_type=:type " +
            "GROUP BY category_name")
    LiveData<List<ExpensesModel>> loadPaymentsBetweenDates(LocalDate from, LocalDate to,
                                                           CategoryEntry.Type type);

    @Query("SELECT expense.category_id as mCategoryId, COUNT(category_name) as mCount, " +
            "category_name as mName, SUM(payment_cost) as mCost " +
            "FROM category JOIN expense ON category.category_id = expense.category_id " +
            "JOIN payment ON expense.expense_id = payment.expense_id " +
            "WHERE payment.payment_date BETWEEN :from AND :to " +
            "GROUP BY category_name")
    List<ExpensesModel> loadPaymentsPerCategoryBetweenDates(LocalDate from, LocalDate to);

    @Query("SELECT * FROM expense " +
            "WHERE expense_id=:expenseId")
    ExpenseEntry loadExpenseById(Long expenseId);

    @Query("SELECT * FROM expense " +
            "WHERE expense_id=:expenseId")
    LiveData<ExpenseEntry> loadExpenseByIdLiveData(Long expenseId);

    @Query("SELECT * FROM expense " +
            "WHERE expense_id IN (:ids) ")
    LiveData<List<ExpenseEntry>> loadExpensesById(long[] ids);

    @Query("SELECT * FROM expense " +
            "WHERE category_id=:categoryId " +
            "AND expense_first_payment_date BETWEEN :from AND :to " +
            "ORDER BY expense_first_payment_date ASC")
    LiveData<List<ExpenseEntry>> loadExpensesByCategoryIdAndDates(
            Long categoryId, LocalDate from, LocalDate to);

    @Query("SELECT SUM(payment_cost) as mCost " +
            "FROM payment WHERE payment_date BETWEEN :from AND :to")
    LiveData<OverallExpensesModel> loadPaymentsBetweenDates(LocalDate from, LocalDate to);

    @Query("SELECT SUM(expense_cost) AS mCost FROM expense " +
            "WHERE expense_first_payment_date >=:start AND expense_last_payment_date >=:end")
    OverallExpensesModel loadExpensesBetweenDates(LocalDate start, LocalDate end);

    @Query("SELECT expense_id as mExpenseId, description as mDescription, expense_cost as mExpenseCost, " +
            "number_of_payments as mNumberOfPayments, expense_monthly_cost as mMonthlyCost," +
            "expense_first_payment_date as mExpenseFirstPayment, expense_last_payment_date " +
            "as mExpenseLastPayment, category_name as mCategoryName FROM expense " +
            "JOIN category ON expense.category_id = category.category_id " +
            "WHERE number_of_payments > 1 ORDER BY expense_last_payment_date DESC")
    LiveData<List<ExpenseListModel>> loadExpenses();

    @Query("DELETE FROM expense")
    void deleteAllExpenses();

    @Query("SELECT expense_id AS mExpenseId, description AS mDescription, expense_cost AS mCost, " +
            "category_name AS mCategoryName FROM expense JOIN category ON " +
            "expense.category_id = category.category_id " +
            "WHERE expense_id IN (:ids) ")
    List<ExpandableListChild> loadExpensesByIds(Long[] ids);

    @Query("SELECT expense.expense_id as mExpenseId, category.category_name as mCategoryName, " +
            "expense.description as mDescription, expense.expense_cost as mCost " +
            "FROM expense JOIN category ON expense.category_id = category.category_id " +
            "WHERE expense.expense_id =:expenseId")
    ExpandableListChild loadExpenseChildById(long expenseId);
}
