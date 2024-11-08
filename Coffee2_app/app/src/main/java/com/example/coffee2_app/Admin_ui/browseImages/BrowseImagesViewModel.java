package com.example.coffee2_app.Admin_ui.browseImages;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;

public class BrowseImagesViewModel extends ViewModel {

    private final MutableLiveData<List<String>> images = new MutableLiveData<>();

    public LiveData<List<String>> getImages() {
        return images;
    }

    public void loadImages() {
        // Simulate loading images or fetch from repository
        List<String> sampleImages = List.of(
                "https://example.com/image1.jpg",
                "https://example.com/image2.jpg",
                "https://example.com/image3.jpg"
        );
        images.setValue(sampleImages);  // Replace with actual data source
    }
}

