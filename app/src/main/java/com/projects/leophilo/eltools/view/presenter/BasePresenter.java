package com.projects.leophilo.eltools.view.presenter;

import com.projects.leophilo.eltools.view.presenter.contact.BaseContact;

public class BasePresenter<V extends BaseContact.IView> implements BaseContact.IPresenter<V> {

    V mView;

    @Override
    public void attachView(V view) {
        this.mView = view;
    }

    @Override
    public void detachView() {

    }
}
