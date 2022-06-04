package com.example.mycocktail.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycocktail.activity.AddLogActivity;
import com.example.mycocktail.AppExecutors;
import com.example.mycocktail.R;
import com.example.mycocktail.adapter.LogAdapter;
import com.example.mycocktail.data.LogDatabase;
import com.example.mycocktail.data.LogEntry;
import com.example.mycocktail.viewmodel.MyLogViewModel;

import java.util.List;

public class MyLogFragment extends Fragment implements LogAdapter.LogItemClickListener {

    private static final String LOG_TAG = MyLogFragment.class.getSimpleName();

    private View mView;
    private Context mContext;

    private RecyclerView mRecyclerView;
    private LogAdapter mLogAdapter;

    private LogDatabase mLogDatabase;

    public static MyLogFragment newInstance() {

        MyLogFragment myLogFragment = new MyLogFragment();
        return myLogFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        Log.e(LOG_TAG, "onCreate");

        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        mLogDatabase = LogDatabase.getInstance(mContext);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.e(LOG_TAG, "onCreateView");

        mView = inflater.inflate(R.layout.fragment_mylog, container, false);
        mContext = mView.getContext();

        return mView;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.e(LOG_TAG, "onViewCreated");

        setupUi();

        setupViewModel();



    }

    private void setupUi() {

        mRecyclerView = mView.findViewById(R.id.rv_logs);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        mLogAdapter = new LogAdapter(mLogDatabase, this, mContext, getActivity());
        mRecyclerView.setAdapter(mLogAdapter);

        Log.e(LOG_TAG, "Recyclerview init in my log fragment");

        //DividerItemDecoration decoration = new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL);

        //mRecyclerView.addItemDecoration(decoration);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                AppExecutors.getInstance().diskIO().execute(() -> {
                    int position = viewHolder.getAdapterPosition();
                    List<LogEntry> logs = mLogAdapter.getLogs();
                    mLogDatabase.logDao().deleteLog(logs.get(position));
                });
            }
        }).attachToRecyclerView(mRecyclerView);

    }

    @Override
    public void onItemClickListener(int itemId) {

        Intent intent = new Intent(getActivity(), AddLogActivity.class);
        intent.putExtra(AddLogActivity.EXTRA_LOG_ID, itemId);
        startActivity(intent);

    }

    private void setupViewModel() {

        MyLogViewModel myLogViewModel = new ViewModelProvider(this).get(MyLogViewModel.class);

        Log.e(LOG_TAG, "Creating instance of myLog viewModel");

        myLogViewModel.onCreate(mLogDatabase);

        myLogViewModel.getLogList().observe(getViewLifecycleOwner(), new Observer<List<LogEntry>>() {

            @Override
            public void onChanged(@Nullable List<LogEntry> logEntries) {

                Log.e(LOG_TAG, "Updating list of tasks from LiveData in ViewModel");
                mLogAdapter.setLogs(logEntries);

            }
        });
    }
}
