package com.javiervillalpando.jamout.adapters;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.javiervillalpando.jamout.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class SearchUsersAdapter extends RecyclerView.Adapter<SearchUsersAdapter.ViewHolder> {
    private Context context;
    private List<ParseUser> userList;

    public SearchUsersAdapter(Context context, List<ParseUser> userList){
        this.context = context;
        this.userList = userList;
    }
    @NonNull
    @Override
    public SearchUsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_users, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchUsersAdapter.ViewHolder holder, int position) {
        ParseUser user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView searchUserProfilePicture;
        private TextView searchUserName;
        private Button searchUserFollowButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            searchUserProfilePicture = itemView.findViewById(R.id.searchUserProfilePicture);
            searchUserName = itemView.findViewById(R.id.searchUserName);
            searchUserFollowButton = itemView.findViewById(R.id.searchUserFollowButton);
        }

        public void bind(ParseUser user) {
            searchUserName.setText(user.getUsername());
            ParseFile image = (ParseFile) user.get("profilePicture");
            String imageUrl = "";
            if(image != null){
                imageUrl = image.getUrl();
            }
            if(imageUrl != ""){
                Glide.with(context).load(imageUrl).circleCrop().into(searchUserProfilePicture);
            }
        }
    }
}
