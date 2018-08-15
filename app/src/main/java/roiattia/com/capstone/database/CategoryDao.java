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

    @Query("SELECT * FROM category WHERE mType=:type ORDER BY mName")
    LiveData<List<CategoryEntry>> loadCategories(CategoryEntry.Type type);

    @Insert
    long insertCat(CategoryEntry categoryEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateCat(CategoryEntry categoryEntry);

    @Delete
    void deleteCat(CategoryEntry categoryEntry);

    @Query("SELECT mName FROM category WHERE id=:categoryId")
    String getCategoryName(int categoryId);
}
