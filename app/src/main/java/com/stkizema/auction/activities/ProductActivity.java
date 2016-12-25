package com.stkizema.auction.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.stkizema.auction.R;
import com.stkizema.auction.activities.dialogs.OfferPriceDialog;
import com.stkizema.auction.adapters.CustomRecyclerViewAdapter;
import com.stkizema.auction.db.DbBidHelper;
import com.stkizema.auction.db.DbProductHelper;
import com.stkizema.auction.db.DbUserHelper;
import com.stkizema.auction.model.Product;
import com.stkizema.auction.model.User;
import com.stkizema.auction.util.ImageConstatnts;
import com.stkizema.auction.util.ImageLoaderHelper;

public class ProductActivity extends AppCompatActivity implements OfferPriceDialog.OfferPriceDialogListener {

    private TextView tvNameProduct, tvNameCreator, tvPrice;
    private ImageView imgProduct;
    private Long productId = 1L;
    private Button btnOffer, btnSellOff;
    private String userName;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_activity);

        tvNameProduct = (TextView) findViewById(R.id.productName);
        tvNameCreator = (TextView) findViewById(R.id.productNameCreator);
        tvPrice = (TextView) findViewById(R.id.productCost);
        imgProduct = (ImageView) findViewById(R.id.imgProduct);
        btnSellOff = (Button) findViewById(R.id.btnSellOff);
        btnSellOff.setVisibility(View.GONE);
        btnOffer = (Button) findViewById(R.id.btnOfferPrice);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Long id = intent.getLongExtra(BotService.EXTRA_PRODUCT_ID, 0L);
                if (!productId.equals(id)) {
                    return;
                }
                Product p = DbProductHelper.getProductById(id);
                setPriceText(p);
            }
        };
        IntentFilter intFiltr = new IntentFilter(BotService.BROADCAST_ACTION_BID);
        registerReceiver(broadcastReceiver, intFiltr);

        if (getIntent() != null) {
            productId = getIntent().getLongExtra(CustomRecyclerViewAdapter.PRODUCTIDEXTRA, 1L);
            userName = getIntent().getStringExtra(CustomRecyclerViewAdapter.USERNAMEEXTRA);
        }

        Product p = DbProductHelper.getProductById(productId);

        if (p.getImageUrl() != null) {
            if (p.getImageUrl().contains(ImageLoaderHelper.DRAWABLE_PREF)){
                ImageLoader.getInstance().displayImage(p.getImageUrl(), imgProduct);
            } else {
                ImageLoader.getInstance().displayImage(ImageLoaderHelper.FILE_SYSTEM_PREF + p.getImageUrl(), imgProduct);
            }
        }

        if (userName.equals(DbUserHelper.getUserById(p.getCreatorId()).getLogin())) {
            btnSellOff.setVisibility(View.VISIBLE);
            btnOffer.setVisibility(View.GONE);
        }

        if (userName.equals(ImageConstatnts.ADMIN)) {
            btnOffer.setVisibility(View.GONE);
        }

        tvNameProduct.setText("Product: " + p.getName());
        tvNameCreator.setText("Created by:\n"+DbUserHelper.getUserById(p.getCreatorId()).getLogin());

        setPriceText(p);

        btnOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OfferPriceDialog dialog = new OfferPriceDialog(ProductActivity.this,
                        DbUserHelper.getUser(userName).getId(), productId, ProductActivity.this);
                dialog.show();
            }
        });

        btnSellOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!buyProduct()) {
                    return;
                }
                Toast.makeText(ProductActivity.this, "You sell - " + DbProductHelper.getProductById(productId).getName(), Toast.LENGTH_SHORT).show();
                goToListOFProductsActivity();
            }
        });

    }

    private boolean buyProduct() {
        Product p = DbProductHelper.getProductById(productId);
        p.setIsWon(true);
        p.setIsAuctioned(false);
        if (DbBidHelper.getBidByProductId(productId).getCreatorId().equals(DbUserHelper.getUser(userName).getId())) {
            Toast.makeText(ProductActivity.this, "You can`t buy your own item!", Toast.LENGTH_SHORT).show();
            return false;
        }
        p.setWinnerId(DbBidHelper.getBidByProductId(productId).getCreatorId());
        DbProductHelper.getProductDao().update(p);
        return true;
    }

    private void goToListOFProductsActivity() {
        Intent intent = new Intent(ProductActivity.this, ListOfProductsActivity.class);
        intent.putExtra(MainActivity.EXTRALOGIN, userName);
        intent.putExtra(MainActivity.EXTRAPERMISSION, User.PERMISSIONUSER);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (userName.equals(ImageConstatnts.ADMIN)) {
            intent.putExtra(MainActivity.EXTRAPERMISSION, User.PERMISSIONADMIN);
        }
        this.finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToListOFProductsActivity();
    }

    private void setPriceText(Product p){
        tvPrice.setText(DbUserHelper.getUserById(DbBidHelper.getBidByProductId(p.getIdProduct()).getCreatorId()).getLogin() + "\n"
                + DbBidHelper.getTopPriceByProductId(p.getIdProduct()) + "$");
    }

    @Override
    public void onOfferNewPriceClick(double price, Long userId) {
        tvPrice.setText(DbUserHelper.getUserById(userId).getLogin() +
                "\n" + price + "$");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
