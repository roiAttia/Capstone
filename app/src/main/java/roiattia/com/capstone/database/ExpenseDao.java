package roiattia.com.capstone.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import org.joda.time.LocalDate;

import java.util.List;

import roiattia.com.capstone.model.ExpensesModel;
import roiattia.com.capstone.model.OverallExpensesModel;
import roiattia.com.capstone.model.OverallIncomeModel;

@Dao
public interface ExpenseDao {

    @Query("SELECT * FROM expense")
    LiveData<List<ExpenseEntry>> loadAllExpenses();

    @Query("SELECT * FROM expense")
    List<ExpenseEntry> debugLoadAllExpenses();

    @Insert
    long[] insertExpenses(List<ExpenseEntry> expenseEntries);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateExpense(ExpenseEntry expenseEntry);

    @Delete
    void deleteExpense(ExpenseEntry expenseEntry);

    @Query("SELECT expense.category_id as mCategoryId, COUNT(category_name) as mCount, " +
            "category_name as mName, SUM(expense_cost) as mCost " +
            "FROM expense JOIN category ON expense.category_id = category.category_id " +
            "WHERE expense_payment_date BETWEEN :from AND :to " +
            "AND category_type=:type " +
            "GROUP BY category_name")
    LiveData<List<ExpensesModel>> loadExpensesBetweenDates(LocalDate from, LocalDate to,
                                                           CategoryEntry.Type type);

    @Query("SELECT * FROM expense " +
            "WHERE expense_id=:expenseId")
    LiveData<ExpenseEntry> loadExpenseById(Long expenseId);

    @Query("SELECT * FROM expense " +
            "WHERE expense_id IN (:ids) ")
    LiveData<List<ExpenseEntry>> loadExpensesById(long[] ids);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateExpenses(List<ExpenseEntry> expensesList);

    @Query("SELECT * FROM expense " +
            "WHERE category_id=:categoryId " +
            "AND expense_payment_date BETWEEN :from AND :to " +
            "ORDER BY expense_payment_date ASC")
    LiveData<List<ExpenseEntry>> loadExpensesByCategoryIdAndDates(
            Long categoryId, LocalDate from, LocalDate to);

    @Query("SELECT SUM(expense_cost) as mCost " +
            "FROM expense WHERE expense_payment_date BETWEEN :from AND :to")
    LiveData<OverallExpensesModel> loadExpensesBetweenDates(LocalDate from, LocalDate to);
}
