
package com.example.deannhom;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.view.MenuItem;
import android.view.Menu;

import com.example.deannhom.adapter.MusicListAdapter;
import com.example.deannhom.adapter.RecyclerViewAdapter;
import com.example.deannhom.fragment.AccountFragment;
import com.example.deannhom.fragment.HomeFragment;
import com.example.deannhom.fragment.PlaylistFragment;
import com.example.deannhom.model.AudioModel;
import com.example.deannhom.model.Upload;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class MainActivity extends AppCompatActivity {
    private View background;
    private boolean isUserLoggedIn = false;
    private String username = "";
    private String password = "";
    private MenuItem logoutMenuItem;
    BottomNavigationView mnBottom;
    RecyclerView recyclerView;
    TextView noMusicTextView;
    ArrayList<AudioModel> songsList = new ArrayList<>();

    RecyclerViewAdapter adapter;
    DatabaseReference mDatabase;
    ProgressDialog progressDialog;
    private List<Upload> uploads;


    private HomeFragment home = new HomeFragment();
    private String homeTag = "home";

    private PlaylistFragment playList = new PlaylistFragment();
    private String playListTag = "playList";

    private AccountFragment accountFragment = new AccountFragment();
    private String accountTag = "account";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploads = new ArrayList<>();

        initView();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Main");

    }

    private void initView() {

        mnBottom = findViewById(R.id.navMenu);
        mnBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.mnAll:
                        selectFragment(home, homeTag);
                        getSupportActionBar().setTitle(menuItem.getTitle());
                        return true;
                    case R.id.mnPlaylist:
                        selectFragment(playList, playListTag);
                        getSupportActionBar().setTitle(menuItem.getTitle());
                        return true;
                    case R.id.mnAccount:
                        getSupportActionBar().setTitle(menuItem.getTitle());
                        selectFragment(accountFragment, accountTag);
                        return true;
                }
                return true;
            }
        });
        selectFragment(home, homeTag);
    }


    private void selectFragment(Fragment fragment, String fragmentTag) {
        FragmentManager fgm = getSupportFragmentManager();
        FragmentTransaction fmTransaction = fgm.beginTransaction();
        Fragment currentFragment = fgm.getPrimaryNavigationFragment();
        Fragment fragmentTemp = fgm.findFragmentByTag(fragmentTag);

        if (currentFragment != null) {
            fmTransaction.hide(currentFragment);
        }

        if (fragmentTemp == null) {

            fragmentTemp = fragment;
            fmTransaction.add(R.id.fragmentContainer, fragmentTemp, fragmentTag);
        } else {
            fmTransaction.show(fragmentTemp);
        }

        fmTransaction.setPrimaryNavigationFragment(fragmentTemp);
        fmTransaction.setReorderingAllowed(true);
        fmTransaction.commitNowAllowingStateLoss();
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

    boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "READ PERMISSION IS REQUIRED,PLEASE ALLOW FROM SETTTINGS", Toast.LENGTH_SHORT).show();
        } else
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerView != null) {
            recyclerView.setAdapter(new MusicListAdapter(songsList, getApplicationContext()));
        }
    }
}