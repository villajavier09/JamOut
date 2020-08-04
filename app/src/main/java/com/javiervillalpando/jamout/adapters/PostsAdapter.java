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
import com.javiervillalpando.jamout.models.Post;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_model, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        try {
            holder.bind(post);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView postUsername;
        private TextView postType;
        private TextView postDescription;
        private TextView postSongTitle;
        private TextView postArtistName;
        private ImageView postImage;
        private ImageView postUserProfileImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postUsername = itemView.findViewById(R.id.postUsername);
            postDescription = itemView.findViewById(R.id.postDescription);
            postSongTitle = itemView.findViewById(R.id.postSongTitle);
            postArtistName = itemView.findViewById(R.id.postArtistName);
            postImage = itemView.findViewById(R.id.postImage);
            postUserProfileImage = itemView.findViewById(R.id.postUserProfileImage);
            postType = itemView.findViewById(R.id.postType);
        }

        public void bind(Post post) throws ParseException {
            postUsername.setText(post.getUser().getUsername());
            if(post.getType().equals("Song")||post.getType() == null){
                postDescription.setText(post.getDescription());
                postSongTitle.setText(post.getSong().getSongTitle());
                postArtistName.setText(post.getSong().getArtist());
                Glide.with(context).load(post.getSong().getImageUrl()).into(postImage);
            }
            if(post.getType().equals("Album")){
                postDescription.setText(post.getDescription());
                postSongTitle.setText(post.getAlbum().getAlbumTitle());
                postArtistName.setText(post.getAlbum().getArtist());
                Glide.with(context).load(post.getAlbum().getImageUrl()).into(postImage);
            }
            if(post.getType().equals("Artist")){
                postDescription.setText(post.getDescription());
                postSongTitle.setText(post.getArtist().getArtistName());
                postArtistName.setText(post.getArtist().getArtistGenre());
                Glide.with(context).load(post.getArtist().getArtistPicture()).into(postImage);
            }

            postType.setText(post.getType());
            ParseFile image = (ParseFile) post.getUser().get("profilePicture");
            String imageUrl = "";
            if(image != null){
                imageUrl = image.getUrl();
            }
            if(imageUrl != ""){
                Glide.with(context).load(imageUrl).circleCrop().into(postUserProfileImage);
            }
        }
    }
}