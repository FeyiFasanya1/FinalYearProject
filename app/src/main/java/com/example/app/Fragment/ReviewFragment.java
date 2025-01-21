    package com.example.app.Fragment;

    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;

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

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_review, container, false);
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            String itemId = getArguments() != null ? getArguments().getString("itemId") : null;
            if (itemId != null) {
                initList(view, itemId);
            }
        }

        private void initList(View view, String itemId) {
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Review");
            ArrayList<ReviewDomain> list = new ArrayList<>();
            Query query = myRef.orderByChild("ItemId").equalTo(itemId);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot issue : snapshot.getChildren()) {
                            ReviewDomain review = issue.getValue(ReviewDomain.class);
                            if (review != null) {
                                list.add(review);
                            }
                        }
                        RecyclerView reviewRecyclerView = view.findViewById(R.id.reviewView);
                        if (!list.isEmpty()) {
                            ReviewAdapter adapter = new ReviewAdapter(list);
                            reviewRecyclerView.setAdapter(adapter);
                            reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

                public void refreshReviews(String orderId) {
                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Reviews").child(orderId);
                    ArrayList<String> reviews = new ArrayList<>();

                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            reviews.clear();
                            if (snapshot.exists()) {
                                String reviewText = snapshot.getValue(String.class);
                                if (reviewText != null) {
                                    reviews.add(reviewText);
                                }
                            }
                            // Update RecyclerView with reviews
                            RecyclerView reviewRecyclerView = getView().findViewById(R.id.reviewView);
                            reviewRecyclerView.setAdapter(new ReviewAdapter(reviews));
                            reviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });

        }
    }