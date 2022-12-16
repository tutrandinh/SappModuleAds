package com.ads.sapp.funtion;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class AdmodHelper {
    private static final String FILE_SETTING = "setting.pref";
    private static final String FILE_SETTING_ADMOD = "setting_admod.pref";
    private static final String IS_FIRST_OPEN = "IS_FIRST_OPEN";
    private static final String KEY_FIRST_TIME = "KEY_FIRST_TIME";

    /**
     * Trả về số click của 1 ads nào đó
     *
     * @param context
     * @param idAds
     * @return
     */
    public static int getNumClickAdsPerDay(Context context, String idAds) {
        return context.getSharedPreferences(FILE_SETTING_ADMOD, Context.MODE_PRIVATE).getInt(idAds, 0);
    }

    /**
     * Tăng số click trên 1 ads
     *
     * @param context
     * @param idAds
     */
    public static void increaseNumClickAdsPerDay(Context context, String idAds) {
        SharedPreferences pre = context.getSharedPreferences(FILE_SETTING_ADMOD, Context.MODE_PRIVATE);
        int count = pre.getInt(idAds, 0);
        pre.edit().putInt(idAds, count + 1).apply();
    }

    /**
     * nếu lần đầu mở app lưu thời gian đầu tiên vào SharedPreferences
     * nếu thời gian hiện tại so với thời gian đầu được 1 ngày thì reset lại data của admod.
     *
     * @param context
     */
    public static void setupAdmodData(Context context) {
        if (isFirstOpenApp(context)) {
            context.getSharedPreferences(FILE_SETTING_ADMOD, Context.MODE_PRIVATE).edit().putLong(KEY_FIRST_TIME, System.currentTimeMillis()).apply();
            context.getSharedPreferences(FILE_SETTING, Context.MODE_PRIVATE).edit().putBoolean(IS_FIRST_OPEN, true).apply();
            return;
        }
        long firstTime = context.getSharedPreferences(FILE_SETTING_ADMOD, Context.MODE_PRIVATE).getLong(KEY_FIRST_TIME, System.currentTimeMillis());
        long rs = System.currentTimeMillis() - firstTime;
       /*
       qua q ngày reset lại data
        */
        if (rs >= 24 * 60 * 60 * 1000) {
            resetAdmodData(context);
        }
    }


    private static void resetAdmodData(Context context) {
        context.getSharedPreferences(FILE_SETTING_ADMOD, Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(FILE_SETTING_ADMOD, Context.MODE_PRIVATE).edit().putLong(KEY_FIRST_TIME, System.currentTimeMillis()).apply();
    }

    private static boolean isFirstOpenApp(Context context) {
        return context.getSharedPreferences(FILE_SETTING, Context.MODE_PRIVATE).getBoolean(IS_FIRST_OPEN, false);
    }
}
