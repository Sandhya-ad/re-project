package com.example.coffee2_app.ui.signedup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SignedUpViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SignedUpViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Signed-Up fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}