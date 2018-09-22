package roiattia.com.capstone.utils;

import java.text.DecimalFormat;

public class AmountUtils {

    public static String getStringFormatFromDouble(double amount){
        DecimalFormat formatter = new DecimalFormat("#,###,###.###");
        return formatter.format(amount);
    }

    public static double getDoubleFormatFromString(String amount){
        return Double.parseDouble(amount.replaceAll(",", ""));
    }
}
