package roiattia.com.capstone.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import roiattia.com.capstone.R;
import roiattia.com.capstone.model.OverallIncomeModel;
import roiattia.com.capstone.ui.finances.FinancesActivity;
import roiattia.com.capstone.utils.AmountUtils;

/**
 * Implementation of App Widget functionality.
 */
public class FinancesWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                OverallIncomeModel model, int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.finances_widget_provider);
        views.setTextViewText(R.id.tv_widget_profits, "PROFITS: " +
                AmountUtils.getStringFormatFromDouble(model.getProfit()));

        Intent intent = new Intent(context, FinancesActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.ll_data, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateFinancialWidgets(Context context, AppWidgetManager appWidgetManager,
                                          OverallIncomeModel model, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, model, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        FinancesReportService.startActionProvideReport(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        FinancesReportService.startActionProvideReport(context);

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

