    package com.example.app.Activity;

    import android.content.Intent;
    import android.os.Bundle;
    import android.text.TextUtils;
    import android.view.View;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import com.example.app.MainActivity;
    import com.example.app.databinding.ActivityProfileBinding;
    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    public class ProfileActivity extends AppCompatActivity {

        private ActivityProfileBinding binding;
        private DatabaseReference userRef;
        private FirebaseUser currentUser;
        private FirebaseAuth firebaseAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Initialize ViewBinding
            binding = ActivityProfileBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            // Initialize Firebase Auth and get current user
            firebaseAuth = FirebaseAuth.getInstance();
            currentUser = firebaseAuth.getCurrentUser();

            if (currentUser != null) {
                // Reference to the current user's data in Firebase Realtime Database
                userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());

                // Load user data and populate the EditTexts
                loadUserData();

                // Set up button listeners
                setupButtonListeners();
            } else {
                // If no user is logged in, redirect to MainActivity
                Toast.makeText(this, "No user is logged in", Toast.LENGTH_SHORT).show();
                redirectToMain();
            }
        }

        private void loadUserData() {
            // Show a loading indicator
            binding.progressBar.setVisibility(View.VISIBLE);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    binding.progressBar.setVisibility(View.GONE);
                    if (snapshot.exists()) {
                        // Retrieve phoneNumber and address from the snapshot
                        String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                        String address = snapshot.child("address").getValue(String.class);

                        // Populate the EditText fields if data exists
                        if (phoneNumber != null) {
                            binding.phoneNumber.setText(phoneNumber);
                        } else {
                            binding.phoneNumber.setHint("Enter phone number");
                        }

                        if (address != null) {
                            binding.address.setText(address);
                        } else {
                            binding.address.setHint("Enter address");
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    binding.progressBar.setVisibility(View.GONE);
                    Toast.makeText(ProfileActivity.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        /**
         * Sets up the click listeners for the buttons in the activity.
         */
        private void setupButtonListeners() {
            // Update Details Button
            binding.updateBtn.setOnClickListener(view -> updateUserDetails());

            // Back Button
            binding.backBtn4.setOnClickListener(v -> {
                // Start HomeActivity when back button is clicked
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();  // Finish current activity to remove it from the back stack
            });

            // Log Out Button
            binding.LogOutBtn.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        }

        /**
         * Updates the user's phone number and address in Firebase Realtime Database.
         */
        private void updateUserDetails() {
            String updatedPhoneNumber = binding.phoneNumber.getText().toString().trim();
            String updatedAddress = binding.address.getText().toString().trim();

            // Validate input fields
            if (TextUtils.isEmpty(updatedPhoneNumber)) {
                binding.phoneNumber.setError("Phone number cannot be empty");
                binding.phoneNumber.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(updatedAddress)) {
                binding.address.setError("Address cannot be empty");
                binding.address.requestFocus();
                return;
            }


            // Show a loading indicator
            binding.progressBar.setVisibility(View.VISIBLE);

            // Update phoneNumber and address in the database
            userRef.child("phoneNumber").setValue(updatedPhoneNumber);
            userRef.child("address").setValue(updatedAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            binding.progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, "Details updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProfileActivity.this, "Update failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        /**
         * Navigates the user back to the HomeActivity.
         */
        private void navigateToHome() {
            Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        /**
         * Logs out the user and redirects to MainActivity.
         */
        private void logoutUser() {
            firebaseAuth.signOut();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            redirectToMain();
        }

        /**
         * Redirects the user to MainActivity.
         */
        private void redirectToMain() {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }