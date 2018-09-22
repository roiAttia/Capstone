package roiattia.com.capstone.database;

import android.arch.persistence.room.TypeConverter;

import roiattia.com.capstone.database.entry.CategoryEntry;

import static roiattia.com.capstone.database.entry.CategoryEntry.Type.EXPENSE;
import static roiattia.com.capstone.database.entry.CategoryEntry.Type.JOB;

public class CategoryTypeConverter {
    @TypeConverter
    public static CategoryEntry.Type toType(int type) {
        if (type == JOB.getCode()) {
            return JOB;
        } else if (type == EXPENSE.getCode()) {
            return EXPENSE;
        } else {
            throw new IllegalArgumentException("Could not recognize category");
        }
    }

    @TypeConverter
    public static Integer toInteger(CategoryEntry.Type type) {
        return type.getCode();
    }
}
