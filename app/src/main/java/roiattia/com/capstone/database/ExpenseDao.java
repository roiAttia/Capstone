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

@Dao
public interface ExpenseDao {
    @Query("SELECT * FROM expense")
    LiveData<List<ExpenseEntry>> loadAllExpenses();

    @Query("SELECT * FROM expense")
    List<ExpenseEntry> debugLoadAllExpenses();

    @Insert
    void insertExpense(ExpenseEntry expenseEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateExpense(ExpenseEntry expenseEntry);

    @Delete
    void deleteExpense(ExpenseEntry expenseEntry);

    @Query("SELECT COUNT(category_name) as mCount, category_name as mName, SUM(expense_cost) as mCost " +
            "FROM expense JOIN category ON expense.category_id = category.category_id " +
            "WHERE expense_payment_date BETWEEN :from AND :to " +
            "AND category_type=:type " +
            "GROUP BY category_name")
    LiveData<List<ExpensesModel>> loadExpensesBetweenDates(LocalDate from, LocalDate to,
                                                           CategoryEntry.Type type);

    @Query("SELECT * FROM expense WHERE expense_id=:expenseId")
    LiveData<ExpenseEntry> loadExpense(long expenseId);
}
