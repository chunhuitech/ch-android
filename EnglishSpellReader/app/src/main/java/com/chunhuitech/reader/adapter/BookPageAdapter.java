package com.chunhuitech.reader.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;
import java.util.Map;

public class BookPageAdapter extends FragmentPagerAdapter {

    public List<Fragment> mFragmentList;
    List<Map<String, Object>> listPageData;
    String bookId;

    public BookPageAdapter(FragmentManager fm, List<Fragment> mFragmentList, List<Map<String, Object>> listPageData, String bookId) {
        super(fm);
        this.mFragmentList = mFragmentList;
        this.listPageData = listPageData;
        this.bookId = bookId;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment currentFragment = mFragmentList.get(i);
        int listIndex = (int)Math.floor(i/2);
        Map<String, Object> data = listPageData.get(listIndex);
        Bundle bundle = new Bundle();
        bundle.putString("bookId", bookId);
        bundle.putString("id", data.get("id").toString());
        bundle.putString("page", data.get("page").toString());
        bundle.putString("imageUrl", data.get("imageUrl").toString());
        bundle.putString("half", i % 2 == 0 ? "left" : "right");
        currentFragment.setArguments(bundle);
        return currentFragment;
    }

    @Override
    public int getCount() {
        return mFragmentList==null ? 0 : mFragmentList.size();
    }
}
