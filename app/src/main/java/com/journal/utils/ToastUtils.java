package com.journal.utils;

import android.widget.Toast;

/**
 * 类名称：com.exing.utils
 * 类描述：
 * 创建人：小土豆   QQ:515353776
 * 创建时间：2017.12.15
 */
public class ToastUtils {

    public static Toast mToast;

    public static void showToast(String msg){
        if (TextUtil.isValidate(msg)){
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setText(msg);
            mToast.show();
        }
    }

}
