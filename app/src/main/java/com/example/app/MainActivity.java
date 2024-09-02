    package com.example.app;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;


    import android.content.Intent;
    import android.os.Bundle;
    import android.text.TextUtils;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.example.app.Activity.AdminActivity;
    import com.example.app.Activity.HomeActivity;
    import com.example.app.model.OrderInfo;
    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.Task;
    import com.google.android.material.textfield.TextInputEditText;
    import com.google.firebase.auth.AuthResult;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    public class MainActivity extends AppCompatActivity {

        TextInputEditText editTextEmail, editTextPassword;
        Button SignIn;
        TextView SignUp;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            editTextEmail = findViewById(R.id.email);
            editTextPassword = findViewById(R.id.password);
            SignIn = findViewById(R.id.sign_in);
            SignUp = findViewById(R.id.sign_up);

            SignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();

                }
            });

            SignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email, password;
                    email = String.valueOf(editTextEmail.getText());
                    password = String.valueOf(editTextPassword.getText());


                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(MainActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(MainActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    String userId = firebaseAuth.getCurrentUser().getUid();
                                    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users/"+userId);

                                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                User user = snapshot.getValue(User.class);
                                                UserAuth.getInstance().setUser(user);

                                                if (user.isAdmin()) {
                                                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                                    startActivity(intent);
                                                    finish();                            }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }


                                    });
                }
            });

        }

//            private void getUserAndLaunchActivity(@NonNull Task<AuthResult> task) {
//                String userId = firebaseAuth.getCurrentUser().getUid();
//                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users/"+userId);
//
//                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()) {
//                            User user = snapshot.getValue(User.class);
//
//                            if (user.isAdmin()) {
//                                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(MainActivity.this, AdminActivity.class);
//                                startActivity(intent);
//                                finish();
//                            } else {
//                                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                                startActivity(intent);
//                                finish();                            }
//                        }
//                    }
            });

        }

    }