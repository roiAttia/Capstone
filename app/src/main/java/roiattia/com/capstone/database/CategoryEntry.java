package roiattia.com.capstone.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "category")
public class CategoryEntry {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "category_id")
    private long mCategoryId;
    @ColumnInfo(name = "category_name")
    private String mCategoryName;
    @ColumnInfo(name = "category_type")
    private Type mCategoryType;

    @Ignore
    public CategoryEntry(@NonNull String categoryName, Type categoryType) {
        mCategoryName = categoryName;
        mCategoryType = categoryType;
    }

    public CategoryEntry(long categoryId, String categoryName, Type categoryType) {
        mCategoryId = categoryId;
        mCategoryName = categoryName;
        mCategoryType = categoryType;
    }

    @Ignore
    public CategoryEntry() { }

    public enum Type{
        JOB(0),
        EXPENSE(1);

        private int code;

        Type(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    public long getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(long categoryId) {
        this.mCategoryId = categoryId;
    }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setCategoryName(String categoryName) {
        this.mCategoryName = categoryName;
    }

    public Type getCategoryType() {
        return mCategoryType;
    }

    public void setCategoryType(Type categoryType) {
        mCategoryType = categoryType;
    }

    @Override
    public String toString() {
        return "**CATEGORY ENTRY** Category mCategoryId: " + mCategoryId + "Category type: " + mCategoryType + ", Category name: " + mCategoryName;
    }
}
