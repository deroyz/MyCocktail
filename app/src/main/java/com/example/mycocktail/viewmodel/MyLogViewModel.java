package com.example.mycocktail.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mycocktail.data.LogDao;
import com.example.mycocktail.data.LogDatabase;
import com.example.mycocktail.data.LogEntry;

import java.util.List;

public class MyLogViewModel extends ViewModel {

    private static final String TAG = MyLogViewModel.class.getSimpleName();

    public LiveData<List<LogEntry>> logList = new MutableLiveData<List<LogEntry>>();

    public void onCreate(LogDatabase mLogDatabase) {

        logList = mLogDatabase.logDao().loadALLLogs();

    }

    public LiveData<List<LogEntry>> getLogList() {

        return logList;

    }

    public void setLogList(LiveData<List<LogEntry>> logList) {
        this.logList = logList;
    }

}
