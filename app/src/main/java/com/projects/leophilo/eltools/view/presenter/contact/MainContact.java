package com.projects.leophilo.eltools.view.presenter.contact;

import android.support.annotation.NonNull;

public interface MainContact extends BaseContact {

    interface View extends BaseContact.IView {
        void showToast(@NonNull String msg);
        void showCreateBar();
        void updateVolumeTextView(String updateData);
        void resetEditBar();
        void editComplete();
        void resetAll();
    }

    interface Presenter extends BaseContact.IPresenter<View> {
        void addNewItem(@NonNull String formula, float volume);
        boolean checkEditBarData(String volumeStr);
        void calculateComplete();
    }

}
