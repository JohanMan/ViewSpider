package com.johan.view.spider;

import android.app.Activity;
import android.view.View;

public interface ViewSpider {

    /**
     * 通过 Activity 查找 View
     * @param activity Activity
     */
    void find(Activity activity);

    /**
     * 通过 View 查找 View
     * @param view View
     */
    void find(View view);

}
