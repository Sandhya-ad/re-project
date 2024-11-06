package com.example.coffee2_app.Admin_ui.browseProfiles;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BrowseProfilesViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public BrowseProfilesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Browse Profile fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
