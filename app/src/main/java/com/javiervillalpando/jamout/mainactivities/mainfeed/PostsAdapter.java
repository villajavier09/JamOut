package com.javiervillalpando.jamout.mainactivities.mainfeed;

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

import org.w3c.dom.Text;

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
        private TextView postDescription;
        private TextView postSongTitle;
        private TextView postArtistName;
        private ImageView postImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postUsername = itemView.findViewById(R.id.postUsername);
            postDescription = itemView.findViewById(R.id.postDescription);
            postSongTitle = itemView.findViewById(R.id.postSongTitle);
            postArtistName = itemView.findViewById(R.id.postArtistName);
            postImage = itemView.findViewById(R.id.postImage);
        }

        public void bind(Post post) throws ParseException {
            postUsername.setText(post.getUser().getUsername());
            postDescription.setText(post.getDescription());
            postSongTitle.setText(post.getSong().getSongTitle());
            postArtistName.setText(post.getSong().getArtist());
            Glide.with(context).load(post.getSong().getImageUrl()).into(postImage);
        }
    }
}