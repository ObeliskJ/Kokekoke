package com.obelisk.koukekouke.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.obelisk.koukekouke.MyApplication;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by Administrator on 2016/10/24.
 */

public abstract class BaseFragment extends Fragment {
    protected Context mContext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        RefWatcher refWatcher = MyApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
