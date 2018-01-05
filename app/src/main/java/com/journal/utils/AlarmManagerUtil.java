package com.journal.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * 类名称：com.journal.utils.AlarmManagerUtil
 * 类描述：
 * 创建人：小土豆   QQ:515353776
 * 创建时间：2017.12.06
 */
public class AlarmManagerUtil {
    public static final String ALARM_ACTION = "com.journal.alarm.clock";

    /**
     * 设置闹钟
     * @param context
     * @param startTime 提醒时间
     * @param isVibrator 是否震动
     * @param tips  提示信息
     * @param id    提醒id （可以用此id来取消提醒）
     */
    public static void setAlarm(Context context, long startTime, boolean isVibrator, String tips, int id) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(ALARM_ACTION);
        intent.putExtra(Constants.KEY_TIPS_SERVICE, tips);
        PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, PendingIntent
                .FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, startTime, sender);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, startTime, sender);
        }
    }


    /**
     * 取消提醒
     * @param context
     * @param id
     */
    public static void cancelAlarm(Context context, int id) {
        Intent intent = new Intent(ALARM_ACTION);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent
                .FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi);
    }


}
