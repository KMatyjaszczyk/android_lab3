package com.example.lab3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdatePhoneActivity extends AppCompatActivity {
    private EditText mEditTextProducer;
    private EditText mEditTextModel;
    private EditText mEditTextVersion;
    private EditText mEditTextWebsite;
    private Button mButtonWebsite;
    private Button mButtonCancel;
    private Button mButtonUpdate;
    private Long phoneId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone);

        connectLayoutElementsWithFields();
        receiveDataFromMainActivity();
        addListenersToLayoutElements();
    }

    private void connectLayoutElementsWithFields() {
        mEditTextProducer = findViewById(R.id.editTextUpdatePhoneProducer);
        mEditTextModel = findViewById(R.id.editTextUpdatePhoneModel);
        mEditTextVersion = findViewById(R.id.editTextUpdatePhoneVersion);
        mEditTextWebsite = findViewById(R.id.editTextUpdatePhoneWebsite);
        mButtonWebsite = findViewById(R.id.buttonUpdatePhoneWebsite);
        mButtonCancel = findViewById(R.id.buttonUpdatePhoneCancel);
        mButtonUpdate = findViewById(R.id.buttonUpdatePhoneUpdate);
    }

    private void receiveDataFromMainActivity() {
        Bundle bundleFromMainActivity = getIntent().getExtras();
        phoneId = bundleFromMainActivity.getLong(MainActivity.ID_KEY);
        String producer = bundleFromMainActivity.getString(MainActivity.PRODUCER_KEY);
        String model = bundleFromMainActivity.getString(MainActivity.MODEL_KEY);
        String version = bundleFromMainActivity.getString(MainActivity.VERSION_KEY);
        String websiteUrl = bundleFromMainActivity.getString(MainActivity.WEBSITE_KEY);
        mEditTextProducer.setText(producer);
        mEditTextModel.setText(model);
        mEditTextVersion.setText(version);
        mEditTextWebsite.setText(websiteUrl);
    }

    private void addListenersToLayoutElements() {
        addNotNullListenerToTextField(mEditTextProducer);
        addNotNullListenerToTextField(mEditTextModel);
        mButtonWebsite.setOnClickListener(this::goToWebsite);
        mButtonCancel.setOnClickListener(this::abandonUpdatingPhone);
        mButtonUpdate.setOnClickListener(this::prepareForUpdatingPhone);
    }

    private void addNotNullListenerToTextField(EditText textField) {
        textField.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus && !isTextFieldNotEmpty(textField)) {
                String errorText = getResources().getString(R.string.textIsEmptyAnnouncement);
                textField.setError(errorText);
                Toast.makeText(UpdatePhoneActivity.this, errorText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isTextFieldNotEmpty(EditText textField) {
        return !TextUtils.isEmpty(textField.getText().toString());
    }

    private void goToWebsite(View view) {
        Uri uri = Uri.parse(mEditTextWebsite.getText().toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(UpdatePhoneActivity.this, getResources().getText(R.string.cannotGoToWebsiteMessage), Toast.LENGTH_SHORT).show();
        }
    }

    private void abandonUpdatingPhone(View view) {
        Intent intent = new Intent(UpdatePhoneActivity.this, MainActivity.class);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    private void prepareForUpdatingPhone(View view) {
        boolean phoneCanBeSaved = isTextFieldNotEmpty(mEditTextProducer)
                && isTextFieldNotEmpty(mEditTextModel);

        if (!phoneCanBeSaved) {
            Toast.makeText(UpdatePhoneActivity.this, getResources().getText(R.string.cannotUpdatePhoneMessage), Toast.LENGTH_LONG).show();
            return;
        }

        Intent intentWithPhoneData = preparePhoneData();
        setResult(RESULT_OK, intentWithPhoneData);
        finish();
    }

    @NonNull
    private Intent preparePhoneData() {
        Intent intent = new Intent(UpdatePhoneActivity.this, MainActivity.class);
        intent.putExtra(MainActivity.ID_KEY, phoneId);
        intent.putExtra(MainActivity.PRODUCER_KEY, mEditTextProducer.getText().toString());
        intent.putExtra(MainActivity.MODEL_KEY, mEditTextModel.getText().toString());
        intent.putExtra(MainActivity.VERSION_KEY, mEditTextVersion.getText().toString());
        intent.putExtra(MainActivity.WEBSITE_KEY, mEditTextWebsite.getText().toString());
        return intent;
    }
}