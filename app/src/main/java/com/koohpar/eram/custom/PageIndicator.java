package com.koohpar.eram.custom;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by Zagrous Anahita on 16/2/16.
 */
public interface PageIndicator extends ViewPager.OnPageChangeListener {
    void setViewPager(ViewPager var1);

    void setViewPager(ViewPager var1, int var2);

    void setCurrentItem(int var1);

    void setOnPageChangeListener(ViewPager.OnPageChangeListener var1);

    void notifyDataSetChanged();
}

