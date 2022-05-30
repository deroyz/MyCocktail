package com.example.mycocktail.viewmodel;



import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.mycocktail.data.LogDatabase;

public class AddLogViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final LogDatabase mLogDatabase;
    private final int mLogId;

    public AddLogViewModelFactory(LogDatabase mLogDatabase, int mLogId) {
        this.mLogDatabase = mLogDatabase;
        this.mLogId = mLogId;
    }

    public <T extends ViewModel> T create(Class<T> modelClass) {

        //noinspection unchecked
        return (T) new AddLogViewModel(mLogDatabase, mLogId);

    }

}
