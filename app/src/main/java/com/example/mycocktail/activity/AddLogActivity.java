package com.example.mycocktail.activity;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.mycocktail.AppExecutors;
import com.example.mycocktail.BuildConfig;
import com.example.mycocktail.R;
import com.example.mycocktail.data.LogDatabase;
import com.example.mycocktail.data.LogEntry;
import com.example.mycocktail.viewmodel.AddLogViewModel;
import com.example.mycocktail.viewmodel.AddLogViewModelFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddLogActivity extends AppCompatActivity {

    private static final String LOG_TAG = AddLogActivity.class.getSimpleName();

    private static final String LOG_MSG_PERMISSION_GRANTED = "Camera and Storage Permission Granted";
    private static final String LOG_MSG_PERMISSION_REQUESTED = "Camera and Storage Permission Requested";
    private static final String LOG_MSG_PERMISSION_REQUEST_RESULT = "onPermissionRequestsResult";

    private static final int CAMERA_STORAGE_PERMISSION_REQUEST_CODE = 100;

    private static final int PICK_IMAGE = 101;

    private static boolean CHECK_CAMERA_PERMISSION = false;
    private static final boolean CAMERA_PERMISSION_GRANTED = true;
    private static final boolean CAMERA_PERMISSION_DENIED = false;

    private static final int DEFAULT_LOG_ID = -1;
    public static final String EXTRA_LOG_ID = "extraTaskId";
    public static final String INSTANCE_LOG_ID = "instanceTaskId";

    final static int REQUEST_TAKE_PHOTO = 1;

    private int mLogId = DEFAULT_LOG_ID;

    EditText mEditTextName;
    EditText mEditTextPrice;
    EditText mEditTextComment;
    EditText mEditTextPlace;
    RatingBar mRatingBar;

    Button mAddButton;
    ImageView mImageViewPhoto;

    private String mCurrentPhotoPath;
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

        mImageViewPhoto = findViewById(R.id.iv_cocktail_photo);
        mImageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPhotoTakeButtonClicked();
            }
        });

        mEditTextPlace = findViewById(R.id.et_place);
        mEditTextPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddPlaceButtonClicked();
            }
        });

    }

    private void onAddPlaceButtonClicked() {

        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);

    }

    private void onAddButtonClicked() {

        String name = mEditTextName.getText().toString();
        Date date = new Date();
        String comment = mEditTextComment.getText().toString();
        float rating = mRatingBar.getRating();
        double price = Double.parseDouble(mEditTextPrice.getText().toString());
        String photoPath = mCurrentPhotoPath;


        final LogEntry log = new LogEntry(name, comment, price, rating, date, photoPath);

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

        Log.e(LOG_TAG, "onPhotoTakeButtonClicked");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cocktail Picture");
        builder.setMessage("How ");

        builder.setPositiveButton("Take Photo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e(LOG_TAG, "onPhotoTakeButtonClicked" + " /Take Photo Clicked");
                        checkCameraPermission();
                        takePhoto();
                        CHECK_CAMERA_PERMISSION = CAMERA_PERMISSION_DENIED;
                    }
                })
                .setNegativeButton("Open Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.e(LOG_TAG, "onPhotoTakeButtonClicked" + " /Choose From Gallery Clicked");
                        openGallery();

                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Log.e(LOG_TAG, "onPhotoTakeButtonClicked" + " /Cancel Clicked");
                        dialog.dismiss();

                    }
                });

        AlertDialog alert = builder.create();
        alert.show();

    }

    private void takePhoto() {

        Log.e(LOG_TAG, "takePhoto");
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String message = String.valueOf(takePhotoIntent.resolveActivity(getPackageManager()));

        Log.e(LOG_TAG, "takePhoto" + " " + message);

        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {

            Log.e(LOG_TAG, "takePhoto /if");

            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {

                Log.e(LOG_TAG, "takePhoto /photoFile not null");

                Uri photoURI =
                        FileProvider.getUriForFile(this,
                                BuildConfig.APPLICATION_ID + ".fileprovider",
                                photoFile);
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePhotoIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {

        //타임포맷
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        // 임시파일 생성
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir);

        mCurrentPhotoPath = photoFile.getAbsolutePath();

        return photoFile;
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE);
    }

    private void checkCameraPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                Log.e(LOG_TAG, LOG_MSG_PERMISSION_GRANTED);

                CHECK_CAMERA_PERMISSION = CAMERA_PERMISSION_GRANTED;

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

                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    builder.setTitle("카메라 권한이 꺼져있습니다.");
                    builder.setMessage("[권한] 설정에서 카메라 권한을 허용해야 합니다.");

                } else if (grantResults[1] == PackageManager.PERMISSION_DENIED) {
                    builder.setTitle("데이터 저장소 권한이 꺼져있습니다.");
                    builder.setMessage("[권한] 설정에서 데이터 저장소 권한을 허용해야 합니다.");

                } else {
                    builder.setTitle("카메라와 데이터 저장소 권한이 꺼져있습니다.");
                    builder.setMessage("[권한] 설정에서 카메라와 데이터 저장소 권한을 허용해야 합니다.");
                }

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

                        .setNegativeButton("돌아가기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {

                    Log.e(LOG_TAG, "Take Photo Successful");

                    File file = new File(mCurrentPhotoPath);
                    Glide.with(this)
                            .load(file)
                            .centerCrop()
                            .into(mImageViewPhoto);
                } else {
                    Log.e(LOG_TAG, "Take Photo Canceled");
                    Toast.makeText(this, "Canceled", Toast.LENGTH_LONG).show();
                } break;

            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {

                    Uri photoUri = intent.getData();

                    String message = String.valueOf(photoUri);
                    Log.e(LOG_TAG, "Take Photo Successful" + " " + message);


                    try {
                        File file = createImageFile();
                        Bitmap bitmap;
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
                        mImageViewPhoto.setImageBitmap(bitmap);
                        getPhotoPathFromGallery(file, bitmap);
                        mCurrentPhotoPath = file.getAbsolutePath();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Glide.with(this)
                            .load(mCurrentPhotoPath)
                            .centerCrop()
                            .into(mImageViewPhoto);

                } else {
                    Log.e(LOG_TAG, "Get Photo Canceled");
                } break;

            default:
                break;
        }

    }

    private void getPhotoPathFromGallery(File file, Bitmap bitmap) {

        OutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void populateUI(LogEntry log) {
        if (log == null) {
            return;
        }

        mCurrentPhotoPath = log.getPhotoPath();
        Glide.with(this)
                .load(mCurrentPhotoPath)
                .into(mImageViewPhoto);

        mEditTextName.setText(log.getName());
        mEditTextComment.setText(log.getComment());
        mEditTextPrice.setText("" + log.getPrice());
        mRatingBar.setRating(log.getRating());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(INSTANCE_LOG_ID, mLogId);
        super.onSaveInstanceState(outState);
    }
}
