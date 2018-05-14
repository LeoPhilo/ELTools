package com.projects.leophilo.eltools.view.presenter.contact;

import android.support.annotation.NonNull;
import android.util.ArraySet;
import android.view.View;

import com.projects.leophilo.eltools.core.Calculator;
import com.projects.leophilo.eltools.model.entity.NormalCompositionItemEntity;
import com.projects.leophilo.eltools.view.adapter.MainListAdapter;

import java.util.ArrayList;

public interface MainContact extends BaseContact {

    interface View extends BaseContact.IView {
        void showToast(@NonNull String msg);
        void showCreateTip();
        void showEditBar();
        void showResult(final Calculator.ELData result, final double sum, ArrayList<NormalCompositionItemEntity> entities);
        void showManagingStateChangeAnim(android.view.View itemView, int itemType, boolean expectToDelete);
        void updateVolumeTextView(String updateData);
        void resetEditBar(boolean requestFocus);
        void reEditItem(NormalCompositionItemEntity entity);
        void notifyEditStateChanged(int state);
    }

    interface Presenter extends BaseContact.IPresenter<View> {
        void addNewItem(@NonNull String formula, double volume);
        boolean checkEditBarData(String volumeStr);
        void calculateComplete();
        MainListAdapter initAdapter();
        void callOnFabClick();
    }

}
