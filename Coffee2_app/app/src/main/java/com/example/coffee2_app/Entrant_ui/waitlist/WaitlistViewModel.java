package com.example.coffee2_app.Entrant_ui.waitlist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WaitlistViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public WaitlistViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Waitlist fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}