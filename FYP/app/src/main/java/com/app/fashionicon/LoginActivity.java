package com.app.fashionicon;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fashionicon.R;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button signUp;
    ArrayList<Users> accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accounts = new ArrayList<>();

        username = findViewById(R.id.signUp);
        password = findViewById(R.id.Password);
        signUp = findViewById(R.id.SignUp);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = username.getText().toString();
                String inputPassword = password.getText().toString();

                if (!isValidEmail(email)) {
                    Toast.makeText(getApplicationContext(), " There is an error in email!", Toast.LENGTH_SHORT).show();
                } else if (!isValidPassword(inputPassword)) {
                    Toast.makeText(getApplicationContext(), " Password failed!", Toast.LENGTH_SHORT).show();
                } else {
                    Users newUser = new Users(email, inputPassword);
                    accounts.add(newUser); // add new user to the array
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("Welcome!", accounts.get(0).getEmail());
                    startActivity(intent);
                }
            }

            private boolean isValidEmail(String email) {
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                return email.matches(emailPattern) && email.length() > 0;
            }

            private boolean isValidPassword(String password) {
                if (password.length() < 6 || password.contains(" ")) {
                    return false;
                }

                boolean hasDigit = false;
                for (char c : password.toCharArray()) {
                    if (Character.isDigit(c)) {
                        hasDigit = true;
                        break;
                    }
                }
                return hasDigit;
            }
        });
    }
}
