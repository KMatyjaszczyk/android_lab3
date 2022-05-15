package com.example.lab3;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
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
    public static final String ID_KEY = "id";
    public static final String PRODUCER_KEY = "producer";
    public static final String MODEL_KEY = "model";
    public static final String VERSION_KEY = "version";
    public static final String WEBSITE_KEY = "website";

    private PhoneViewModel mPhoneViewModel;
    private PhoneListAdapter mPhonesAdapter;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFabAdd;
    private ActivityResultLauncher<Intent> mAddPhoneActivityResultLauncher;
    private ActivityResultLauncher<Intent> mUpdatePhoneActivityResultLauncher;
    private PhoneListAdapter.RecyclerViewOnClickListener mRecyclerViewOnClickListener;
    private ItemTouchHelper.SimpleCallback mItemTouchHelperCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connectLayoutElementsWithFields();
        setupRemovingItems();
        createAdapterForRecyclerView();
        connectRecyclerViewWithDatabase();
        setupForAddingNewPhone();
    }

    private void connectLayoutElementsWithFields() {
        mRecyclerView = findViewById(R.id.phonesRecyclerView);
        mFabAdd = findViewById(R.id.fabAdd);
    }

    private void setupRemovingItems() {
        int noDraggingValue = 0;

        mItemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(noDraggingValue, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int phoneIndex = viewHolder.getAdapterPosition();
                Phone phone = mPhonesAdapter.getPhoneList().get(phoneIndex);
                mPhoneViewModel.delete(phone);
                mPhonesAdapter.notifyItemRemoved(phoneIndex);
                Toast.makeText(MainActivity.this, getResources().getText(R.string.phoneDeletedMessage), Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void createAdapterForRecyclerView() {
        setupForUpdatingPhone();
        mPhonesAdapter = new PhoneListAdapter(this, mRecyclerViewOnClickListener);
        new ItemTouchHelper(mItemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mPhonesAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupForUpdatingPhone() {
        mRecyclerViewOnClickListener = (view, position) -> {
            Intent intent = new Intent(MainActivity.this, UpdatePhoneActivity.class);
            Phone phone = mPhonesAdapter.getPhoneList().get(position);
            intent.putExtra(ID_KEY, phone.getId());
            intent.putExtra(PRODUCER_KEY, phone.getProducer());
            intent.putExtra(MODEL_KEY, phone.getModel());
            intent.putExtra(VERSION_KEY, phone.getVersion());
            intent.putExtra(WEBSITE_KEY, phone.getWebsiteUrl());
            mUpdatePhoneActivityResultLauncher.launch(intent);
        };

        mUpdatePhoneActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::comeBackFromUpdatingPhone);
    }

    private void comeBackFromUpdatingPhone(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            processUpdatingNewPhone(result);
            return;
        }

        if (result.getResultCode() == RESULT_CANCELED) {
            Toast.makeText(MainActivity.this, getResources().getText(R.string.cancelledMessage), Toast.LENGTH_SHORT).show();
            return;
        }

        throw new UnsupportedOperationException("Unhandled result type");
    }

    private void processUpdatingNewPhone(ActivityResult result) {
        Intent resultData = Objects.requireNonNull(result.getData());
        updatePhone(resultData);
        Toast.makeText(MainActivity.this, getResources().getText(R.string.phoneUpdatedMessage), Toast.LENGTH_SHORT).show();
    }

    private void updatePhone(Intent resultData) {
        long id = resultData.getLongExtra(ID_KEY, -1L);
        String producer = resultData.getStringExtra(PRODUCER_KEY);
        String model = resultData.getStringExtra(MODEL_KEY);
        String androidVersion = resultData.getStringExtra(VERSION_KEY);
        String website = resultData.getStringExtra(WEBSITE_KEY);

        Phone phone = new Phone(producer, model);
        phone.setId(id);
        phone.setVersion(androidVersion.isEmpty() ? null : androidVersion);
        phone.setWebsiteUrl(website.isEmpty() ? null : website);

        mPhoneViewModel.update(phone);
    }

    private void connectRecyclerViewWithDatabase() {
        mPhoneViewModel = new ViewModelProvider(this).get(PhoneViewModel.class);
        mPhoneViewModel.getAllPhones().observe(this, phones -> mPhonesAdapter.setPhoneList(phones));
    }

    private void setupForAddingNewPhone() {
        mFabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddPhoneActivity.class);
            mAddPhoneActivityResultLauncher.launch(intent);
        });

        mAddPhoneActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::comeBackFromAddingNewPhone);
    }

    private void comeBackFromAddingNewPhone(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            processAddingNewPhone(result);
            return;
        }

        if (result.getResultCode() == RESULT_CANCELED) {
            Toast.makeText(MainActivity.this, getResources().getText(R.string.cancelledMessage), Toast.LENGTH_SHORT).show();
            return;
        }

        throw new UnsupportedOperationException("Unhandled result type");
    }

    private void processAddingNewPhone(ActivityResult result) {
        Intent resultData = Objects.requireNonNull(result.getData());
        insertNewPhone(resultData);
        Toast.makeText(MainActivity.this, getResources().getText(R.string.newPhoneAddedMessage), Toast.LENGTH_SHORT).show();
    }

    private void insertNewPhone(Intent resultData) {
        String producer = resultData.getStringExtra(PRODUCER_KEY);
        String model = resultData.getStringExtra(MODEL_KEY);
        String androidVersion = resultData.getStringExtra(VERSION_KEY);
        String website = resultData.getStringExtra(WEBSITE_KEY);

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