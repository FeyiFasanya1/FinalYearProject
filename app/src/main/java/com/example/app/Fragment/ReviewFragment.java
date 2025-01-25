    package com.example.app.Fragment;

    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.app.Adapter.ReviewAdapter;
    import com.example.app.Domain.ReviewDomain;
    import com.example.app.R;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.Query;
    import com.google.firebase.database.ValueEventListener;

    import java.util.ArrayList;

    public class ReviewFragment extends Fragment {

        private ArrayList<ReviewDomain> reviewList;
        private ReviewAdapter reviewAdapter;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_review, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            // Retrieve the productId passed to the fragment
            String productId = getArguments() != null ? getArguments().getString("productId") : null;

            if (productId != null) {
                setupRecyclerView(view);
                fetchReviews(productId);
            } else {
                Toast.makeText(getContext(), "No productId provided", Toast.LENGTH_SHORT).show();
            }
        }

        private void setupRecyclerView(View view) {
            RecyclerView recyclerView = view.findViewById(R.id.reviewView);
            reviewList = new ArrayList<>();
            reviewAdapter = new ReviewAdapter(reviewList);
            recyclerView.setAdapter(reviewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        private void fetchReviews(String productId) {
            DatabaseReference reviewRef = FirebaseDatabase.getInstance().getReference("Review");

            // Query reviews where productId matches
            Query query = reviewRef.orderByChild("productId").equalTo(productId);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    reviewList.clear(); // Clear old data
                    if (snapshot.exists()) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            ReviewDomain review = data.getValue(ReviewDomain.class);
                            if (review != null) {
                                reviewList.add(review); // Add review to the list
                            }
                        }
                        reviewAdapter.notifyDataSetChanged(); // Update the RecyclerView
                    } else {
                        Toast.makeText(getContext(), "No reviews found for this product", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Failed to load reviews: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
