
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
import android.view.MenuItem;
import android.view.Menu;


public class HomeActivity extends AppCompatActivity {

    private boolean isUserLoggedIn = false;
    private String username = "";
    private String password = "";

    private MenuItem logoutMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Check if user is already logged in
        isUserLoggedIn = checkUserLoginStatus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        logoutMenuItem = menu.findItem(R.id.menu_logout);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_logout) {
            clearUserSessionData();
            navigateToLoginScreen();
            return true;
        } else if (id == R.id.menu_delete_account && isUserLoggedIn) {
            deleteAccount();
            clearUserSessionData();
            navigateToLoginScreen();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void clearUserSessionData() {
        isUserLoggedIn = false;
        username = "";
        password = "";
    }

    private void navigateToLoginScreen() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    private void deleteAccount() {
        if (username.equals("example") && password.equals("password")) {
            // Delete the account
            Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkUserLoginStatus() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return prefs.getBoolean("isLoggedIn", false);
    }
}