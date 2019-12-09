package com.onmobile.rbt.baseline.adapter.base;

import android.view.ViewGroup;

import com.onmobile.rbt.baseline.fragment.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Shahbaz Akhtar on 18/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public class SimpleFragmentPagerAdapter<T extends BaseFragment> extends FragmentStatePagerAdapter {

    private List<String> titles;
    private List<T> list;

    public SimpleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        titles = new ArrayList<>();
        list = new ArrayList<>();
    }

    @Override
    public T getItem(int i) {
        return list.get(i);
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    public void removeItem(@NonNull T fragment) {
        list.remove(fragment);
    }

    public void removeItem(int index) {
        if (titles.size() > index)
            titles.remove(index);
        if (list.size() > index)
            list.remove(index);
    }

    public void addFragment(@NonNull T fragment) {
        addFragment(null, fragment);
    }

    public void addFragments(List<T> items) {
        if (items == null)
            return;
        for (T fragment : items) {
            addFragment(null, fragment);
        }
    }

    public void removeItem(String title, @NonNull T fragment) {
        titles.remove(title);
        list.remove(fragment);
    }

    public void addFragment(String title, @NonNull T fragment) {
        titles.add(title);
        list.add(fragment);
    }

    public void addFragments(List<String> titles, List<T> items) {
        if (titles == null || items == null)
            return;
        for (int i = 0; i < items.size(); i++) {
            addFragment(titles.get(i), items.get(i));
        }
    }

    public void clearItems() {
        titles.clear();
        list.clear();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        if (position >= getCount()) {
            FragmentManager manager = ((Fragment) object).getFragmentManager();
            if (manager != null) {
                FragmentTransaction trans = manager.beginTransaction();
                trans.remove((Fragment) object);
                trans.commit();
            }
        }
    }

}