package com.stkizema.auction.main.controller;

import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stkizema.auction.R;
import com.stkizema.auction.db.DbUserHelper;
import com.stkizema.auction.db.Session;
import com.stkizema.auction.model.User;
import com.stkizema.auction.util.ImageConstatnts;
import com.stkizema.auction.validator.EmailValidator;
import com.stkizema.auction.validator.PasswordValidator;

public class RegisterController extends BaseBottomController {

    private TextView tvLogin;
    private Button btnOk;
    private EditText etLogIn, etPassword, etEmail;

    public RegisterController(View parent, BottomControllerListener adapterListener) {
        super(parent, adapterListener);

        tvLogin = (TextView) parent.findViewById(R.id.tv_login);
        tvLogin.setPaintFlags(tvLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomControllerListener.onRegisterLoginChange(false);
            }
        });

        btnOk = (Button) parent.findViewById(R.id.btn_register);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User createdUser = registerUser();
                if (createdUser == null) {
                    return;
                }

                //TODO chared prefs
                Session.getInstance().saveToken(createdUser.getLogin(), createdUser.getPassword());
                bottomControllerListener.onBtnOkClickListener(User.PERMISSIONUSER, etLogIn.getText().toString());
            }
        });

        etLogIn = (EditText) parent.findViewById(R.id.et_login);
        etPassword = (EditText) parent.findViewById(R.id.et_password);
        etEmail = (EditText) parent.findViewById(R.id.et_email);
    }

    private User registerUser() {
        String login = etLogIn.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (checkForExisting(login, email)) {
            return null;
        }
        if (!PasswordValidator.getInstance().validate(password)) {
            Toast.makeText(parent.getContext(), "Your password less than 7 characters or does not contain at least one integer", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (!EmailValidator.getInstance().validate(email)) {
            Toast.makeText(parent.getContext(), "Incorrect email!", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (adminRegistration(login, password)) {
            return null;
        }

        User newUser = DbUserHelper.create(login, email, password);
        if (newUser == null) {
            Toast.makeText(parent.getContext(), "Such user already exist!", Toast.LENGTH_SHORT).show();
            return null;
        }

        return newUser;
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private boolean checkForExisting(String login, String email) {
        if (login.equals("") || email.equals("")) {
            Toast.makeText(parent.getContext(), "Enter please email and login", Toast.LENGTH_SHORT).show();
            return true;
        }

        User user = DbUserHelper.getUser(login);
        if (user != null){
            Toast.makeText(parent.getContext(), "Such login already exist!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return false;
    }

    private boolean adminRegistration(String login, String password) {
        if (login.equals(ImageConstatnts.ADMIN) && password.equals(ImageConstatnts.ADMIN)) {
            Toast.makeText(parent.getContext(), "Doesn`t allowed admin registration!", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
