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

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class SearchActivityAdapter extends RecyclerView.Adapter<SearchActivityAdapter.ViewHolder>{

    private List<Bookitem> bookitemList;
    private OnCollectClickListener collectClickListener;
    private String userId;

    public SearchActivityAdapter(List<Bookitem> bookitemList, OnCollectClickListener collectClickListener, String userId) {
        this.bookitemList = bookitemList;
        this.collectClickListener = collectClickListener;
        this.userId = userId;
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
        holder.ActivityTitle.setText(book.getTitle());
        holder.ActivityAuthor.setText("By: "+ book.getAuthor());
        holder.ActivityGenres.setText("Genres: \n"+ book.getGenres());
        holder.ActivityDate.setText("Published: \n"+ book.getDate());
        holder.CollectButton.setChecked(book.isCollected());

        if (book.isCollected()) {
            holder.CollectButton.setBackgroundResource(R.drawable.on_book_mark);
        } else {
            holder.CollectButton.setBackgroundResource(R.drawable.off_book_mark);
        }

        Glide.with(holder.itemView.getContext())
                .load(book.getImageURL())  // URL from the book item
                .placeholder(R.drawable.google)  // Optional: show a placeholder while the image loads
                .error(R.drawable.bookwise_logo)  // Optional: show this if there's an error loading the image
                .into(holder.ActivityImage);

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("bookCollection")
                .document(book.getBookId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    boolean isCollected = documentSnapshot.exists(); // Toggle button based on existence
                    holder.CollectButton.setChecked(isCollected);
                    holder.CollectButton.setBackgroundResource(isCollected ? R.drawable.on_book_mark : R.drawable.off_book_mark);
                    book.setCollected(isCollected); // Update the collection state of the book in the list
                })
                .addOnFailureListener(e -> {
                    // Handle errors (optional)
                    holder.CollectButton.setChecked(false); // Default to not collected on error
                    holder.CollectButton.setBackgroundResource(R.drawable.off_book_mark);
                });

        holder.CollectButton.setOnClickListener(v -> {
            boolean isCollected = holder.CollectButton.isChecked();
            collectClickListener.onCollectClick(book, isCollected, userId);
            book.setCollected(isCollected); // Update the collection state of the book
            holder.CollectButton.setBackgroundResource(isCollected ? R.drawable.on_book_mark : R.drawable.off_book_mark);
        });
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
    }
    public interface OnCollectClickListener {
        void onCollectClick(Bookitem item, boolean isCollected, String userId);
    }
}
