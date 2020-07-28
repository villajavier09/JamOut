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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.javiervillalpando.jamout.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class SearchUsersAdapter extends RecyclerView.Adapter<SearchUsersAdapter.ViewHolder> {
    private Context context;
    private List<ParseUser> userList;
    private OnFollowClickListener onFollowClickListener;
    private OnUserClickListener onUserClickListener;

    public  interface OnFollowClickListener {
        void OnFollowClicked(int position);
    }
    public interface  OnUserClickListener{
        void OnUserClickListener(int position);
    }

    public SearchUsersAdapter(Context context, List<ParseUser> userList, OnFollowClickListener onFollowClickListener, OnUserClickListener onUserClickListener){
        this.context = context;
        this.userList = userList;
        this.onFollowClickListener = onFollowClickListener;
        this.onUserClickListener = onUserClickListener;
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
        private ConstraintLayout itemUserContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            searchUserProfilePicture = itemView.findViewById(R.id.searchUserProfilePicture);
            searchUserName = itemView.findViewById(R.id.searchUserName);
            searchUserFollowButton = itemView.findViewById(R.id.searchUserFollowButton);
            itemUserContainer = itemView.findViewById(R.id.itemUserContainer);
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
            searchUserFollowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onFollowClickListener.OnFollowClicked(getAdapterPosition());
                }
            });
            itemUserContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onUserClickListener.OnUserClickListener(getAdapterPosition());
                }
            });
        }
    }
}
