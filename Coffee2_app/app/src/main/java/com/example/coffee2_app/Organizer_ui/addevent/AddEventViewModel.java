package com.example.coffee2_app.Organizer_ui.addevent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddEventViewModel extends ViewModel{
    private final MutableLiveData<String> mText;

    public AddEventViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the add event fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}