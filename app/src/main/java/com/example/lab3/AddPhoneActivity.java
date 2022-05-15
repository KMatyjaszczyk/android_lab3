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

public class AddPhoneActivity extends AppCompatActivity {
    private EditText mEditTextProducer;
    private EditText mEditTextModel;
    private EditText mEditTextVersion;
    private EditText mEditTextWebsite;
    private Button mButtonWebsite;
    private Button mButtonCancel;
    private Button mButtonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phone);

        connectLayoutElementsWithFields();
        addListenersToLayoutElements();
    }

    private void connectLayoutElementsWithFields() {
        mEditTextProducer = findViewById(R.id.editTextAddPhoneProducer);
        mEditTextModel = findViewById(R.id.editTextAddPhoneModel);
        mEditTextVersion = findViewById(R.id.editTextAddPhoneVersion);
        mEditTextWebsite = findViewById(R.id.editTextAddPhoneWebsite);
        mButtonWebsite = findViewById(R.id.buttonAddPhoneWebsite);
        mButtonCancel = findViewById(R.id.buttonAddPhoneCancel);
        mButtonSave = findViewById(R.id.buttonAddPhoneSave);
    }

    private void addListenersToLayoutElements() {
        addNotNullListenerToTextField(mEditTextProducer);
        addNotNullListenerToTextField(mEditTextModel);
        mButtonWebsite.setOnClickListener(this::goToWebsite);
        mButtonSave.setOnClickListener(this::prepareForAddingNewPhone);
        mButtonCancel.setOnClickListener(this::abandonAddingNewPhone);
    }

    private void goToWebsite(View view) {
        Uri uri = Uri.parse(mEditTextWebsite.getText().toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(intent);
        }
        catch (ActivityNotFoundException e) {
            Toast.makeText(AddPhoneActivity.this, getResources().getText(R.string.cannotGoToWebsiteMessage), Toast.LENGTH_SHORT).show();
        }
    }

    private void addNotNullListenerToTextField(EditText textField) {
        textField.setOnFocusChangeListener((view, hasFocus) -> {
            if (!hasFocus && !isTextFieldNotEmpty(textField)) {
                String errorText = getResources().getString(R.string.textIsEmptyAnnouncement);
                textField.setError(errorText);
                Toast.makeText(AddPhoneActivity.this, errorText, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isTextFieldNotEmpty(EditText textField) {
        return !TextUtils.isEmpty(textField.getText().toString());
    }

    private void prepareForAddingNewPhone(View view) {
        boolean phoneCanBeSaved = isTextFieldNotEmpty(mEditTextProducer)
                && isTextFieldNotEmpty(mEditTextModel);

        if (!phoneCanBeSaved) {
            Toast.makeText(AddPhoneActivity.this, getResources().getText(R.string.cannotSavePhoneMessage), Toast.LENGTH_LONG).show();
            return;
        }

        Intent intentWithPhoneData = preparePhoneData();
        setResult(RESULT_OK, intentWithPhoneData);
        finish();
    }

    private void abandonAddingNewPhone(View view) {
        Intent intent = new Intent(AddPhoneActivity.this, MainActivity.class);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @NonNull
    private Intent preparePhoneData() {
        Intent intent = new Intent(AddPhoneActivity.this, MainActivity.class);
        intent.putExtra(MainActivity.PRODUCER_KEY, mEditTextProducer.getText().toString());
        intent.putExtra(MainActivity.MODEL_KEY, mEditTextModel.getText().toString());
        intent.putExtra(MainActivity.VERSION_KEY, mEditTextVersion.getText().toString());
        intent.putExtra(MainActivity.WEBSITE_KEY, mEditTextWebsite.getText().toString());
        return intent;
    }
}