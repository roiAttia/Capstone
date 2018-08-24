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
public interface CategoryDao {

    @Query("SELECT * FROM category WHERE category_type=:type ORDER BY category_name")
    LiveData<List<CategoryEntry>> loadCategories(CategoryEntry.Type type);

    @Query("SELECT * FROM category")
    List<CategoryEntry> debugLoadCategories();

    @Insert
    long insertCategory(CategoryEntry categoryEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCategory(CategoryEntry categoryEntry);

    @Delete
    void deleteCategory(CategoryEntry categoryEntry);

    @Query("SELECT category_name FROM category WHERE category_id=:categoryId")
    String getCategoryName(long categoryId);
}
