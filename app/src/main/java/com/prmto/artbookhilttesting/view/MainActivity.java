package com.prmto.artbookhilttesting.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.prmto.artbookhilttesting.R;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Inject
    ArtFragmentFactory fragmentFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().setFragmentFactory(fragmentFactory);
        setContentView(R.layout.activity_main);
    }
}
