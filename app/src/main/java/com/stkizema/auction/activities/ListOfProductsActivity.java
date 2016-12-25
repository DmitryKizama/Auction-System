package com.stkizema.auction.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.astuetz.PagerSlidingTabStrip;
import com.stkizema.auction.R;
import com.stkizema.auction.activities.dialogs.CreateProductDialog;
import com.stkizema.auction.activities.dialogs.UserDialog;
import com.stkizema.auction.adapters.CustomRecyclerViewAdapter;
import com.stkizema.auction.db.DbUserHelper;
import com.stkizema.auction.db.Session;
import com.stkizema.auction.model.Product;
import com.stkizema.auction.model.User;
import com.stkizema.auction.util.ImageConstatnts;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListOfProductsActivity extends AppCompatActivity implements CustomRecyclerViewAdapter.CustomRecyclerViewAdapterListener {
    public static final String BROADCAST_ACTION_BOT = "BROADCASTACTIONPAGEGENERATE";
    public static final String EXTRAPERMISSION = "extra_permission";
    public static final String EXTRAUSERNAME = "extra_user_name";

    private ViewPager pager;
    private FragmentPagerAdapterProducts pagerAdapter;
    private String permission = User.PERMISSIONUSER, userName;
    private ImageView imLogOut;
    private CircleImageView cimViewAddProduct;
    private CreateProductDialog dialog;
    private  View imgMenu;

    private BroadcastReceiver broadcastReceiverProduct;
    private BroadcastReceiver broadcastReceiverBid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_conferences);
        imLogOut = (ImageView) findViewById(R.id.imgLogOut);
        pager = (ViewPager) findViewById(R.id.pager);
        cimViewAddProduct = (CircleImageView) findViewById(R.id.addImg);
        imgMenu = findViewById(R.id.imgMenu);

        broadcastReceiverProduct = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                pagerAdapter.getItem(1).updateFragment();
            }
        };
        broadcastReceiverBid = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Long id = intent.getLongExtra(BotService.EXTRA_PRODUCT_ID, 0L);
                pagerAdapter.getItem(1).updateFragment(id);
            }
        };
        registerBroadcast();

        Intent intent = new Intent(this, BotService.class);
        startService(intent);

        if (getIntent() != null) {
            permission = getIntent().getStringExtra(MainActivity.EXTRAPERMISSION);
            userName = getIntent().getStringExtra(MainActivity.EXTRALOGIN);
        }

        if (permission.equals(User.PERMISSIONADMIN)) {
            cimViewAddProduct.setVisibility(View.GONE);
        }

        pagerAdapter = new FragmentPagerAdapterProducts(getSupportFragmentManager(), permission, this, userName);
        pager.setAdapter(pagerAdapter);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (permission.equals(User.PERMISSIONADMIN)) {
                    cimViewAddProduct.setVisibility(View.GONE);
                    return;
                }

                if (position == 0){
                    cimViewAddProduct.setVisibility(View.VISIBLE);
                } else {
                    cimViewAddProduct.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });


        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        imLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Session.getInstance().clearAuthToken();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                ListOfProductsActivity.this.finish();
                startActivity(intent);
            }
        });

        cimViewAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new CreateProductDialog(new CreateProductDialog.CreateProductDialogListener() {
                    @Override
                    public void onAddProduct(Product p) {
                        pager.setCurrentItem(0, true);
                        pagerAdapter.getItem(0).notifyItemInsertedInListBid(p, 0);
                    }
                }, ListOfProductsActivity.this, new CreateProductDialog.OnDialogBtnsCLickListener() {
                    @Override
                    public void onCameraClick() {
                        filePath = openCameraActivity();
                    }

                    @Override
                    public void onGalleryCLick() {
                        openGalleryAcivity();
                    }
                }, DbUserHelper.getUser(userName).getId());
                dialog.show();
            }
        });

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDialog dialog = new UserDialog(view.getContext(), DbUserHelper.getUser(userName));
                dialog.show();
            }
        });

    }

    String filePath = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ImageConstatnts.REQUEST_CODE_GALLERY:
                if (resultCode == RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    String photoPath = getPath(selectedImageUri);
                    if (dialog != null) {
                        dialog.showPhoto(photoPath);
                    }
                    return;
                }

            case ImageConstatnts.REQUEST_CODE_MAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        String photoPath = data.getStringExtra(ImageConstatnts.FILE_NAME);
                        if (dialog != null) {
                            dialog.showPhoto(photoPath);
                        }
                    } else {

                        if (dialog != null && filePath != null) {
                            dialog.showPhoto(filePath);
                        }
                    }
                }
                return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void registerBroadcast() {
        IntentFilter intFiltr = new IntentFilter(BotService.BROADCAST_ACTION_BID);
        registerReceiver(broadcastReceiverBid, intFiltr);
        IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION_BOT);
        registerReceiver(broadcastReceiverProduct, intFilt);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("BotServiceLog", "Destroy in activity");
        stopService(new Intent(this, BotService.class));
        unregisterReceiver(broadcastReceiverProduct);
        unregisterReceiver(broadcastReceiverBid);
    }

    @Override
    public void notifyItemInsertedInListBidRV(Product product, int position) {
        pager.setCurrentItem(1, true);
        pagerAdapter.getItem(1).notifyItemInsertedInListBid(product, position);
    }

    private class FragmentPagerAdapterProducts extends FragmentStatePagerAdapter {

        SparseArray<ListFragment> registeredFragments = new SparseArray<>();
        int PAGE_COUNT;
        private String permission;
        private CustomRecyclerViewAdapter.CustomRecyclerViewAdapterListener listener;
        private String userNam;

        public FragmentPagerAdapterProducts(FragmentManager fm, String permission, CustomRecyclerViewAdapter.CustomRecyclerViewAdapterListener listener, String userN) {
            super(fm);
            this.permission = permission;
            this.listener = listener;
            this.userNam = userN;
            PAGE_COUNT = 3;
        }

        private final String[] TITLESADMIN = {"All won products", "All bidding products", "Unregistered products"};
        private final String[] TITLES = {"Your unregister products", "All bidding products", "All won products"};

        @Override
        public CharSequence getPageTitle(int position) {
            if (permission.equals(User.PERMISSIONUSER)) {
                return TITLES[position];
            } else {
                return TITLESADMIN[position];
            }

        }

        @Override
        public ListFragment getItem(int position) {
            if (registeredFragments.get(position) != null) {
                return registeredFragments.get(position);
            }
            ListFragment day = ListFragment.newInstance(position, listener, userNam);
            registeredFragments.put(position, day);
            return day;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ListFragment fragment = (ListFragment) super.instantiateItem(container, position);
//            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public ListFragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }

    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            Log.d("RTY", "  if( uri == null )");
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

    public String openCameraActivity() {
        String filePath = null;

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String imageFileName = "JPEG" + timeStamp;
                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                photoFile = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );

                // Save a file: path for use with ACTION_VIEW intents
                filePath = photoFile.getAbsolutePath();
            } catch (IOException ex) {
                filePath = null;
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, ImageConstatnts.REQUEST_CODE_MAKE_PHOTO);
            }
        }

        return filePath;
    }

    public void openGalleryAcivity() {
        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setType("image/*");

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        boolean isIntentSafe = activities.size() > 0;

        if (isIntentSafe) {
            startActivityForResult(Intent.createChooser(intent,
                    "Select from gallery"), ImageConstatnts.REQUEST_CODE_GALLERY);
        }
    }

}
