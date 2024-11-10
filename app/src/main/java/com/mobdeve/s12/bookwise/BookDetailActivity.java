package com.mobdeve.s12.bookwise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {
    private LinearLayout btnHome, btnSearch, btnAdd, btnCollection, btnGoal;
    private Button btnSubmit;
    private RecyclerView rv_review_item;

    private ImageView bookImageID;
    private TextView bookTitle, bookAuthor, bookSummary, bookGenres, bookDate;
    private EditText content;
    private RatingBar ratingResult;

    private Bookitem currentBook;
    private BookDetailActivityAdapter activityBookDetailAdapter;
    private List<Reviews> reviewList;
    private Reviews reviews;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    String userId;
    String bookId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userId = auth.getCurrentUser().getUid();

        reviewList = new ArrayList<>();
        // Initialize RecyclerView

        fetchReviews();

        initViews();
        data();
        btnHome.setOnClickListener(v -> homePage());
        btnSearch.setOnClickListener(v -> searchPage());
        btnAdd.setOnClickListener(v -> addPage());
        btnCollection.setOnClickListener(v -> collectionPage());
        btnGoal.setOnClickListener(v -> goalPage());
        btnSubmit.setOnClickListener(v -> submitReview());
    }

    public void initViews(){
        btnHome = findViewById(R.id.h_home_btn);
        btnSearch = findViewById(R.id.h_search_btn);
        btnAdd = findViewById(R.id.h_add_btn);
        btnCollection = findViewById(R.id.h_collection_btn);
        btnGoal = findViewById(R.id.h_goal_btn);
        btnSubmit = findViewById(R.id.bd_submit);
        rv_review_item = findViewById(R.id.rv_review_item);

        content = findViewById(R.id.bd_content);
        ratingResult = findViewById(R.id.bd_rating);

        bookImageID = findViewById(R.id.bd_imageID);
        bookTitle = findViewById(R.id.bd_title);
        bookAuthor = findViewById(R.id.bd_author);
        bookGenres = findViewById(R.id.bd_place);
        bookDate = findViewById(R.id.bd_date);
        bookSummary = findViewById(R.id.bd_summary);

        rv_review_item.setLayoutManager(new LinearLayoutManager(this));
        activityBookDetailAdapter = new BookDetailActivityAdapter(reviewList);
        rv_review_item.setAdapter(activityBookDetailAdapter);
    }

    public void data(){
        Intent intent = getIntent();
        bookId = intent.getStringExtra("bookId");
        System.out.println(bookId);
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        String genres = intent.getStringExtra("genres");
        String summary = intent.getStringExtra("summary");
        String date = intent.getStringExtra("date");
        String imageURL = intent.getStringExtra("imageURL");

        bookTitle.setText(title);
        bookAuthor.setText("By: "+ author);
        bookGenres.setText(genres);
        bookSummary.setText(summary);
        bookDate.setText(date);
        Glide.with(this)
                .load(imageURL)  // Use the image URL retrieved from the intent
                .placeholder(R.drawable.google)  // Optional: a placeholder while the image loads
                .error(R.drawable.logo)  // Optional: an error image if loading fails
                .into(bookImageID);
    }

    public void submitReview(){
        // Get the content from EditText
        String reviewContent = content.getText().toString().trim();
        // Get the rating from RatingBar (it returns a float)
        float rating = ratingResult.getRating();

        // Check if the content is empty or rating is not set
        if (reviewContent.isEmpty() || rating == 0) {
            Toast.makeText(this, "Please provide a valid review and rating", Toast.LENGTH_SHORT).show();
            return;
        }
        Reviews newReview = new Reviews(userId, bookId, reviewContent, rating);

        db.collection("reviews")
                .add(newReview)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(BookDetailActivity.this, "Review submitted successfully!", Toast.LENGTH_SHORT).show();
                    // Optionally, you can navigate back or update the UI
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(BookDetailActivity.this, "Error submitting review", Toast.LENGTH_SHORT).show();
                });
    }

    public void fetchReviews() {
        // Get the book ID from the intent (it was passed when navigating to BookDetailActivity)
        Intent intent = getIntent();
        String bookId = intent.getStringExtra("bookId");

        // Fetch reviews for the current book
        db.collection("reviews")
                .whereEqualTo("bookId", bookId) // Filter reviews by bookId
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Loop through the query result and create Review objects
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Reviews review = document.toObject(Reviews.class);
                            reviewList.add(review);
                        }
                        // Set up the RecyclerView adapter
                        activityBookDetailAdapter.notifyDataSetChanged();
                    } else {
                        loadDefaultReview();
                        // No reviews found for the book
                        Toast.makeText(BookDetailActivity.this, "No reviews found for this book", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(BookDetailActivity.this, "Error fetching reviews", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadDefaultReview() {
        // Load books from the DataGeneratorBooks singleton if no user collection exists
        reviewList = new ArrayList<>();
        activityBookDetailAdapter = new BookDetailActivityAdapter(reviewList);
        rv_review_item.setAdapter(activityBookDetailAdapter);
        activityBookDetailAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Loaded default book list.", Toast.LENGTH_SHORT).show();
    }

    public void homePage(){
        Intent intent = new Intent(BookDetailActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void searchPage(){
        Intent intent = new Intent(BookDetailActivity.this, SearchActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void addPage(){
        Intent intent = new Intent(BookDetailActivity.this, AdvanceSearchActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void collectionPage(){
        Intent intent = new Intent(BookDetailActivity.this, CollectionActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    public void goalPage(){
        Intent intent = new Intent(BookDetailActivity.this, GoalSettingActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}