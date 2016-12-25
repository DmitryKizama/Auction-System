package com.stkizema.auction.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.stkizema.auction.R;
import com.stkizema.auction.db.DbUserHelper;
import com.stkizema.auction.db.DemoDb;
import com.stkizema.auction.db.Session;
import com.stkizema.auction.main.controller.BaseBottomController;
import com.stkizema.auction.main.controller.LogInController;
import com.stkizema.auction.main.controller.RegisterController;
import com.stkizema.auction.model.User;

public class MainActivity extends AppCompatActivity implements BaseBottomController.BottomControllerListener {

    public static final String EXTRALOGIN = "LOGIN";
    public static final String EXTRAPERMISSION = "PERMISSION";
    private FrameLayout bottomLayout;
    private BaseBottomController bottomController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String name = Session.getInstance().getNameToken();
        String pass = Session.getInstance().getPassToken();

        DemoDb.setDemoDb();

        User myUser = DbUserHelper.getUserByLoginAndPass(name, pass);
        if (myUser != null) {
            onBtnOkClickListener(myUser.getPermission(), myUser.getLogin());
            return;
        }

        bottomLayout = (FrameLayout) findViewById(R.id.frame_main_layout);
        onRegisterLoginChange(false);
    }

    @Override
    public void onBtnOkClickListener(String permission, String login) {
        Intent intent = new Intent(MainActivity.this, ListOfProductsActivity.class);
        intent.putExtra(EXTRALOGIN, login);
        intent.putExtra(EXTRAPERMISSION, permission);
        this.finish();
        startActivity(intent);
    }

    @Override
    public void onRegisterLoginChange(boolean registerLogin) {
        if (registerLogin) {
            View view = LayoutInflater.from(this).inflate(R.layout.layout_main_bottom_register, bottomLayout, false);
            bottomLayout.removeAllViews();
            bottomLayout.addView(view);
            bottomController = new RegisterController(bottomLayout, this);
        } else {
            View view = LayoutInflater.from(this).inflate(R.layout.layout_main_bottom_login, bottomLayout, false);
            bottomLayout.removeAllViews();
            bottomLayout.addView(view);
            bottomController = new LogInController(bottomLayout, this);
        }
    }

}
