package com.stkizema.auction.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.stkizema.auction.R;
import com.stkizema.auction.activities.ProductActivity;
import com.stkizema.auction.activities.dialogs.ConfirmProductDialog;
import com.stkizema.auction.db.DbBidHelper;
import com.stkizema.auction.db.DbProductHelper;
import com.stkizema.auction.db.DbUserHelper;
import com.stkizema.auction.model.Product;
import com.stkizema.auction.util.ImageConstatnts;
import com.stkizema.auction.util.ImageLoaderHelper;

import java.util.List;

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolderCustom> implements ConfirmProductDialog.ConfirmProductDialogListener {

    public static final String PRODUCTIDEXTRA = "product_id";
    public static final String USERNAMEEXTRA = "user_name";

    private List<Product> list;
    private CustomRecyclerViewAdapterListener listener;
    private int page;
    private String userName;
    private ViewGroup parent;


    public interface CustomRecyclerViewAdapterListener {
        void notifyItemInsertedInListBidRV(Product product, int position);
    }

    public CustomRecyclerViewAdapter(String userName, List<Product> list, CustomRecyclerViewAdapterListener listener, int page) {
        this.userName = userName;
        this.list = list;
        this.listener = listener;
        this.page = page;
    }

    public void setListProduct(List<Product> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void updateElementById(Long id) {
        int counter = 0;
        for (Product p : list) {
            if (p.getIdProduct().equals(id)) {
                Product product = DbProductHelper.getProductById(id);
                list.set(counter, product);
                notifyItemChanged(counter);
                return;
            }
            ++counter;
        }
    }

    @Override
    public ViewHolderCustom onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_custom_adapter, parent, false);
        return new ViewHolderCustom(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolderCustom holder, int position) {
        setView(holder, list.get(position));
        switch (page) {
            case 0:
                break;
            case 1:
                holder.llParent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(parent.getContext(), ProductActivity.class);
                        intent.putExtra(PRODUCTIDEXTRA, list.get(holder.getAdapterPosition()).getIdProduct());
                        intent.putExtra(USERNAMEEXTRA, userName);
                        parent.getContext().startActivity(intent);
                    }
                });
                break;
            case 2:
                if (!userName.equals(ImageConstatnts.ADMIN)) {
                    break;
                }
                holder.llParent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ConfirmProductDialog dialog = new ConfirmProductDialog(view.getContext(), list.get(holder.getAdapterPosition()),
                                CustomRecyclerViewAdapter.this, holder.getAdapterPosition());
                        dialog.show();
                    }
                });
                break;
        }
    }

    private void setView(ViewHolderCustom holder, Product product) {
        holder.nameProduct.setText(product.getName());
        holder.costProduct.setText("Buyer: " + DbUserHelper.getUserById(DbBidHelper.getBidByProductId(product.getIdProduct()).getCreatorId()).getLogin() +
                " - " + DbBidHelper.getTopPriceByProductId(product.getIdProduct()) + "$");
        holder.nameUser.setText("Creator: " + DbUserHelper.getUserById(product.getCreatorId()).getLogin());
        holder.emailUser.setText("Creator email: " + DbUserHelper.getUserById(product.getCreatorId()).getEmail());

        String imgUrl = product.getImageUrl();
        if (imgUrl != null) {
            if (imgUrl.contains(ImageLoaderHelper.DRAWABLE_PREF)){
                ImageLoader.getInstance().displayImage(imgUrl, holder.imageProduct);
            } else {
                ImageLoader.getInstance().displayImage(ImageLoaderHelper.FILE_SYSTEM_PREF + imgUrl, holder.imageProduct);
            }
        } else {
            holder.imageProduct.setImageResource(R.drawable.auction_hammer);
        }
    }

    public void notifyItemInsertedInListUnreg(int positionToInsert, Product product) {
        list.add(positionToInsert, product);
        notifyItemInserted(positionToInsert);
    }


    @Override
    public void onBtnOkClick(int positionToInsert, int positionToRemove, Product product) {
        list.remove(positionToRemove);
        notifyItemRemoved(positionToRemove);
        listener.notifyItemInsertedInListBidRV(product, positionToInsert);
    }

    @Override
    public void onDeleteClick(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }
}
