package com.lanslot.fastvideo.Adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lanslot.fastvideo.Bean.ImageRotation;
import com.lanslot.fastvideo.Views.RoundImageView;
import com.squareup.picasso.Picasso;
import com.youth.banner.adapter.BannerAdapter;

import java.util.List;

public class AdBannerAdapter extends BannerAdapter<ImageRotation, AdBannerAdapter.BannerViewHolder> {

    private Context context;

    public AdBannerAdapter(Context context, List<ImageRotation> datas) {
        super(datas);
        this.context = context;
    }

    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        RoundImageView imageView = new RoundImageView(parent.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        imageView.setRadius(30);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new BannerViewHolder(imageView);
    }

    @Override
    public void onBindView(BannerViewHolder holder, ImageRotation data, int position, int size) {
        Picasso.get().load(data.getImagePath()).into(holder.imageView);
        switch(data.getType()){
            case 0://无交互
                break;
            case 1://外部跳转
                holder.imageView.setOnClickListener(v -> {

                });
                break;
            case 2://内部跳转
                break;
        }
    }

    class BannerViewHolder extends RecyclerView.ViewHolder{
        RoundImageView imageView;
        public BannerViewHolder(@NonNull RoundImageView itemView) {
            super(itemView);
            imageView = itemView;
        }
    }
}
