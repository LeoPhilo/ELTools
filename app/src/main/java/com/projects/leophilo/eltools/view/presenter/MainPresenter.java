package com.projects.leophilo.eltools.view.presenter;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.projects.leophilo.eltools.model.entity.NormalCompositionItemEntity;
import com.projects.leophilo.eltools.model.entity.NormalCompositionItemEntityDao;
import com.projects.leophilo.eltools.model.greendao.GreenDaoHelper;
import com.projects.leophilo.eltools.view.presenter.contact.MainContact;

import org.greenrobot.greendao.query.Query;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class MainPresenter extends BasePresenter<MainContact.View> implements MainContact.Presenter {

    private Query<NormalCompositionItemEntity> query;
    private ArrayList<NormalCompositionItemEntity> mCompositionItemEntities;
    private float sum = 0;

    @Override
    public void attachView(MainContact.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {

    }

    @Override
    public void addNewItem(@NonNull String formula, float volume) {
        if (null == query)
            query = GreenDaoHelper.getDaoSession()
                    .getNormalCompositionItemEntityDao().queryBuilder()
                    .where(NormalCompositionItemEntityDao.Properties.Formula.eq(formula)).build();
        else
            query.setParameter(0, formula);

        NormalCompositionItemEntity entityToAdd = query.unique();

        if (null == entityToAdd) {
            mView.showCreateBar();
            return;
        }


        sum += volume;
        boolean containsEntity = mCompositionItemEntities.contains(entityToAdd);
        if (containsEntity) {
            entityToAdd.setValue(entityToAdd.getValue() + volume);
        } else {
            entityToAdd.setValue(volume);
            mCompositionItemEntities.add(entityToAdd);
        }

        if (sum == 100) {
            mView.editComplete();
        } else {
            mView.resetEditBar();
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

        float value = Float.parseFloat(volumeStr);
        float nSum = sum + value;
        if (nSum > 100) {
            value = value + 100 - nSum;
            value = new BigDecimal(value).setScale(3, RoundingMode.HALF_UP).floatValue();
            volumeStr = value + "";
            mView.updateVolumeTextView(volumeStr);
        }
        return true;
    }

    @Override
    public void calculateComplete() {
        mCompositionItemEntities.clear();
        sum = 0;
        mView.resetAll();
    }

    public ArrayList<NormalCompositionItemEntity> getCompositionItemEntities() {
        if (null == mCompositionItemEntities)
            mCompositionItemEntities = new ArrayList<>();
        return mCompositionItemEntities;
    }

}
