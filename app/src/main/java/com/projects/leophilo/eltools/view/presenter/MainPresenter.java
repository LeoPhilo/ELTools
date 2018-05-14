package com.projects.leophilo.eltools.view.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.projects.leophilo.eltools.R;
import com.projects.leophilo.eltools.core.Arith;
import com.projects.leophilo.eltools.core.Calculator;
import com.projects.leophilo.eltools.model.entity.NormalCompositionItemEntity;
import com.projects.leophilo.eltools.model.entity.NormalCompositionItemEntityDao;
import com.projects.leophilo.eltools.model.greendao.GreenDaoHelper;
import com.projects.leophilo.eltools.view.adapter.MainListAdapter;
import com.projects.leophilo.eltools.view.presenter.contact.MainContact;

import org.greenrobot.greendao.query.Query;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class MainPresenter extends BasePresenter<MainContact.View> implements MainContact.Presenter {

    private Query<NormalCompositionItemEntity> query;

    private MainListAdapter adapter;
    private ArrayList<NormalCompositionItemEntity> mExpectToDeleteEntities;
    private ArrayList<Integer> mExpectToDeleteEntitiesPositions;
    private double sum = 0;
    private int currentEditState;
    public static final int editState_editing = 0x1;
    public static final int editState_complete = 0x2;
    public static final int editState_managing = 0x3;

    // TODO: Presenter层初始化部分******************************************

    public MainPresenter() {
        currentEditState = editState_editing;
    }

    @Override
    public void attachView(MainContact.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {

    }

    @Override
    public void addNewItem(@NonNull String formula, double volume) {
        if (null == query)
            query = GreenDaoHelper.getDaoSession()
                    .getNormalCompositionItemEntityDao().queryBuilder()
                    .where(NormalCompositionItemEntityDao.Properties.Formula.eq(formula)).build();
        else
            query.setParameter(0, formula);

        NormalCompositionItemEntity entityToAdd = query.unique();

        if (null == entityToAdd) {
            mView.showCreateTip();
            return;
        }

        sum = Arith.add(sum, volume);
        boolean containsEntity = adapter.getData().contains(entityToAdd);
        entityToAdd.setValue(volume);
        if (containsEntity) {
            //如果当前列表包含正在编辑的Item, 则对此Item的数值进行更新
            adapter.notifyItemChanged(adapter.getData().indexOf(entityToAdd));
        } else {
            //如果当前列表不包含正在编辑的Item, 则为新Item, 添加到列表
            adapter.addData(entityToAdd);
        }

        if (sum == 100) {
            currentEditState = editState_complete;
            mView.resetEditBar(false);
        } else {
            mView.resetEditBar(true);
        }
    }

    @Override
    public boolean checkEditBarData(String volumeStr) {
        //检查体积分数, 若不填/超出范围则验证失败
        if (TextUtils.isEmpty(volumeStr)
                || Float.parseFloat(volumeStr) <= 0
                || Float.parseFloat(volumeStr) > 100) {
            mView.showToast("请输入0~100内的体积分数");
            mView.updateVolumeTextView("");
            return false;
        }

        double value = Double.parseDouble(volumeStr);
        double nSum = Arith.add(sum, value);
        if (nSum > 100) {
            value = value + 100 - nSum;
        }
        value = Arith.round(value, Calculator.DecimalScale);
        volumeStr = value + "";
        mView.updateVolumeTextView(volumeStr);
        return true;
    }

    @Override
    public void calculateComplete() {
        //流程结束, 重置
        int size = adapter.getData().size();
        adapter.getData().clear();
        adapter.notifyItemRangeRemoved(0, size);
        sum = 0;
        checkEditState(editState_editing);
        mView.resetEditBar(true);
    }

    @Override
    public MainListAdapter initAdapter() {
        adapter = new MainListAdapter(R.layout.item_main_recycler, null);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //与操作状态相关的点击事件交由MainPresenter管理
                callOnItemClick(view, position);
            }
        });
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                //与操作状态相关的点击事件交由MainPresenter管理
                callOnItemLongClick(view, position);
                return true;
            }
        });
        return adapter;
    }

    @Override
    public void callOnFabClick() {
        switch (currentEditState) {
            case editState_editing:
                mView.showEditBar();
                break;
            case editState_complete:
                Calculator.calculate(adapter.getData(), new Calculator.OnCalculateCallBack() {
                    @Override
                    public void onResult(Calculator.ELData result, double sum) {
                        mView.showResult(result, sum, (ArrayList<NormalCompositionItemEntity>) adapter.getData());
                    }

                    @Override
                    public void onError(String description) {
                        mView.showToast(description);
                        checkEditState(editState_editing);
                    }
                });
                break;
            case editState_managing:
                adapter.getData().removeAll(mExpectToDeleteEntities);
                int x = 0;
                for (int i : mExpectToDeleteEntitiesPositions) {
                    adapter.notifyItemRemoved(i);
                    adapter.notifyItemChanged(i);
                    sum -= mExpectToDeleteEntities.get(x++).getValue();
                }
                mExpectToDeleteEntities.clear();
                mExpectToDeleteEntitiesPositions.clear();
                checkEditState(editState_editing);
                break;
        }
    }

    private void callOnItemClick(View itemView, int position) {
        switch (currentEditState) {
            case editState_editing:
            case editState_complete:
                sum = Arith.sub(sum, adapter.getData().get(position).getValue());
                checkEditState(editState_editing);
                mView.reEditItem(adapter.getData().get(position));
                break;
            case editState_managing:
                manageExpectToDeleteEntity(itemView, position, adapter.getData().get(position));
                break;
        }
    }

    private void callOnItemLongClick(View itemView, int position) {
        //长按Item进入管理状态
        checkEditState(editState_managing);
        manageExpectToDeleteEntity(itemView, position, adapter.getData().get(position));
    }

    private void manageExpectToDeleteEntity(View itemView, int position, NormalCompositionItemEntity entity) {
        if (null == mExpectToDeleteEntities)
            mExpectToDeleteEntities = new ArrayList<>();
        if (null == mExpectToDeleteEntitiesPositions)
            mExpectToDeleteEntitiesPositions = new ArrayList<>();
        boolean containsEntity = mExpectToDeleteEntities.contains(entity);
        if (containsEntity) {
            int index = mExpectToDeleteEntitiesPositions.indexOf(position);
            mExpectToDeleteEntities.remove(entity);
            mExpectToDeleteEntitiesPositions.remove(index);
            checkExpectToDeleteListSize();
        } else {
            mExpectToDeleteEntities.add(entity);
            mExpectToDeleteEntitiesPositions.add(position);
        }
        mView.showManagingStateChangeAnim(itemView, entity.getType(), !containsEntity);

    }

    private void checkExpectToDeleteListSize() {
        if (mExpectToDeleteEntities.size() == 0) {
            checkEditState(editState_editing);
        }
    }

    private void checkEditState(int editState) {
        if (currentEditState == editState) return;
        currentEditState = editState;
        mView.notifyEditStateChanged(currentEditState);
    }
}
