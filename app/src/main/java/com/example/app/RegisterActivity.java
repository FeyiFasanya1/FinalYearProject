    package com.example.app;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;

    import android.annotation.SuppressLint;
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

        TextInputEditText editTextEmail, editTextPassword;
        Button SignUp;
        TextView SignIn;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register);

            editTextEmail = findViewById(R.id.email);
            editTextPassword = findViewById(R.id.password);
            SignIn = findViewById(R.id.sign_in);
            SignUp = findViewById(R.id.Sign_up);

            SignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                }
            });

            SignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email, password;
                    email = String.valueOf(editTextEmail.getText());
                    password = String.valueOf(editTextPassword.getText());

                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(RegisterActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(RegisterActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                        createUser();
                                    }
                                    else {
                                        Toast.makeText(RegisterActivity.this, "Authentication Has Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });
        }
        private void createUser() {
            String userId = firebaseAuth.getCurrentUser().getUid();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("Users/" + userId);

            User user = new User(false, userId);

            myRef.setValue(user).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Data added successfully
                    Intent intent = new Intent(RegisterActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Handle the error
                    Toast.makeText(RegisterActivity.this, "Failed to create User.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }