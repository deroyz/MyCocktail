package com.example.mycocktail;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mycocktail.data.LogDatabase;
import com.example.mycocktail.data.LogEntry;
import com.example.mycocktail.viewmodel.AddLogViewModel;
import com.example.mycocktail.viewmodel.AddLogViewModelFactory;

import java.util.Date;

public class AddLogActivity extends AppCompatActivity {

    private static final String LOG_TAG = AddLogActivity.class.getSimpleName();

    private static final int DEFAULT_LOG_ID = -1;
    public static final String EXTRA_LOG_ID = "extraTaskId";
    public static final String INSTANCE_LOG_ID = "instanceTaskId";

    private int mLogId = DEFAULT_LOG_ID;

    EditText mEditTextName;
    EditText mEditTextPrice;
    EditText mEditTextComment;
    RatingBar mRatingBar;
    Button mAddButton;

    private LogDatabase mLogDatabase;
    private AddLogViewModel mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_log);

        initViews();

        mLogDatabase = LogDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_LOG_ID)) {

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
                       // populateUI(logEntry);
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
                onAddButtonCLicked();
            }
        });

    }

    private void onAddButtonCLicked() {
        
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

}
