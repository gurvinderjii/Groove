package com.codezlab.groove;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.provider.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.codezlab.groove.databinding.ActivityMainBinding;
import com.codezlab.groove.music.MusicFiles;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String[] permissions = {"android.permission.POST_NOTIFICATIONS",
                                    "android.permission.READ_MEDIA_AUDIO"};
    private int REQUEST_CODE = 123;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavBar.setOnItemSelectedListener(item -> {
            if (item.getItemId()==R.id.home){
                replaceFragment(new HomeFragment());
            } else if (item.getItemId()==R.id.liked) {
                replaceFragment(new LikedFragment());
            } else if (item.getItemId()==R.id.playlist) {
                replaceFragment(new PlaylistFragment());
            }
            return true;
        });
    }

    @Override
    protected void onStart() {
        if (!permissionGranted()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissions,REQUEST_CODE);
            }
        }
        super.onStart();
    }
    private boolean permissionGranted(){
        for (String permission :permissions) {
            if (ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode ==REQUEST_CODE){
            boolean allPermissionGranted = true;
            for (int granResult: grantResults) {
                if (granResult!=PackageManager.PERMISSION_GRANTED){
                    allPermissionGranted = false;
                    break;
                }
            }
            if (allPermissionGranted){
                return;
            }else {
                Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.nav_view,fragment).commit();
    }

    public void hideKeyborard(){
//        InputMethodManager imm = getSystemService();
    }
}