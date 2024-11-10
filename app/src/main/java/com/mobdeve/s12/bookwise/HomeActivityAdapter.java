package com.mobdeve.s12.bookwise;

import android.graphics.ColorSpace;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class HomeActivityAdapter extends RecyclerView.Adapter<HomeActivityAdapter.ViewHolder> {

    private List<Bookitem> bookitemList;
    private OnCollectClickListener collectClickListener;
    private String userId;

    private FirebaseAuth auth;

    public HomeActivityAdapter(List<Bookitem> bookitemList, OnCollectClickListener collectClickListener,String userId) {
        this.bookitemList = bookitemList;
        this.collectClickListener = collectClickListener;
        this.userId = userId;
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
        holder.h_MarkAsRead.setChecked(book.isMarked());

        if (book.isMarked()) {
            holder.h_MarkAsRead.setBackgroundResource(R.drawable.unmark_as_read);
        } else {
            holder.h_MarkAsRead.setBackgroundResource(R.drawable.mark_as_read);
        }

        if (book.isCollected()) {
            holder.h_CollectButton.setBackgroundResource(R.drawable.on_book_mark);
        } else {
            holder.h_CollectButton.setBackgroundResource(R.drawable.off_book_mark);
        }

        Glide.with(holder.itemView.getContext())
                .load(book.getImageURL())
                .placeholder(R.drawable.google)
                .error(R.drawable.bookwise_logo)
                .into(holder.h_ActivityImage);

        // Check Firestore to see if the book is already in the user’s collection
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("bookCollection")
                .document(book.getBookId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    boolean isCollected = documentSnapshot.exists(); // Toggle button based on existence
                    holder.h_CollectButton.setChecked(isCollected);
                    holder.h_CollectButton.setBackgroundResource(isCollected ? R.drawable.on_book_mark : R.drawable.off_book_mark);
                    book.setCollected(isCollected); // Update the collection state of the book in the list
                })
                .addOnFailureListener(e -> {
                    // Handle errors (optional)
                    holder.h_CollectButton.setChecked(false); // Default to not collected on error
                    holder.h_CollectButton.setBackgroundResource(R.drawable.off_book_mark);
                });

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("bookMark")
                .document(book.getBookId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    boolean isMarked = documentSnapshot.exists(); // Toggle button based on existence
                    holder.h_MarkAsRead.setChecked(isMarked);
                    holder.h_MarkAsRead.setBackgroundResource(isMarked ? R.drawable.unmark_as_read : R.drawable.mark_as_read);
                    book.setMarked(isMarked); // Update the collection state of the book in the list
                })
                .addOnFailureListener(e -> {
                    // Handle errors (optional)
                    holder.h_MarkAsRead.setChecked(false); // Default to not collected on error
                    holder.h_MarkAsRead.setBackgroundResource(R.drawable.mark_as_read);
                });

        holder.h_CollectButton.setOnClickListener(v -> {
            boolean isCollected = holder.h_CollectButton.isChecked();
            collectClickListener.onCollectClick(book, isCollected, userId);
            book.setCollected(isCollected); // Update the collection state of the book
            holder.h_CollectButton.setBackgroundResource(isCollected ? R.drawable.on_book_mark : R.drawable.off_book_mark);
        });

        holder.h_MarkAsRead.setOnClickListener(v -> {
            boolean isMarked = holder.h_MarkAsRead.isChecked();
            collectClickListener.onMarkBookClick(book, isMarked, userId);
            book.setMarked(isMarked); // Update the collection state of the book
            holder.h_MarkAsRead.setBackgroundResource(isMarked ? R.drawable.unmark_as_read : R.drawable.mark_as_read);
            // Reference to the user's Firestore document
            DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(userId);

            // Fetch the current numBookRead and update it
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Long numBookRead = documentSnapshot.getLong("numBookRead");
                    if (numBookRead == null) numBookRead = 0L; // Set to 0 if null

                    // Increment or decrement based on toggle state
                    long updatedCount = isMarked ? numBookRead + 1 : numBookRead - 1;

                    // Update Firestore with the new count
                    userRef.update("numBookRead", updatedCount)
                            .addOnSuccessListener(aVoid -> Log.d("Firestore", "numBookRead updated successfully"))
                            .addOnFailureListener(e -> Log.e("Firestore", "Error updating numBookRead", e));
                }
            }).addOnFailureListener(e -> Log.e("Firestore", "Error fetching user document", e));
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), BookDetailActivity.class);
            intent.putExtra("bookId", book.getBookId());
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
        ToggleButton h_CollectButton, h_MarkAsRead;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            h_ActivityImage = itemView.findViewById(R.id.hi_image);
            h_ActivityTitle = itemView.findViewById(R.id.hi_title);
            h_ActivityAuthor = itemView.findViewById(R.id.hi_author);
            h_ActivityRating = itemView.findViewById(R.id.hi_rating);
            h_CollectButton = itemView.findViewById(R.id.hi_collect);
            h_MarkAsRead = itemView.findViewById(R.id.hi_markAsRead);
        }
    }

    public interface OnCollectClickListener {
        void onCollectClick(Bookitem item, boolean isCollected, String userId);
        void onMarkBookClick(Bookitem item, boolean isCollected, String userId);
    }
}

