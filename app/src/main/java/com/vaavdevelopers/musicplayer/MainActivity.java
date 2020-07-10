package com.vaavdevelopers.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_REQUEST = 1;

    ArrayList<String> arrayList;
    ListView songList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED ){
            if(ActivityCompat.shouldShowRequestPermissionRationale
                    (MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSION_REQUEST);
            }
        } else {
            showSongsList();
        }


        //runtimePermission();
    }

    public void showSongsList() {
        songList = findViewById(R.id.mySongListView);
        arrayList = new ArrayList<>();
        getMusic();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        songList.setAdapter(adapter);

        songList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //open music player to play desired song
            }
        });
    }

    public void runtimePermission() {

        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public void getMusic() {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        long id = 1;
        Uri singleSongUri = MediaStore.Audio.Media.getContentUri("external");


        Cursor songCursor = contentResolver.query(songUri, null, null, null, null);

        if(songCursor != null && songCursor.moveToFirst())
        {
            int songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);

            do {
                String currentTitle = songCursor.getString(songTitle);
                String currentArtist = songCursor.getString(songArtist);
                arrayList.add("Title: "+currentTitle + "\n" +
                        "Artist: "+currentArtist);
            } while(songCursor.moveToNext());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case MY_PERMISSION_REQUEST: {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    if(ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();

                        showSongsList();
                    }
                } else {
                    Toast.makeText(this, "No permission granted!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return ;
            }
        }
    }
}
