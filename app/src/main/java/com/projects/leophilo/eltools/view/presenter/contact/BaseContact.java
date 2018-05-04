package com.projects.leophilo.eltools.view.presenter.contact;

public interface BaseContact {

    interface IView {

    }

    interface IPresenter<V extends IView> {
        void attachView(V view);
        void detachView();
    }

}
