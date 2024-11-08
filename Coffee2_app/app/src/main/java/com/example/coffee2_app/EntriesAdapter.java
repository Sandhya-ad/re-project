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

/**
 * RecyclerView Adapter for displaying a list of entrant names in an event
 * Each entry includes entrant name and cancel button
 */
public class EntriesAdapter extends RecyclerView.Adapter<EntriesAdapter.EntrantViewHolder> {

    private List<String> names;
    private Fragment currentFragment;

    /**
     * Constructor for initializing the adapter
     *
     * @param names            List of names
     * @param currentFragment  Fragment where this adapter is used
     */
    public EntriesAdapter(List<String> names, Fragment currentFragment) {
        this.names = names;
        this.currentFragment = currentFragment;
    }

    /**
     * Inflates layout for each item and returns ViewHolder
     *
     * @param parent   The parent ViewGroup
     * @param viewType The view type of the new View
     * @return A new instance of EntrantViewHolder containing item layout
     */
    @NonNull
    @Override
    public EntriesAdapter.EntrantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_entrant_entry, parent, false);
        return new EntriesAdapter.EntrantViewHolder(view);
    }

    /**
     * Binds entrant name to the ViewHolder for each item
     *
     * @param holder   ViewHolder for binding
     * @param position Position of item in the adapter
     */
    @Override
    public void onBindViewHolder(@NonNull EntrantViewHolder holder, int position) {
        String name = names.get(position);
        holder.nameTextView.setText(name);
    }

    /**
     * Returns total number of items in the list
     *
     * @return The size of the name list
     */
    @Override
    public int getItemCount() {
        return names.size();
    }

    /**
     * ViewHolder class for holding references to the views for each entrant item
     */
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
