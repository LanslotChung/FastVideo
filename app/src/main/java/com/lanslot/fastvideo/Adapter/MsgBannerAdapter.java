package com.lanslot.fastvideo.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lanslot.fastvideo.Bean.Notice;
import com.lanslot.fastvideo.R;
import com.xuexiang.view.MarqueeTextView;
import com.youth.banner.adapter.BannerAdapter;

import java.util.Arrays;
import java.util.List;

public class MsgBannerAdapter extends BannerAdapter<Notice, MsgBannerAdapter.BannerViewHolder> {
    Context mainContext;

    public MsgBannerAdapter(List<Notice> datas, Context context) {
        super(datas);
        mainContext = context;
    }

    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_message, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindView(BannerViewHolder holder, Notice data, int position, int size) {
        holder.textView.setSpeed(5);
        holder.textView.startSimpleRoll(Arrays.asList(data.getTitle()));
        holder.view.setOnClickListener((v) -> {
            AlertDialog alertDialog1 = new AlertDialog.Builder(mainContext)
                    .setTitle(data.getTitle())//标题
                    .setMessage(data.getContent())//内容
                    .setIcon(R.drawable.msg)//图标
                    .create();
            alertDialog1.show();
        });
    }

    class BannerViewHolder extends RecyclerView.ViewHolder {
        View view;
        MarqueeTextView textView;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            textView = view.findViewById(R.id.tx_msg);
        }
    }
}
