package com.javiervillalpando.jamout.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.javiervillalpando.jamout.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {
   private Context context;
   private List<ParseUser> following;

    public FollowingAdapter(Context context, List<ParseUser> following){
        this.context = context;
        this.following = following;
   }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_following,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingAdapter.ViewHolder holder, int position) {
        ParseUser userFollowing = following.get(position);
        holder.bind(userFollowing);

    }

    @Override
    public int getItemCount() {
        return following.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView followingProfilePicture;
        private TextView followingUsername;

        public ViewHolder(View itemView){
            super(itemView);
            followingProfilePicture = itemView.findViewById(R.id.followingProfilePicture);
            followingUsername = itemView.findViewById(R.id.followingUsername);
        }

        public void bind(ParseUser userFollowing) {
            followingUsername.setText(userFollowing.getUsername());
            ParseFile image = (ParseFile) userFollowing.get("profilePicture");
            String imageUrl = "";
            if(image != null){
                imageUrl = image.getUrl();
            }
            if(imageUrl != ""){
                Glide.with(context).load(imageUrl).circleCrop().into(followingProfilePicture);
            }
        }
    }
}
