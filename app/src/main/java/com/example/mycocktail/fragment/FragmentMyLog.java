package com.example.mycocktail.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycocktail.AddLogActivity;
import com.example.mycocktail.MainActivity;
import com.example.mycocktail.R;
import com.example.mycocktail.adapter.LogAdapter;
import com.example.mycocktail.data.LogDatabase;
import com.example.mycocktail.data.LogEntry;

import java.util.List;

public class FragmentMyLog extends Fragment implements LogAdapter.ItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private View mView;
    private Context mContext;

    private RecyclerView mRecyclerView;
    private LogAdapter mAdapter;

    private LogDatabase mLogDatabase;

    public static FragmentMyLog newInstance(){

        FragmentMyLog fragmentMyLog = new FragmentMyLog();
        return fragmentMyLog;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mLogDatabase = LogDatabase.getInstance(getActivity().getApplicationContext());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_mylog, container, false);

        setupUi();

        return mView;

    }

    private void setupUi() {

        mRecyclerView = mView.findViewById(R.id.rv_logs);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mAdapter = new LogAdapter(this, mContext);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getActivity().getApplicationContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<LogEntry> logs = mAdapter.getLogs();
                        mLogDatabase.logDao().deleteLog(logs.get(position));
                    }
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
}
