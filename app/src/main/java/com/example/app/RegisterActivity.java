    package com.example.app;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

    import android.content.Intent;
    import android.os.Bundle;
    import android.text.TextUtils;
    import android.view.View;
    import android.widget.Button;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.example.app.Activity.HomeActivity;
    import com.example.app.WelcomeActivity;
    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.android.material.textfield.TextInputEditText;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;

    public class RegisterActivity extends AppCompatActivity {

        // Declare the UI elements
        TextInputEditText editTextEmail, editTextPassword, editTextPhoneNumber, editTextAddress;
        Button SignUp;
        TextView SignIn;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);

            // Initialize UI elements
            editTextEmail = findViewById(R.id.email);
            editTextPassword = findViewById(R.id.password);
            editTextPhoneNumber = findViewById(R.id.phonenumber); // Added phone number field
            editTextAddress = findViewById(R.id.postcode); // Added address field
            SignIn = findViewById(R.id.sign_in);
            SignUp = findViewById(R.id.Sign_up);

            // Handle the Sign In click event
            SignIn.setOnClickListener(v -> {
                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            });

            // Handle the Sign Up click event
            SignUp.setOnClickListener(v -> {
                String email = String.valueOf(editTextEmail.getText());
                String password = String.valueOf(editTextPassword.getText());
                String phoneNumber = String.valueOf(editTextPhoneNumber.getText()); // Get phone number
                String address = String.valueOf(editTextAddress.getText()); // Get address

                // Validate email and password input fields
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Register user with Firebase Authentication
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Registration successful
                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                // Call method to create user in Firebase Realtime Database
                                createUser(email, phoneNumber, address);
                            } else {
                                // Registration failed
                                Toast.makeText(RegisterActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            });
        }

        // Method to create user in Firebase Realtime Database
        private void createUser(String email, String phoneNumber, String address) {
            String userId = firebaseAuth.getCurrentUser().getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Users/" + userId);

            // Create new User object
            User user = new User(false, userId, email, phoneNumber, address);

            // Store the User data in the database
            myRef.setValue(user).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Navigate to WelcomeActivity upon successful user creation
                    Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // User creation failed
                    Toast.makeText(RegisterActivity.this, "Failed to create User.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
