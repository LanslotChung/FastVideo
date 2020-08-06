package com.lanslot.fastvideo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lanslot.fastvideo.Bean.InviteUser;
import com.lanslot.fastvideo.Bean.MyContacts;
import com.lanslot.fastvideo.R;

import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {
    private ArrayList<InviteUser> users;
    private ArrayList<MyContacts> contacts;
    private boolean hasContacts = false;
    private Context context;

    public UserListAdapter(Context context, ArrayList<InviteUser> users, ArrayList<MyContacts> contacts) {
        this.users = users;
        this.contacts = contacts;
        hasContacts = contacts != null && contacts.size() > 0;
        this.context = context;
    }

    @NonNull
    @Override
    public UserListAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_invite_user, parent, false);
        return new UserListAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.UserViewHolder holder, int position) {
        holder.mobile.setText(users.get(position).getMobile());
        int imgId = R.drawable.unsubscriber;
        switch (users.get(position).getLevel()) {
            case 0:
                imgId = R.drawable.unsubscriber;
                break;
            case 1:
                imgId = R.drawable.subscriber;
                break;
            case 2:
                imgId = R.drawable.subscriber_forever;
        }
        holder.level.setImageResource(imgId);
        if(hasContacts){
            UserListAdapter.UserViewHolder finalHolder = holder;
            contacts.forEach(contact -> {
                if (contact.getPhone().equals(users.get(position).getMobile())) {
                    if (contact.getNote() != null && !contact.getNote().isEmpty())
                        finalHolder.name.setText(contact.getNote());
                    if (contact.getName() != null && !contact.getName().isEmpty())
                        finalHolder.name.setText(contact.getName());
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void loadMore(ArrayList<InviteUser> newInviteUser) {
        users.addAll(newInviteUser);
        notifyDataSetChanged();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView mobile;
        ImageView level;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.username);
            mobile = itemView.findViewById(R.id.mobile);
            level = itemView.findViewById(R.id.subscribe_status);
        }
    }
}
