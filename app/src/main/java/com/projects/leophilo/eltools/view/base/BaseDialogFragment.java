package com.projects.leophilo.eltools.view.base;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseDialogFragment extends DialogFragment {

    private Unbinder unbinder;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (null != dialog.getWindow())
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (null == getActivity()) return null;

        View root = attachLayoutRes() == 0 ?
                null :
                getActivity().getLayoutInflater().inflate(attachLayoutRes(), container, false);
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
