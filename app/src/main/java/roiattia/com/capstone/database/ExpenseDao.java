package roiattia.com.capstone.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ExpenseDao {
    @Query("SELECT * FROM category")
    LiveData<List<ExpenseEntry>> loadAllExpenses();

    @Insert
    void insertExpense(ExpenseEntry expenseEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateExpense(ExpenseEntry expenseEntry);

    @Delete
    void deleteExpense(ExpenseEntry expenseEntry);
}
