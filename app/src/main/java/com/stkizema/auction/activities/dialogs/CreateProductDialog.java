package com.stkizema.auction.activities.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.stkizema.auction.R;
import com.stkizema.auction.db.DbProductHelper;
import com.stkizema.auction.model.Product;
import com.stkizema.auction.util.ImageLoaderHelper;

public class CreateProductDialog extends Dialog {

    private EditText etName, etPrice;
    private Button btnAdd, btnDownloadGallery, btnDownloadCamera, btnCancel;
    private Long userId;
    private Context context;
    private ImageView imgProduct;
    private String imageUrl;

    private OnDialogBtnsCLickListener listener;
    private CreateProductDialogListener listenerP;

    public interface CreateProductDialogListener {
        void onAddProduct(Product p);
    }

    public interface OnDialogBtnsCLickListener {
        void onCameraClick();

        void onGalleryCLick();
    }

    public CreateProductDialog(CreateProductDialogListener listenerP, Activity context, OnDialogBtnsCLickListener listener, Long userId) {
        super(context, R.style.AppTheme);
        this.context = context;
        this.listener = listener;
        this.listenerP = listenerP;
        this.userId = userId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_product_create);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        ViewGroup parent = (ViewGroup) findViewById(R.id.parent);

        etName = (EditText) findViewById(R.id.edtName);
        etPrice = (EditText) findViewById(R.id.edtPrice);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnDownloadGallery = (Button) findViewById(R.id.btnDownloadGallery);
        btnDownloadCamera = (Button) findViewById(R.id.btnDownloadCamera);
        imgProduct = (ImageView) findViewById(R.id.imgProduct);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnDownloadGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onGalleryCLick();
            }
        });

        btnDownloadCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCameraClick();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double cost = 0;
                try {
                    cost = Double.parseDouble(etPrice.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(context, "Enter valid price!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Product p = DbProductHelper.create(etName.getText().toString(), cost, imageUrl, userId);
                if (p == null) {
                    Toast.makeText(context, "Enter another name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                listenerP.onAddProduct(p);
                dismiss();
            }
        });

    }

    public void showPhoto(String path) {
        ImageLoader.getInstance().displayImage(ImageLoaderHelper.FILE_SYSTEM_PREF + path, imgProduct);
        imageUrl = path;
    }
}