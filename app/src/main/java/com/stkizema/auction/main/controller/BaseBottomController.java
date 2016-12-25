package com.stkizema.auction.main.controller;

import android.view.View;

abstract public class BaseBottomController {
    protected View parent;
    protected BottomControllerListener bottomControllerListener;

    public interface BottomControllerListener {
        void onBtnOkClickListener(String permission, String login);

        void onRegisterLoginChange(boolean flag);
    }

    public BaseBottomController(View parent, BottomControllerListener adapterListener) {
        this.parent = parent;
        this.bottomControllerListener = adapterListener;
    }

}
