package com.example.coffee2_app.Organizer_ui.myevents;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.coffee2_app.R;
import com.example.coffee2_app.databinding.FragmentMyEventsBinding;

public class MyEventsFragment extends Fragment {

    private FragmentMyEventsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_events, container, false);

        // Find the Back Button from the layout
        ImageButton backButton = root.findViewById(R.id.back_button);

        // Handle Back Button click
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the back button action
                getActivity().onBackPressed();  // Navigate back when clicked
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
