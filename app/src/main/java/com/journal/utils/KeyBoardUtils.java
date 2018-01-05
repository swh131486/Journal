package com.journal.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 类名称：com.exing.utils
 * 类描述：
 * 创建人：小土豆   QQ:515353776
 * 创建时间：2017.12.06
 */
public class KeyBoardUtils {

    /**
     * 隐藏小键盘
     */
    public static void hideKeyBoard(Context context, EditText editor) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editor.getWindowToken(), 0);
    }


    /**
     * 显示小键盘
     */
    public static void showKeyBoard(Context context, EditText editor) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editor,InputMethodManager.SHOW_FORCED);
    }

}
