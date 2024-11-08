package com.example.coffee2_app.Admin_ui.browseImages;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.coffee2_app.databinding.FragmentAdminImagesBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class BrowseImagesFragment extends Fragment {

    private FragmentAdminImagesBinding binding; // Assume view binding is set up for fragment_admin_images.xml
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private BrowseImagesAdapter imagesAdapter;
    private List<String> imagesList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminImagesBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Setup RecyclerView
        recyclerView = binding.imageGrid;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3)); // 3 columns

        imagesList = new ArrayList<>();
        imagesAdapter = new BrowseImagesAdapter(getContext(), imagesList);
        recyclerView.setAdapter(imagesAdapter);

        // Load images from Firestore
        loadImages();

        // Back button functionality
        binding.backButton.setOnClickListener(v -> getActivity().onBackPressed());

        return rootView;
    }

    private void loadImages() {
        db.collection("images").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                imagesList.clear();
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String imageUrl = document.getString("url"); // Assume "url" is the field name in Firestore
                        if (imageUrl != null) {
                            imagesList.add(imageUrl);
                        }
                    }
                    imagesAdapter.notifyDataSetChanged();
                }
            } else {
                // Handle errors, e.g., show a Toast
                Toast.makeText(getContext(), "Failed to load images", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }
}
