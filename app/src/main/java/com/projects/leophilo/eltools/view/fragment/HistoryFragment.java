package com.projects.leophilo.eltools.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.projects.leophilo.eltools.R;
import com.projects.leophilo.eltools.core.Calculator;
import com.projects.leophilo.eltools.model.entity.HistoryResultEntity;
import com.projects.leophilo.eltools.model.entity.HistoryResultEntityDao;
import com.projects.leophilo.eltools.model.entity.NormalCompositionItemEntity;
import com.projects.leophilo.eltools.model.greendao.GreenDaoHelper;
import com.projects.leophilo.eltools.view.adapter.HistoryListAdapter;
import com.projects.leophilo.eltools.view.base.BaseFragment;
import com.projects.leophilo.eltools.view.dialog.ResultDetailDialog;

import org.greenrobot.greendao.query.Query;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HistoryFragment extends BaseFragment {

    @BindView(R.id.list_view)
    RecyclerView listView;

    private final int TOTAL_COUNTER = 20;
    private int pageCount = 0;

    public static HistoryFragment newInstance() {

        Bundle args = new Bundle();

        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void startEvent() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());

        Query<HistoryResultEntity> query = GreenDaoHelper.getDaoSession()
                .getHistoryResultEntityDao()
                .queryBuilder()
                .limit(TOTAL_COUNTER).offset(pageCount++ * TOTAL_COUNTER)
                .orderDesc(HistoryResultEntityDao.Properties.Id)
                .build();
        List<HistoryResultEntity> listToAdd = query.list();

        final HistoryListAdapter adapter = new HistoryListAdapter(R.layout.item_history_recycler, listToAdd);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (adapter instanceof HistoryListAdapter) {
                    Type listType = new TypeToken<ArrayList<NormalCompositionItemEntity>>() {
                    }.getType();
                    ArrayList<NormalCompositionItemEntity> entities = new Gson()
                            .fromJson(((HistoryListAdapter) adapter).getItem(position).getDetail(), listType);

                    Calculator.ELResult result = new Calculator.ELResult(
                            Double.parseDouble(((HistoryListAdapter) adapter).getItem(position).getTemperature()),
                            Double.parseDouble(((HistoryListAdapter) adapter).getItem(position).getPressure()),
                            new Calculator.ELData(
                                    Double.parseDouble(((HistoryListAdapter) adapter).getItem(position).getLEL()),
                                    Double.parseDouble(((HistoryListAdapter) adapter).getItem(position).getUEL())
                            ),
                            Double.parseDouble(((HistoryListAdapter) adapter).getItem(position).getSum())
                    );

                    ResultDetailDialog dialog = ResultDetailDialog.newInstance(
                            result, entities);

                    dialog.setHideSaveAction(true);
                    dialog.show(getChildFragmentManager(), ResultDetailDialog.TAG);
                }
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                Query<HistoryResultEntity> query = GreenDaoHelper.getDaoSession()
                        .getHistoryResultEntityDao()
                        .queryBuilder()
                        .limit(TOTAL_COUNTER).offset(pageCount++ * TOTAL_COUNTER)
                        .orderDesc(HistoryResultEntityDao.Properties.Id)
                        .build();
                List<HistoryResultEntity> listToAdd = query.list();

                if (listToAdd.size() < TOTAL_COUNTER
                        && listToAdd.size() > 0) {
                    adapter.addData(listToAdd);
                    adapter.loadMoreEnd();
                } else if (listToAdd.size() == TOTAL_COUNTER) {
                    adapter.addData(listToAdd);
                    adapter.loadMoreComplete();
                } else {
                    adapter.loadMoreEnd();
                }
            }
        }, listView);

        listView.setAdapter(adapter);
        listView.setLayoutManager(llm);

        adapter.setEmptyView(R.layout.view_empty_list, listView);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragemnt_history;
    }
}
