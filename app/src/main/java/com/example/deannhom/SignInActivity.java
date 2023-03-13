package com.example.deannhom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class SignInActivity extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mSignInButton;

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Get references to UI elements
        mEmailEditText = findViewById(R.id.emailEditText);
        mPasswordEditText = findViewById(R.id.passwordEditText);
        mSignInButton = findViewById(R.id.signInButton);

        // Get reference to SharedPreferences
        mSharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Set click listener for sign in button
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String email = mEmailEditText.getText().toString().trim();
                String password = mPasswordEditText.getText().toString().trim();

                // Check if input is valid
                if (!isValidEmail(email)) {
                    Toast.makeText(SignInActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidPassword(password)) {
                    Toast.makeText(SignInActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if email and password match saved information
                String savedEmail = mSharedPreferences.getString("email", "");
                String savedPassword = mSharedPreferences.getString("password", "");

                if (email.equals(savedEmail) && password.equals(savedPassword)) {
                    // If the email and password are valid, do the login here
                    Toast.makeText(SignInActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignInActivity.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        if (email.isEmpty()) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    private boolean isValidPassword(String password) {
        if (password.isEmpty()) {
            return false;
        } else {
            // Define password pattern
            String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
            Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

            // Check if password matches pattern
            return pattern.matcher(password).matches();
        }
    }
}
