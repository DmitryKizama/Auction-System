package com.stkizema.auction.activities.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.stkizema.auction.R;
import com.stkizema.auction.model.User;
import com.stkizema.auction.util.ImageConstatnts;

public class UserDialog extends Dialog {

    private Context context;
    private User user;

    private TextView tvEmail, tvName;


    public UserDialog(Context context, User user) {
        super(context, R.style.AppTheme);
        this.context = context;
        this.user = user;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dilaog_user_profile);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.7f;

        tvName = (TextView) findViewById(R.id.tvName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);

        if (user == null) {
            tvName.setText(ImageConstatnts.ADMIN);
            tvEmail.setText(ImageConstatnts.ADMIN);
            return;
        }
        tvName.setText(user.getLogin());
        tvEmail.setText(user.getEmail());

    }
}