package com.example.coffee2_app.ui.signedup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.coffee2_app.databinding.FragmentSignedUpBinding;
import com.example.coffee2_app.databinding.FragmentSignedUpBinding;

public class SignedUpFragment extends Fragment {

    private FragmentSignedUpBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SignedUpViewModel homeViewModel =
                new ViewModelProvider(this).get(SignedUpViewModel.class);

        binding = FragmentSignedUpBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}