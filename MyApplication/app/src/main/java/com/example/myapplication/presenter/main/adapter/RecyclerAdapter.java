package com.example.myapplication.presenter.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.domain.Result;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private Context ctx;
    private static final int VIEW_TYPE_LOADING = 0;
    private static final int VIEW_TYPE_NORMAL = 1;
    private boolean isLoaderVisible = false;
    private List<Result> mPostItems;


    public RecyclerAdapter(Context ctx) {
        this.ctx = ctx;
        mPostItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new ViewHolder(
                        LayoutInflater.from(parent.getContext()).
                                inflate(R.layout.current_item, parent, false));
            case VIEW_TYPE_LOADING:
                return new ProgressHolder(
                        LayoutInflater.from(parent.getContext()).
                                inflate(R.layout.item_loading, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoaderVisible) {
            return position == mPostItems.size() - 1 ?
                    VIEW_TYPE_LOADING :
                    VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return mPostItems == null ? 0 : mPostItems.size();
    }

    public void addItems(List<Result> postItems) {
        mPostItems.addAll(postItems);
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
        mPostItems.add(new Result());
        notifyItemInserted(mPostItems.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = mPostItems.size() - 1;
        Result item = getItem(position);
        if (item != null) {
            mPostItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        mPostItems.clear();
        notifyDataSetChanged();
    }

    Result getItem(int position) {
        return mPostItems.get(position);
    }

    public class ViewHolder extends BaseViewHolder {
        protected TextView txvName;
        protected TextView txvStatus;
        protected TextView txvSpecie;
        protected TextView txvGender;
        protected ImageView imageView;
        protected View viewStatus;


        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            txvName = itemView.findViewById(R.id.txvName);
            txvSpecie = itemView.findViewById(R.id.txvSpecie);
            txvStatus = itemView.findViewById(R.id.txvStatus);
            viewStatus = itemView.findViewById(R.id.viewStatus);
            txvGender = itemView.findViewById(R.id.txvGender);


        }

        protected void clear() {
        }

        public void onBind(int position) {
            super.onBind(position);
            Result item = mPostItems.get(position);
            txvName.setText(item.getName());
            txvSpecie.setText(item.getSpecies());
            txvStatus.setText(item.getStatus());
            viewStatus.setBackgroundColor(item.isStatusAlive() ?
                    ContextCompat.getColor(ctx, R.color.colorStatusAlive) :
                    ContextCompat.getColor(ctx, R.color.colorStatusDead));
            txvGender.setText(item.getGender());
            Picasso.get().load(item.getImageUrl()).fit().into(imageView);
        }
    }

    public class ProgressHolder extends BaseViewHolder {
        ProgressBar progressBar;

        ProgressHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }

        @Override
        protected void clear() {
        }
    }
}