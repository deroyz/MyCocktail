package com.example.mycocktail;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mycocktail.data.LogDatabase;
import com.example.mycocktail.data.LogEntry;
import com.example.mycocktail.viewmodel.AddLogViewModel;
import com.example.mycocktail.viewmodel.AddLogViewModelFactory;

import java.io.File;
import java.util.Date;

public class AddLogActivity extends AppCompatActivity {

    private static final String LOG_TAG = AddLogActivity.class.getSimpleName();

    private static final String LOG_MSG_PERMISSION_GRANTED = "Camera and Storage Permission Granted";
    private static final String LOG_MSG_PERMISSION_REQUESTED = "Camera and Storage Permission Requested";
    private static final String LOG_MSG_PERMISSION_REQUEST_RESULT = "onPermissionRequestsResult";

    private static final int CAMERA_STORAGE_PERMISSION_REQUEST_CODE = 100;

    private static final int DEFAULT_LOG_ID = -1;
    public static final String EXTRA_LOG_ID = "extraTaskId";
    public static final String INSTANCE_LOG_ID = "instanceTaskId";

    private int mLogId = DEFAULT_LOG_ID;

    EditText mEditTextName;
    EditText mEditTextPrice;
    EditText mEditTextComment;
    RatingBar mRatingBar;
    Button mAddButton;
    ImageButton mPhotoTakeButton;

    private LogDatabase mLogDatabase;
    private AddLogViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_log);

        initViews();

        mLogDatabase = LogDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_LOG_ID)) {
            mLogId = savedInstanceState.getInt(INSTANCE_LOG_ID, DEFAULT_LOG_ID);
        }

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra(EXTRA_LOG_ID)) {
            mAddButton.setText(R.string.update_button);
            if (mLogId == DEFAULT_LOG_ID) {
                mLogId = intent.getIntExtra(EXTRA_LOG_ID, DEFAULT_LOG_ID);
                AddLogViewModelFactory factory = new AddLogViewModelFactory(mLogDatabase, mLogId);
                mViewModel = new ViewModelProvider(
                        this, (ViewModelProvider.Factory) factory).get(AddLogViewModel.class);
                mViewModel.getLog().observe(this, new Observer<LogEntry>() {
                    @Override
                    public void onChanged(LogEntry logEntry) {
                        mViewModel.getLog().removeObserver(this);
                        populateUI(logEntry);
                    }
                });
            }
        }
    }

    private void initViews() {

        mEditTextName = findViewById(R.id.et_cocktail_name);
        mEditTextComment = findViewById(R.id.et_comment);
        mEditTextPrice = findViewById(R.id.et_price);
        mRatingBar = findViewById(R.id.rb_cocktail);

        mAddButton = findViewById(R.id.btn_add);
        mAddButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onAddButtonClicked();
            }
        });

        mPhotoTakeButton = findViewById(R.id.img_btn_cocktail);
        mPhotoTakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPhotoTakeButtonClicked();
            }
        });

    }

    private void onAddButtonClicked() {

        String name = mEditTextName.getText().toString();
        Date date = new Date();
        String comment = mEditTextComment.getText().toString();
        float rating = 0;
        double price = Double.parseDouble(mEditTextPrice.getText().toString());

        final LogEntry log = new LogEntry(name, comment, price, rating, date);

        Log.e(LOG_TAG, "Display Log Cocktail Name: " + log.getName());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {

            @Override
            public void run() {
                if (mLogId == DEFAULT_LOG_ID) {
                    mLogDatabase.logDao().insertLog(log);
                } else {
                    log.setId(mLogId);
                    mLogDatabase.logDao().updateLog(log);
                }
                finish();
            }
        });

    }

    public void onPhotoTakeButtonClicked() {

        checkCameraPermission();

    }

    private void checkCameraPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                Log.e(LOG_TAG, LOG_MSG_PERMISSION_GRANTED);

            } else {

                Log.e(LOG_TAG, LOG_MSG_PERMISSION_REQUESTED);

                ActivityCompat.requestPermissions(AddLogActivity.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        CAMERA_STORAGE_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.e(LOG_TAG, LOG_MSG_PERMISSION_REQUEST_RESULT);

        if (requestCode == CAMERA_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                Log.e(LOG_TAG, "Permission: " + permissions[0] + " was " + grantResults[0]);
                Log.e(LOG_TAG, "Permission: " + permissions[1] + " was " + grantResults[1]);

            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("위치 권한이 꺼져있습니다.");
                builder.setMessage("[권한] 설정에서 위치 권한을 허용해야 합니다.");

                builder.setPositiveButton("설정으로 가기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        })

                        .setNegativeButton("종료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }

    private void populateUI(LogEntry log) {
        if (log == null) {
            return;
        }
    }


}
