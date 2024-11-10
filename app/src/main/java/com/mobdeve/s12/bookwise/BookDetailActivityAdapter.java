package com.mobdeve.s12.bookwise;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class BookDetailActivityAdapter extends RecyclerView.Adapter<BookDetailActivityAdapter.ReviewViewHolder>{
    FirebaseFirestore db;
    private List<Reviews> reviewList;

    public BookDetailActivityAdapter(List<Reviews> reviewList) {
        this.reviewList = reviewList;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_book_detail_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        Reviews review = reviewList.get(position);
        holder.reviewContent.setText(review.getContent());
        holder.ratingBar.setRating(review.getRating());
        holder.ratingNum.setText(review.getRating() + "/5");

        String userId = review.getUserId();
        db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Get user details from the document
                        Users user = documentSnapshot.toObject(Users.class);

                        if (user != null) {
                            // Set the user name, email, and profile image (if available)
                            holder.userName.setText(user.getFullName());
                            System.out.println(user.getImageUrl());
                            Glide.with(holder.itemView.getContext())
                                    .load(user.getImageUrl())// Load the image URL into ImageView
                                    .placeholder(R.drawable.default_dp)// Optional placeholder
                                    .error(R.drawable.error_profile)// Optional: error image if URL fails
                                    .into(holder.userImage); // Set the image in ImageView
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the error (e.g., if the user doesn't exist)
                    Toast.makeText(holder.itemView.getContext(), "Error fetching user details", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView reviewContent;
        RatingBar ratingBar;
        TextView ratingNum;
        TextView userName;  // If you want to display the user name

        public ReviewViewHolder(View itemView) {
            super(itemView);
            reviewContent = itemView.findViewById(R.id.bdi_content);
            ratingBar = itemView.findViewById(R.id.bdi_rating);
            ratingNum = itemView.findViewById(R.id.bdi_ratingNum);
            userName = itemView.findViewById(R.id.bdi_user); // If you want to show the user's name
            userImage = itemView.findViewById(R.id.bdi_image);
        }
    }
}
