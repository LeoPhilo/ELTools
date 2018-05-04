package com.projects.leophilo.eltools.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.projects.leophilo.eltools.R;
import com.projects.leophilo.eltools.core.Calculator;
import com.projects.leophilo.eltools.model.entity.NormalCompositionItemEntity;
import com.projects.leophilo.eltools.view.adapter.EditBarAutoAdapter;
import com.projects.leophilo.eltools.view.adapter.MainListAdapter;
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

    private MainListAdapter adapter;
    private MainPresenter presenter;
    private View.OnClickListener onCreateBarClickListener;


    @OnClick(R.id.button_add)
    public void add() {
        switch (addBtn.getText().toString()) {
            //defined in R.Strings.xml
            case "添加":
                boolean pass = presenter.checkEditBarData(volumeText.getText().toString());

                if (!pass) return;

                float volume = Float.parseFloat(volumeText.getText().toString());

                presenter.addNewItem(formulaText.getText().toString(), volume);

                adapter.notifyItemInserted(presenter.getCompositionItemEntities().size() - 1);
                adapter.notifyDataSetChanged();
                lv.smoothScrollToPosition(adapter.getItemCount() - 1);
                break;
            case "完成":
                final ArrayList<NormalCompositionItemEntity> entities = presenter.getCompositionItemEntities();
                if (entities != null) {
                    Calculator.calculate(entities, new Calculator.OnCalculateCallBack() {
                        @Override
                        public void onResult(final Calculator.ELData result, final float sum) {
                            if (null != MainFragment.this.getActivity())
                                MainFragment.this.getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
//                                        progressBar.setVisibility(View.GONE);
//                                        tipsTV.setVisibility(View.GONE);

                                        ResultDetailDialog dialog = ResultDetailDialog.newInstance(
                                                result.getLEL()
                                                , result.getUEL()
                                                , sum
                                                , entities);

                                        dialog.show(getChildFragmentManager(), ResultDetailDialog.TAG);
                                        dialog.setDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialog) {
                                                presenter.calculateComplete();
                                            }
                                        });
                                    }
                                });
                        }
                    });
                }
                break;
        }
    }


    public static MainFragment newInstance() {

        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void startEvent() {
        this.presenter = new MainPresenter();
        presenter.attachView(this);

        initList();

        initEditBar();
    }

    private void initEditBar() {
        EditBarAutoAdapter adapter = new EditBarAutoAdapter(this.getActivity(), null);

        formulaText.setThreshold(1);
        formulaText.setAdapter(adapter);

    }

    private void initList() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());

        adapter = new MainListAdapter(R.layout.item_main_recycler, presenter.getCompositionItemEntities());

        lv.setHasFixedSize(true);
        lv.setAdapter(adapter);
        lv.setLayoutManager(llm);

        adapter.setEmptyView(R.layout.view_empty_list, lv);
    }


    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_main;
    }

    @Override
    public void showToast(@NonNull String msg) {
        if (null != this.getActivity())
            Toast.makeText(
                    this.getActivity().getApplicationContext()
                    , msg
                    , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showCreateBar() {
        if (null == onCreateBarClickListener)
            onCreateBarClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCreateDialog();
                }
            };
        Snackbar.make(lv, "未查询到此成分信息", Snackbar.LENGTH_SHORT)
                .setAction("创建", onCreateBarClickListener).show();
    }

    private void showCreateDialog() {
        CreateItemDialog dialog = CreateItemDialog.newInstance(formulaText.getText().toString());
        dialog.show(getChildFragmentManager(), CreateItemDialog.TAG);
    }

    @Override
    public void updateVolumeTextView(String updateData) {
        volumeText.setText(updateData);
    }

    @Override
    public void resetEditBar() {
        formulaText.setVisibility(View.VISIBLE);
        volumeText.setVisibility(View.VISIBLE);
        formulaText.setText("");
        volumeText.setText("");
        addBtn.setText(R.string.button_add_state_1);
        formulaText.requestFocus();
    }

    @Override
    public void editComplete() {
        formulaText.setText("");
        volumeText.setText("");
        hideSoftInput();
        formulaText.setVisibility(View.GONE);
        volumeText.setVisibility(View.GONE);
        addBtn.setText(R.string.button_add_state_2);
    }

    @Override
    public void resetAll() {
        resetEditBar();
        adapter.notifyDataSetChanged();
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
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }
}
