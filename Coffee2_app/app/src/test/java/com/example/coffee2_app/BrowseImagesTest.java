package com.example.coffee2_app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import androidx.lifecycle.ViewModelProvider;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.example.coffee2_app.Admin_ui.browseImages.BrowseImagesAdapter;
import com.example.coffee2_app.Admin_ui.browseImages.BrowseImagesFragment;
import com.example.coffee2_app.Admin_ui.browseImages.BrowseImagesViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class BrowseImagesTest {

    private BrowseImagesAdapter adapter;
    private List<Image> images;
    private BrowseImagesViewModel viewModel;
    private BrowseImagesFragment fragment;

    @Rule
    public ActivityTestRule<AdminHomeActivity> activityRule = new ActivityTestRule<>(AdminHomeActivity.class);

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();

        // Set up for BrowseImagesAdapter
        images = new ArrayList<>();
        adapter = new BrowseImagesAdapter(context, images);

        // Set up for BrowseImagesFragment
        fragment = new BrowseImagesFragment();

        // Set up for BrowseImagesViewModel
        viewModel = new ViewModelProvider(activityRule.getActivity()).get(BrowseImagesViewModel.class);
    }

    // Test for BrowseImagesAdapter
    @Test
    public void testAdapterNotNull() {
        assertNotNull("Adapter should not be null", adapter);
    }

    @Test
    public void testAdapterItemCount() {
        assertEquals("Item count should be zero initially", 0, adapter.getItemCount());
        images.add(new Image("https://example.com/image.jpg"));
        adapter.notifyDataSetChanged();
        assertEquals("Item count should be one after adding an item", 1, adapter.getItemCount());
    }

    // Test for BrowseImagesFragment
    @Test
    public void testFragmentNotNull() {
        assertNotNull("Fragment should not be null", fragment);
    }

    // Test for BrowseImagesViewModel
    @Test
    public void testViewModelNotNull() {
        assertNotNull("ViewModel should not be null", viewModel);
    }
}
