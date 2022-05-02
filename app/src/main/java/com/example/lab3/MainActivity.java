package com.example.lab3;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private PhoneViewModel mPhoneViewModel;
    private PhoneListAdapter mAdapter;

    RecyclerView recyclerView;
    private FloatingActionButton mFabAdd;
    private ActivityResultLauncher<Intent> mActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.phonesRecyclerView);
        mAdapter = new PhoneListAdapter(this);
        recyclerView.setAdapter(mAdapter);

        mFabAdd = findViewById(R.id.fabAdd);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPhoneViewModel = new ViewModelProvider(this).get(PhoneViewModel.class);

        mPhoneViewModel.getAllPhones().observe(this, phones -> mAdapter.setPhoneList(phones));

        mFabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddPhoneActivity.class);
            mActivityResultLauncher.launch(intent);
        });

        mActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {}
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu); // TODO wyklad "paski aplikacji" - str 107
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete_all_phones) {
            mPhoneViewModel.deleteAll();
            Toast.makeText(this, "Deleted all phones", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}