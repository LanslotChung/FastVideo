package com.lanslot.fastvideo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lanslot.fastvideo.Bean.MovieChannel;
import com.lanslot.fastvideo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlatformAdapter extends BaseAdapter {
    private Context context;
    List<MovieChannel> platforms;

    public PlatformAdapter(Context context, List<MovieChannel> platforms) {
        this.context = context;
        this.platforms = platforms;
    }

    @Override
    public int getCount() {
        return platforms.size();
    }

    @Override
    public MovieChannel getItem(int position) {
        return platforms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PlatformViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_platform,parent,false);
            TextView title = convertView.findViewById(R.id.platform_title);
            ImageView logo = convertView.findViewById(R.id.platform_logo);
            holder = new PlatformViewHolder(title,logo);
            convertView.setTag(holder);
        }else{
            holder = (PlatformViewHolder) convertView.getTag();
        }
        MovieChannel platform = getItem(position);
        holder.title.setText(platform.getName());
        Picasso.get().load(platform.getIcon()).into(holder.logo);
        convertView.setOnClickListener((v)->{
            Toast.makeText(parent.getContext(),platform.getUrl(),Toast.LENGTH_SHORT).show();
        });
        return convertView;
    }

    class PlatformViewHolder{
        TextView title;
        ImageView logo;

        public PlatformViewHolder(TextView title,ImageView logo){
            this.title = title;
            this.logo = logo;
        }
    }
}
