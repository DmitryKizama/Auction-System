package com.stkizema.auction.activities.dialogs;

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
import android.widget.Toast;

import com.stkizema.auction.R;
import com.stkizema.auction.db.DbProductHelper;
import com.stkizema.auction.model.Product;

public class ConfirmProductDialog extends Dialog {

    private EditText etName, etPrice;
    private Button btnDelete, btnCancel, btnYes;
    private Context context;
    private Product product;
    private ConfirmProductDialogListener listener;
    private int position;

    public interface ConfirmProductDialogListener {
        void onBtnOkClick(int positionInsert, int positionRemove, Product product);

        void onDeleteClick(int position);
    }

    public ConfirmProductDialog(Context context, Product product, ConfirmProductDialogListener listener, int position) {
        super(context, R.style.AppTheme);
        this.context = context;
        this.product = product;
        this.listener = listener;
        this.position = position;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_product_confirm);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        final ViewGroup parent = (ViewGroup) findViewById(R.id.parent);

        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnYes = (Button) findViewById(R.id.btnYes);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!DbProductHelper.delete(DbProductHelper.getProductById(product.getIdProduct()))) {
                    Toast.makeText(context, "Something goes wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onDeleteClick(position);
                dismiss();
            }
        });

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product1 = DbProductHelper.getProductById(product.getIdProduct());
                product1.setIsAuctioned(true);
                DbProductHelper.getProductDao().update(product1);
                listener.onBtnOkClick(DbProductHelper.getListBidProduct().size() - 1, position, product1);
                dismiss();
            }
        });

    }
}