package com.example.lab3;

import androidx.activity.result.ActivityResult;
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

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private PhoneViewModel mPhoneViewModel;
    private PhoneListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFabAdd;
    private ActivityResultLauncher<Intent> mActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectLayoutElementsWithFields();
        createAdapterForRecyclerView();
        connectRecyclerViewWithDatabase();
        setupForAddingNewPhone();
    }

    private void connectLayoutElementsWithFields() {
        mRecyclerView = findViewById(R.id.phonesRecyclerView);
        mFabAdd = findViewById(R.id.fabAdd);
    }

    private void createAdapterForRecyclerView() {
        mAdapter = new PhoneListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void connectRecyclerViewWithDatabase() {
        mPhoneViewModel = new ViewModelProvider(this).get(PhoneViewModel.class);
        mPhoneViewModel.getAllPhones().observe(this, phones -> mAdapter.setPhoneList(phones));
    }

    private void setupForAddingNewPhone() {
        mFabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddPhoneActivity.class);
            mActivityResultLauncher.launch(intent);
        });

        mActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::processAddingNewPhone);
    }

    private void processAddingNewPhone(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent resultData = Objects.requireNonNull(result.getData());
            insertNewPhone(resultData);
        }
    }

    private void insertNewPhone(Intent resultData) {
        String producer = resultData.getStringExtra(AddPhoneActivity.PRODUCER_KEY);
        String model = resultData.getStringExtra(AddPhoneActivity.MODEL_KEY);
        String androidVersion = resultData.getStringExtra(AddPhoneActivity.VERSION_KEY);
        String website = resultData.getStringExtra(AddPhoneActivity.WEBSITE_KEY);

        Phone phone = new Phone(producer, model);
        phone.setVersion(androidVersion.isEmpty() ? null : androidVersion);
        phone.setWebsiteUrl(website.isEmpty() ? null : website);
        mPhoneViewModel.insert(phone);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.delete_all_phones) {
            mPhoneViewModel.deleteAll();
            String message = getResources().getString(R.string.deletedAllPhonesMessage);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}