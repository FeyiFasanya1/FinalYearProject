    package com.example.app.Adapter;

    import android.content.Context;
    import android.view.LayoutInflater;
    import android.view.ViewGroup;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;

    import com.bumptech.glide.Glide;
    import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
    import com.example.app.Domain.ReviewDomain;
    import com.example.app.R;
    import com.example.app.databinding.ViewholderReviewBinding;

    import java.util.ArrayList;

    public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.Viewholder> {

        // List of reviews to display
        private ArrayList<ReviewDomain> reviews;
        private Context context;

        // Constructor to initialize the review list
        public ReviewAdapter(ArrayList<ReviewDomain> reviews) {
            this.reviews = reviews;
        }

        @NonNull
        @Override
        public ReviewAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Inflate the custom ViewHolder layout using View Binding
            context = parent.getContext();
            ViewholderReviewBinding binding = ViewholderReviewBinding.inflate(LayoutInflater.from(context), parent, false);
            return new Viewholder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ReviewAdapter.Viewholder holder, int position) {
            // Get the current review item
            ReviewDomain review = reviews.get(position);

            // Bind the reviewText to the descTxt field
            holder.binding.descTxt.setText(review.getReviewText());

            // Optionally handle image (if picUrl is present in the review object)
            if (review.getPicUrl() != null && !review.getPicUrl().isEmpty()) {
                Glide.with(context)
                        .load(review.getPicUrl())
                        .transform(new GranularRoundedCorners(100, 100, 100, 100))
                        .into(holder.binding.pic);
            } else {
                // Optionally, hide the ImageView or show a placeholder if no image is provided
                holder.binding.pic.setImageResource(R.drawable.baseline_person_2); // Replace with your placeholder image

            }
        }

        @Override
        public int getItemCount() {
            return reviews.size();
        }

        // ViewHolder class
        public class Viewholder extends RecyclerView.ViewHolder {
            ViewholderReviewBinding binding;

            public Viewholder(ViewholderReviewBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }
    }
