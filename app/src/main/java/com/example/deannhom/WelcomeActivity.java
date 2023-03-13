package com.example.deannhom;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class WelcomeActivity extends AppCompatActivity {

    private View background;
    private SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        background = (View) findViewById(R.id.background);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            background.setBackground(this.getDrawable(R.drawable.drawable_linear_bg_api31));
        } else {
            /**
             * If the current version is under S version, it does not allow to set angle is under 45.
             * In others word, it must be multiple of 45.
             */
            background.setBackground(this.getDrawable(R.drawable.drawable_linear_bg_api26));
        }

        Button loginButton = findViewById(R.id.signInBtn);
        Button registerButton = findViewById(R.id.signUpBtn);
        mSharedPreferences = getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);

        // if (mSharedPreferences.getBoolean("loggedIn", false)) {
        // ĐỪNG UNCOMMENT NÓ, LOGOUT VỚI DELETE ACCOUNT KHÔNG HOẠT ĐỘNG
        //    startActivity(new Intent(WelcomeActivity.this, HomeActivity.class));
       //     finish();
      //  }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });

    }
}