package com.lanslot.fastvideo.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanslot.fastvideo.Bean.MyContacts;
import com.lanslot.fastvideo.Bean.User;
import com.lanslot.fastvideo.R;

import java.util.ArrayList;

public class UserListAdapter extends BaseAdapter {
    private ArrayList<User> users;
    private ArrayList<MyContacts> contacts;
    private boolean hasContacts = false;

    public UserListAdapter(ArrayList<User> users, ArrayList<MyContacts> contacts) {
        this.users = users;
        this.contacts = contacts;
        hasContacts = contacts != null && contacts.size() > 0;
    }

    @Override
    public int getCount() {
        return users==null?0:users.size();
    }

    @Override
    public User getItem(int position) {
        return users==null?null:users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position) == null?0:getItem(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserHolder holder = null;
        if(convertView == null){
            convertView =LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_invite_user, parent, false);
            TextView name = convertView.findViewById(R.id.username);
            TextView mobile = convertView.findViewById(R.id.mobile);
            ImageView status = convertView.findViewById(R.id.subscribe_status);
            holder = new UserHolder(name,mobile,status);
            convertView.setTag(holder);
        }
        holder = (UserHolder) convertView.getTag();
        holder.mobile.setText(getItem(position).getMobile());
        int imgId = R.drawable.unsubscriber;
        switch(getItem(position).getLevel()){
            case 0:
                imgId = R.drawable.unsubscriber;
                break;
            case 1:
                imgId = R.drawable.subscriber;
                break;
            case 2:
                imgId = R.drawable.subscriber_forever;
        }
        holder.status.setImageResource(imgId);
        if(hasContacts){
            UserHolder finalHolder = holder;
            contacts.forEach(contact -> {
                if(contact.getPhone().equals(getItem(position).getMobile())){
                    if(!contact.getNote().isEmpty())
                        finalHolder.name.setText(contact.getNote());
                    if(!contact.getName().isEmpty())
                        finalHolder.name.setText(contact.getName());
                }
            });
        }
        return convertView;
    }

    class UserHolder{
        TextView name;
        TextView mobile;
        ImageView status;

        public UserHolder(TextView name, TextView mobile, ImageView status) {
            this.name = name;
            this.mobile = mobile;
            this.status = status;
        }
    }
}
