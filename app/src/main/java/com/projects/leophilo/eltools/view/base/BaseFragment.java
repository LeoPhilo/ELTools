package com.projects.leophilo.eltools.view.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = attachLayoutRes() == 0 ?
                null :
                inflater.inflate(attachLayoutRes(), container, false);
        if (root != null) {
            unbinder = ButterKnife.bind(this, root);
        }
        startEvent();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != unbinder)
            unbinder.unbind();
    }

    protected abstract void startEvent();

    @LayoutRes
    protected abstract int attachLayoutRes();
}
