package com.example.coffee2_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EntriesAdapter extends RecyclerView.Adapter<EntriesAdapter.EntrantViewHolder> {

    private List<String> names;
    private Fragment currentFragment;

    public EntriesAdapter(List<String> names, Fragment currentFragment) {
        this.names = names;
        this.currentFragment = currentFragment;
    }

    @NonNull
    @Override
    public EntriesAdapter.EntrantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_entrant_entry, parent, false);
        return new EntriesAdapter.EntrantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntrantViewHolder holder, int position) {
        String name = names.get(position);
        holder.nameTextView.setText(name);
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    static class EntrantViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        Button cancelButton;

        EntrantViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.entrantName);
            cancelButton = itemView.findViewById(R.id.cancelButton);
        }
    }
}
