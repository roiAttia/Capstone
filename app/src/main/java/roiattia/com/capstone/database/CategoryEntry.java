package roiattia.com.capstone.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "category")
public class CategoryEntry {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String mName;
    private Type mType;

    @Ignore
    public CategoryEntry(String name, Type type) {
        mName = name;
        mType = type;
    }

    public CategoryEntry(long id, String name, Type type) {
        this.id = id;
        mName = name;
        mType = type;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Type getType() {
        return mType;
    }

    @Override
    public String toString() {
        return "**CATEGORY ENTRY** Category id: " + id + "Category type: " + mType + ", Category name: " +mName;
    }
}
