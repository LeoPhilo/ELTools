package com.projects.leophilo.eltools.view.dialog;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.projects.leophilo.eltools.R;

public class CreateItemDialog extends BottomSheetDialogFragment {

    // TODO: 2018/4/28 完成Fab点击后打开此页面进行Item创建
    public static final String TAG = "DialogCreateItem";

    public static CreateItemDialog newInstance(
            ) {

        Bundle args = new Bundle();

        CreateItemDialog fragment = new CreateItemDialog();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.dialog_create_new_item, container, false);

        return root;
    }
}
