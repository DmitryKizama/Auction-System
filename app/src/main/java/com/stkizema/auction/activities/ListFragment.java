package com.stkizema.auction.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stkizema.auction.R;
import com.stkizema.auction.adapters.CustomRecyclerViewAdapter;
import com.stkizema.auction.db.DbProductHelper;
import com.stkizema.auction.db.DbUserHelper;
import com.stkizema.auction.model.Product;
import com.stkizema.auction.util.ImageConstatnts;

import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ListFragment extends Fragment {
    static final String ARGUMENT_PAGE_NUMBER = "arg_page_number";
    static final String ARGUMENT_USER_NAME = "arg_name";

    private int page;
    private int countInputs;
    private RecyclerView rv;
    private int idPreviouse;
    private CustomRecyclerViewAdapter adapter;
    private List<Product> list;
    private static CustomRecyclerViewAdapter.CustomRecyclerViewAdapterListener listener;
    private String userName;
    private TextView tvNoItems;
    private Observer observer;

    public static ListFragment newInstance(int page, CustomRecyclerViewAdapter.CustomRecyclerViewAdapterListener listenerP, String userName) {
        ListFragment d = new ListFragment();
        Bundle args = new Bundle();
        listener = listenerP;
        args.putInt(ARGUMENT_PAGE_NUMBER, page);
        args.putString(ARGUMENT_USER_NAME, userName);
        d.setArguments(args);
        return d;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(ARGUMENT_PAGE_NUMBER);
        userName = getArguments().getString(ARGUMENT_USER_NAME);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        rv = (RecyclerView) view.findViewById(R.id.recyclerView);
        tvNoItems = (TextView) view.findViewById(R.id.tvNoItems);
        Log.d("ListFragmentLog", "page = " + page);

        Long id;

        if (userName.equals(ImageConstatnts.ADMIN)) {
            id = null;
            switch (page) {
                case 0:
                    adapter = new CustomRecyclerViewAdapter(userName, DbProductHelper.getListWonProduct(id), listener, page);
                    break;
                case 1:
                    adapter = new CustomRecyclerViewAdapter(userName, DbProductHelper.getListBidProduct(), listener, page);
                    break;
                case 2:
                    adapter = new CustomRecyclerViewAdapter(userName, DbProductHelper.getListUnregisteredProduct(id), listener, page);
                    break;
            }

        } else {
            id = DbUserHelper.getUser(userName).getId();
            switch (page) {
                case 0:
                    adapter = new CustomRecyclerViewAdapter(userName, DbProductHelper.getListUnregisteredProduct(id), listener, page);
                    break;
                case 1:
                    adapter = new CustomRecyclerViewAdapter(userName, DbProductHelper.getListBidProduct(), listener, page);
                    break;
                case 2:
                    adapter = new CustomRecyclerViewAdapter(userName, DbProductHelper.getListWonProduct(id), listener, page);
                    break;
            }
        }

        observer = new Observer();
        adapter.registerAdapterDataObserver(observer);
        observer.checkIfEmpty();

        rv.setAdapter(adapter);
        rv.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(mLayoutManager);

        return view;
    }

    public void notifyItemInsertedInListBid(Product product, int position) {
        adapter.notifyItemInsertedInListUnreg(position, product);
    }

    public void updateFragment() {
        adapter.setListProduct(DbProductHelper.getListBidProduct());
    }

    public void updateFragment(Long id) {
        adapter.updateElementById(id);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class Observer extends RecyclerView.AdapterDataObserver{
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        public void checkIfEmpty() {
            final boolean emptyViewVisible = adapter.getItemCount() == 0;
            tvNoItems.setVisibility(emptyViewVisible ? VISIBLE : GONE);
        }
    };
}
