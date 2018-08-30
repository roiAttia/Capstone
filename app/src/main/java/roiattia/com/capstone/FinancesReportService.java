package roiattia.com.capstone;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import org.joda.time.LocalDate;

import roiattia.com.capstone.database.AppDatabase;
import roiattia.com.capstone.database.JobDao;
import roiattia.com.capstone.model.OverallIncomeModel;
import roiattia.com.capstone.ui.finances.FinancesRepository;
import roiattia.com.capstone.ui.finances.FinancesViewModel;
import roiattia.com.capstone.utils.AppExecutors;
import roiattia.com.capstone.utils.DateUtils;
import roiattia.com.capstone.utils.InjectorUtils;

public  class FinancesReportService extends IntentService {

    public static final String ACTION_GET_REPORT =
            "roiattia.com.capstone.action.get_report";
    private LocalDate mStartDate;
    private LocalDate mEndDate;

    public FinancesReportService() {
        super("FinancesReportService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_REPORT.equals(action)) {
                handleActionProvideReport();
            }
        }
    }

    public void handleActionProvideReport() {
        mStartDate = DateUtils.getStartDateOfTheMonth();
        mEndDate = DateUtils.getEndDateOfTheMonth();
        final AppDatabase database = AppDatabase.getsInstance(this);
        AppExecutors appExecutors = AppExecutors.getInstance();
        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                OverallIncomeModel model =
                        database.jobDao().loadReportBetweenDates(mStartDate, mEndDate);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(FinancesReportService.this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                        new ComponentName(FinancesReportService.this, FinancesWidgetProvider.class));
                //Now update all widgets
                FinancesWidgetProvider.updateFinancialWidgets(
                        FinancesReportService.this, appWidgetManager, model, appWidgetIds);
            }
        });
    }

    public static void startActionProvideReport(Context context) {
        Intent intent = new Intent(context, FinancesReportService.class);
        intent.setAction(ACTION_GET_REPORT);
        context.startService(intent);
    }
}
