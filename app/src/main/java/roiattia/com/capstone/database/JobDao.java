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

@Dao
public interface JobDao {

    @Query("SELECT * FROM job ORDER BY mProfit")
    LiveData<List<JobEntry>> loadAllJobs();

    @Query("SELECT * FROM job ORDER BY mProfit")
    List<JobEntry> debugLoadAllJobs();

    @Insert
    long insertJob(JobEntry jobEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateJob(JobEntry jobEntry);

    @Delete
    void deleteJob(JobEntry jobEntry);

    @Query("SELECT * FROM job WHERE job_date=:localDate ORDER BY mIncome")
    LiveData<List<JobEntry>> loadJobsAtDate(LocalDate localDate);
}
