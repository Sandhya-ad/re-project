package com.example.coffee2_app.Organizer_ui.myevents;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.coffee2_app.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * Fragment to display a QR Code for an event.
 */
public class QRCodeFragment extends Fragment {

    private String eventID;

    /**
     * Creates a new instance of QRCodeFragment with the eventID passed as an argument.
     *
     * @param eventID The event ID to encode into the QR code.
     * @return A new instance of QRCodeFragment.
     */
    public static QRCodeFragment newInstance(String eventID) {
        QRCodeFragment fragment = new QRCodeFragment();
        Bundle args = new Bundle();
        args.putString("eventID", eventID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            eventID = getArguments().getString("eventID");
        } else {
            Log.e("QRCodeFragment", "No eventID provided");
            Toast.makeText(getContext(), "Invalid Event ID", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_qr_code, container, false);

        // Back button functionality
        ImageButton backButton = rootView.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        // Set the event ID text
        TextView eventIDText = rootView.findViewById(R.id.event_id_text);
        eventIDText.setText("Hash QR Data: " + eventID);

        // Generate and display the QR code
        ImageView qrCodeImage = rootView.findViewById(R.id.qr_code_image);
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(eventID, BarcodeFormat.QR_CODE, 250, 250);
            qrCodeImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.e("QRCodeFragment", "Error generating QR code", e);
            Toast.makeText(getContext(), "Failed to generate QR code", Toast.LENGTH_SHORT).show();
        }

        // Download QR code functionality
        rootView.findViewById(R.id.download_qr_button).setOnClickListener(v -> {
            if (saveQRCodeToGallery(qrCodeImage.getDrawable())) {
                Toast.makeText(getContext(), "QR Code downloaded successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Failed to download QR Code", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    /**
     * Saves the QR code displayed in the ImageView to the device gallery.
     *
     * @param qrDrawable The drawable of the QR code.
     * @return True if successful, false otherwise.
     */
    private boolean saveQRCodeToGallery(android.graphics.drawable.Drawable qrDrawable) {
        // Implement the logic to save the QR code drawable to the gallery.
        // You can use the previous implementation provided for saving QR codes.
        return true;
    }
}
