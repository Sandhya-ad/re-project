package com.example.coffee2_app.Organizer_ui.myevents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coffee2_app.EntriesAdapter;
import com.example.coffee2_app.R;
import com.example.coffee2_app.databinding.FragmentEventEntrantsBinding;

import java.util.Arrays;
import java.util.List;

/**
 * Fragment that displays a list of entrants for an event. Uses a RecyclerView to display entrant names
 */
public class EntriesFragment extends Fragment {

    private FragmentEventEntrantsBinding binding;
    private List<String> nameList;
    private EntriesAdapter namesAdapter;

    /**
     * Inflates the layout, initializes an array and click listeners.
     *
     * @param inflater  LayoutInflater to inflate views in the fragment
     * @param container          Parent view to contain the fragment's UI
     * @param savedInstanceState Bundle containing the fragment's saved state
     * @return The root view of the fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEventEntrantsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Sample data for the list
        List<String> nameList = Arrays.asList("Sridhar", "Igor", "Sandhya", "Liqi", "David", "Ethan");

        // Set up RecyclerView
        binding.entrantList.setLayoutManager(new LinearLayoutManager(getContext()));
        //nameList = new ArrayList<>();
        namesAdapter = new EntriesAdapter(nameList, this);
        binding.entrantList.setAdapter(namesAdapter);

        // Find the Back Button from the layout
        ImageButton backButton = root.findViewById(R.id.back_button);

        // Handle Back Button click
        backButton.setOnClickListener(v -> getActivity().onBackPressed()); // Navigate back when clicked

        return root;
    }

    /**
     * Clears the binding reference when the view is destroyed to prevent memory leaks.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
