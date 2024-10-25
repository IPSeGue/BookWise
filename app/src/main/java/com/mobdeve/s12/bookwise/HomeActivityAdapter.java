package com.mobdeve.s12.bookwise;

import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class HomeActivityAdapter extends RecyclerView.Adapter<HomeActivityAdapter.ViewHolder> {

    private List<Bookitem> bookitemList;
    private OnCollectClickListener collectClickListener;

    public HomeActivityAdapter(List<Bookitem> bookitemList, OnCollectClickListener collectClickListener) {
        this.bookitemList = bookitemList;
        this.collectClickListener = collectClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_home_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bookitem book = bookitemList.get(position);
        holder.h_ActivityTitle.setText(book.getTitle());
        holder.h_ActivityAuthor.setText("By: "+ book.getAuthor());
        holder.h_ActivityRating.setRating(book.getRating());
        holder.h_CollectButton.setChecked(book.isCollected());

        Glide.with(holder.itemView.getContext())
                .load(book.getImageURL())
                .placeholder(R.drawable.google)
                .error(R.drawable.bookwise_logo)
                .into(holder.h_ActivityImage);

        holder.h_CollectButton.setOnClickListener(v -> {
            boolean isCollected = holder.h_CollectButton.isChecked();
            collectClickListener.onCollectClick(book, isCollected);
            book.setCollected(isCollected); // Update the collection state of the book
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), BookDetailActivity.class);
            intent.putExtra("title", book.getTitle());
            intent.putExtra("author", book.getAuthor());
            intent.putExtra("genres", book.getGenres());
            intent.putExtra("summary", book.getSummary());
            intent.putExtra("date", book.getDate());
            intent.putExtra("imageURL", book.getImageURL());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bookitemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView h_ActivityImage;
        TextView h_ActivityTitle, h_ActivityAuthor;
        RatingBar h_ActivityRating;
        ToggleButton h_CollectButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            h_ActivityImage = itemView.findViewById(R.id.hi_image);
            h_ActivityTitle = itemView.findViewById(R.id.hi_title);
            h_ActivityAuthor = itemView.findViewById(R.id.hi_author);
            h_ActivityRating = itemView.findViewById(R.id.hi_rating);
            h_CollectButton = itemView.findViewById(R.id.hi_collect);
        }
    }

    public interface OnCollectClickListener {
        void onCollectClick(Bookitem item, boolean isCollected);
    }
}

