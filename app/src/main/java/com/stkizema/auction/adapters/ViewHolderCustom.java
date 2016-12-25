package com.stkizema.auction.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stkizema.auction.R;

public class ViewHolderCustom extends RecyclerView.ViewHolder {

    public TextView nameProduct, costProduct, nameUser, emailUser;
    public ImageView imageProduct;
    public LinearLayout llParent;


    public ViewHolderCustom(View itemView) {
        super(itemView);
        nameProduct = (TextView) itemView.findViewById(R.id.nameProduct);
        costProduct = (TextView) itemView.findViewById(R.id.costProduct);
        nameUser = (TextView) itemView.findViewById(R.id.loginUser);
        emailUser = (TextView) itemView.findViewById(R.id.emailUser);
        llParent = (LinearLayout) itemView.findViewById(R.id.llParent);
        imageProduct = (ImageView) itemView.findViewById(R.id.imgProduct);
    }
}
