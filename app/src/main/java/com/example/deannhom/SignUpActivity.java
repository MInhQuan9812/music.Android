package com.example.deannhom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import java.util.regex.Pattern;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    private EditText mFullNameEditText;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private Button mSignUpButton;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize SharedPreferences
        mSharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Initialize views
        mFullNameEditText = findViewById(R.id.fullNameEditText);
        mEmailEditText = findViewById(R.id.emailEditText);
        mPasswordEditText = findViewById(R.id.passwordEditText);
        mConfirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        mSignUpButton = findViewById(R.id.signUpButton);

        // Set onClickListener for signUpButton
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get user input
                String fullName = mFullNameEditText.getText().toString();
                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                String confirmPassword = mConfirmPasswordEditText.getText().toString();


                // Check if input is valid
                if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Please enter all required information", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 10) {
                    Toast.makeText(SignUpActivity.this, "Password must be at least 10 characters long", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(email)) {
                    Toast.makeText(SignUpActivity.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {


                    // If input is valid, save sign-up information and go to home activity
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString("fullName", fullName);
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.apply();

                    startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
                    finish();
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        // Regular expression for email validation
        String regex = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(email).matches();
    }
}
