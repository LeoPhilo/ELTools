package com.projects.leophilo.eltools.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.projects.leophilo.eltools.R;
import com.projects.leophilo.eltools.core.Calculator;
import com.projects.leophilo.eltools.model.entity.NormalCompositionItemEntity;
import com.projects.leophilo.eltools.view.adapter.EditBarAutoAdapter;
import com.projects.leophilo.eltools.view.adapter.MainListAdapter;
import com.projects.leophilo.eltools.view.anim.AnimHelper;
import com.projects.leophilo.eltools.view.base.BaseFragment;
import com.projects.leophilo.eltools.view.dialog.CreateItemDialog;
import com.projects.leophilo.eltools.view.dialog.ResultDetailDialog;
import com.projects.leophilo.eltools.view.presenter.MainPresenter;
import com.projects.leophilo.eltools.view.presenter.contact.MainContact;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class MainFragment extends BaseFragment implements MainContact.View {

    @BindView(R.id.list_view)
    RecyclerView lv;
    @BindView(R.id.textFormula)
    AutoCompleteTextView formulaText;
    @BindView(R.id.text_volume)
    EditText volumeText;
    @BindView(R.id.button_add)
    Button addBtn;
    @BindView(R.id.floatingToolbar)
    FloatingToolbar floatingToolbar;
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    private MainPresenter presenter;
    private AlertDialog createTipDialog;

    // TODO: View层初始化部分***********************

    public MainFragment() {
    }

    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_main;
    }

    @Override
    protected void startEvent() {
        this.presenter = new MainPresenter();
        presenter.attachView(this);

        initList();
        initFab();
        initEditBar();
    }

    private void initFab() {
        floatingToolbar.attachFab(floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //与操作状态相关的点击事件交由MainPresenter管理
                presenter.callOnFabClick();
            }
        });
    }

    private void initEditBar() {
        EditBarAutoAdapter adapter = new EditBarAutoAdapter(this.getActivity(), null);

        formulaText.setThreshold(1);
        formulaText.setAdapter(adapter);
    }

    private void initList() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());

        MainListAdapter adapter = presenter.initAdapter();

        lv.setHasFixedSize(true);
        lv.setAdapter(adapter);
        lv.setLayoutManager(llm);

        adapter.setEmptyView(R.layout.view_empty_list, lv);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    // TODO: View协议接口实现部分**************************

    @OnClick(R.id.button_shrink)
    void shrinkEditBar() {
        floatingToolbar.hide();
    }

    @OnClick(R.id.button_add)
    void add() {
        boolean pass = presenter.checkEditBarData(volumeText.getText().toString());

        if (!pass) return;

        double volume = Double.parseDouble(volumeText.getText().toString());

        presenter.addNewItem(formulaText.getText().toString(), volume);
    }

    @Override
    public void showToast(@NonNull final String msg) {
        if (null != this.getActivity())
            this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(
                            MainFragment.this.getActivity()
                            , msg
                            , Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public void showCreateTip() {
        if (null == getActivity()) return;
        if (null == createTipDialog) {
            createTipDialog =
                    new AlertDialog
                            .Builder(getActivity())
                            .setMessage("未查询到此成分信息, 需要创建新的成分信息吗?")
                            .setPositiveButton("创建", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    showCreateDialogImpl();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
        }
        if (!createTipDialog.isShowing()) {
            createTipDialog.show();
        }
    }

    private void showCreateDialogImpl() {
        CreateItemDialog dialog = CreateItemDialog.newInstance(formulaText.getText().toString());
        dialog.show(getChildFragmentManager(), CreateItemDialog.TAG);
    }

    @Override
    public void showEditBar() {
        floatingToolbar.show();
    }

    @Override
    public void showResult(final Calculator.ELData result, final double sum, final ArrayList<NormalCompositionItemEntity> entities) {
        if (null == getActivity()) return;
        ResultDetailDialog dialog = ResultDetailDialog.newInstance(
                Double.toString(result.getLEL()),
                Double.toString(result.getUEL()),
                Double.toString(sum)
                , entities);

        dialog.show(getChildFragmentManager(), ResultDetailDialog.TAG);
        dialog.setDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                presenter.calculateComplete();
            }
        });
    }

    @Override
    public void showManagingStateChangeAnim(View itemView, int itemType, boolean expectToDelete) {
        floatingToolbar.hide();
        FrameLayout logoView = itemView.findViewById(R.id.logo);
        if (null == getContext() || null == logoView) return;
        AnimHelper.getInstance().animMainItemStateChange(getContext(), logoView, expectToDelete, itemType);
    }

    @Override
    public void updateVolumeTextView(String updateData) {
        volumeText.setText(updateData);
    }

    @Override
    public void resetEditBar(boolean requestFocus) {
        formulaText.setText("");
        volumeText.setText("");
        if (requestFocus)
            formulaText.requestFocus();
        else {
            hideSoftInput();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    floatingActionButton.setImageResource(R.drawable.ic_check_black_24dp);
                    floatingToolbar.hide();
                }
            }, 500);

        }
    }

    private void hideSoftInput() {
        // Check if no view has focus:
        if (null == getActivity()) return;
        View view = this.getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void reEditItem(NormalCompositionItemEntity entity) {
        if (!floatingToolbar.isShowing()) floatingToolbar.show();
        formulaText.setText(entity.getFormula());
        volumeText.setText(String.valueOf(entity.getValue()));
        volumeText.requestFocus();
    }

    @Override
    public void notifyEditStateChanged(final int state) {
        if (null == getActivity()) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (state) {
                    case MainPresenter.editState_editing:
                        floatingActionButton.setImageResource(R.drawable.ic_add_black_24dp);
                        break;
                    case MainPresenter.editState_complete:
                        floatingActionButton.setImageResource(R.drawable.ic_check_black_24dp);
                        break;
                    case MainPresenter.editState_managing:
                        floatingActionButton.setImageResource(R.drawable.ic_delete_black_24dp);
                        break;
                }
            }
        });
    }

}
