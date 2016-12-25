package com.stkizema.auction.activities.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stkizema.auction.R;
import com.stkizema.auction.db.DbBidHelper;

public class OfferPriceDialog extends Dialog {

    private Long userId;
    private Button btnOffer, btnCancel;
    private Context context;
    private EditText etPrice;
    private Long productId;
    private OfferPriceDialogListener listener;

    public interface OfferPriceDialogListener {
        void onOfferNewPriceClick(double price, Long userId);
    }

    public OfferPriceDialog(Context context, Long userId, Long id, OfferPriceDialogListener listener) {
        super(context, R.style.AppTheme);
        this.context = context;
        this.userId = userId;
        this.productId = id;
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_offer_price);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.7f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnOffer = (Button) findViewById(R.id.btnOffer);
        etPrice = (EditText) findViewById(R.id.etPrice);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        btnOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!check()) {
                    return;
                }
                dismiss();
            }
        });
    }

    private boolean check() {
        double cost;
        try {
            cost = Double.parseDouble(etPrice.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(context, "Enter valid price!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cost <= DbBidHelper.getTopPriceByProductId(productId)) {
            Toast.makeText(context, "This price is lower than previous!", Toast.LENGTH_SHORT).show();
            return false;
        }
        DbBidHelper.create(userId, productId, cost);
        listener.onOfferNewPriceClick(cost, userId);
        return true;
    }
}
