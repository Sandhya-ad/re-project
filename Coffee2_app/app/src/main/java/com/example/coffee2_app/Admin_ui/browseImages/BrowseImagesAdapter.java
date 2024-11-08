package com.example.coffee2_app.Admin_ui.browseImages;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.coffee2_app.Image;
import com.example.coffee2_app.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.List;

public class BrowseImagesAdapter extends RecyclerView.Adapter<BrowseImagesAdapter.ImageViewHolder> {

    private final Context context;
    private final List<Image> imageList;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();

    public BrowseImagesAdapter(Context context, List<Image> imageList) {
        this.context = context;
        this.imageList = imageList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Image image = imageList.get(position);
        Glide.with(context).load(image.getUrl()).into(holder.imageView);

        // Set up delete button click listener
        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Image")
                    .setMessage("Are you sure you want to delete this image?")
                    .setPositiveButton("Yes", (dialog, which) -> deleteImage(image.getId(), image.getUrl(), position))
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void deleteImage(String imageId, String imageUrl, int position) {
        // Delete image from Firestore
        db.collection("images").document(imageId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Optionally delete the image from Firebase Storage
                    StorageReference imageRef = storage.getReferenceFromUrl(imageUrl);
                    imageRef.delete().addOnSuccessListener(aVoid1 -> {
                        imageList.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Image removed successfully", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> Toast.makeText(context, "Failed to delete image from storage", Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to delete image", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton deleteButton;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_grid);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
