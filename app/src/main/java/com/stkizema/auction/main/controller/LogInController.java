package com.stkizema.auction.main.controller;

import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stkizema.auction.R;
import com.stkizema.auction.db.DbUserHelper;
import com.stkizema.auction.model.User;
import com.stkizema.auction.util.ImageConstatnts;

public class LogInController extends BaseBottomController {

    private TextView tvRegister;
    private Button btnOk;
    private EditText etLogIn, etPassword;

    public LogInController(final View parent, BottomControllerListener adapterListener) {
        super(parent, adapterListener);
        btnOk = (Button) parent.findViewById(R.id.btn_login);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adminLogin()) {
                    bottomControllerListener.onBtnOkClickListener(User.PERMISSIONADMIN, ImageConstatnts.ADMIN);
                    return;
                }
                if (!checkOk()) {
                    Toast.makeText(parent.getContext(), "Name or password invalid", Toast.LENGTH_SHORT).show();
                    return;
                }
                bottomControllerListener.onBtnOkClickListener(User.PERMISSIONUSER, etLogIn.getText().toString());
            }
        });

        tvRegister = (TextView) parent.findViewById(R.id.tv_register);
        tvRegister.setPaintFlags(tvRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomControllerListener.onRegisterLoginChange(true);
            }
        });

        etLogIn = (EditText) parent.findViewById(R.id.et_login);
        etPassword = (EditText) parent.findViewById(R.id.et_password);
    }

    private boolean adminLogin() {
        if (etLogIn.getText().toString().equals(ImageConstatnts.ADMIN) && etPassword.getText().toString().equals(ImageConstatnts.ADMIN)) {
            return true;
        }
        return false;
    }

    private boolean checkOk() {
        String login = etLogIn.getText().toString();
        String pass = etPassword.getText().toString();
        User user = DbUserHelper.getUserByLoginAndPass(login, pass);
        return user != null;
    }

}
