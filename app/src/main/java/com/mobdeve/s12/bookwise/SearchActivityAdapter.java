package com.mobdeve.s12.bookwise;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchActivityAdapter extends RecyclerView.Adapter<SearchActivityAdapter.ViewHolder>{

    private List<Bookitem> bookitemList;
    private OnCollectClickListener collectClickListener;

    public SearchActivityAdapter(List<Bookitem> bookitemList, OnCollectClickListener collectClickListener) {
        this.bookitemList = bookitemList;
        this.collectClickListener = collectClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_search_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bookitem book = bookitemList.get(position);
        holder.ActivityImage.setImageResource(book.getImageID());
        holder.ActivityTitle.setText(book.getTitle());
        holder.ActivityAuthor.setText("By: " + book.getAuthor());
        holder.ActivityGenres.setText("Genres: \n" + book.getGenres());
        holder.ActivityDate.setText("Published: \n" + book.getDate());
        holder.bindData(book, collectClickListener);
    }

    @Override
    public int getItemCount() {
        return bookitemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ActivityImage;
        TextView ActivityTitle, ActivityAuthor, ActivityGenres, ActivityDate;
        ToggleButton CollectButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ActivityImage = itemView.findViewById(R.id.si_image);
            ActivityTitle = itemView.findViewById(R.id.si_title);
            ActivityAuthor = itemView.findViewById(R.id.si_author);
            ActivityGenres = itemView.findViewById(R.id.si_genres);
            ActivityDate = itemView.findViewById(R.id.si_publish);
            CollectButton = itemView.findViewById(R.id.si_collect);
        }
        public void bindData(Bookitem book, OnCollectClickListener collectClickListener) {
            ActivityTitle.setText(book.getTitle());
            ActivityTitle.setText(book.getAuthor());
            ActivityGenres.setText(book.getGenres());
            ActivityDate.setText(book.getDate());

            // Set the toggle button status based on whether the item is collected or not
            CollectButton.setChecked(book.isCollected());

            // ToggleButton click listener to mark the item as collected
            CollectButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                collectClickListener.onCollectClick(getAdapterPosition(), isChecked);
            });
        }
    }
}
