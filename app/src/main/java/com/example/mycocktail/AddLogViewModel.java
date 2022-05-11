package com.example.mycocktail;



import android.arch.lifecycle.ViewModel;

import androidx.lifecycle.LiveData;

import com.example.mycocktail.data.LogDatabase;
import com.example.mycocktail.data.LogEntry;



public class AddLogViewModel extends ViewModel {

    private LiveData<LogEntry> log;
    public AddLogViewModel(LogDatabase logDatabase, int logId){

        log = logDatabase.logDao().loadTaskById(logId);

    }

    public LiveData<LogEntry> getLog() {
        return log;
    }

}
