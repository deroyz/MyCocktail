package com.example.mycocktail.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.mycocktail.data.LogDatabase;
import com.example.mycocktail.data.LogEntry;


public class AddLogViewModel extends ViewModel {


    private LiveData<LogEntry> log;


    public AddLogViewModel(LogDatabase logDatabase, int logId) {

        log = logDatabase.logDao().loadTaskById(logId);

    }



    public LiveData<LogEntry> getLog() {
        return log;
    }

}
