package com.example.app.Activity;

import android.content.Intent;
    import android.graphics.Bitmap;
    import android.net.Uri;
    import android.os.Bundle;
    import android.provider.MediaStore;
    import android.view.MotionEvent;
    import android.view.View;
    import android.widget.Button;
    import com.example.app.R;
    import android.widget.FrameLayout;
    import android.widget.ImageView;
    import android.widget.Toast;

    import androidx.appcompat.app.AppCompatActivity;

    import com.otaliastudios.zoom.ZoomLayout;

    import java.io.IOException;
    import java.util.ArrayList;

    public class DesignActivity extends AppCompatActivity {

        private static final int PICK_IMAGES_REQUEST = 1;
        private ZoomLayout zoomLayout;
        private FrameLayout canvas;
        private ArrayList<Uri> imageUris = new ArrayList<>(); // Store URIs of selected images

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_design);

            zoomLayout = findViewById(R.id.zoomLayout);
            canvas = findViewById(R.id.canvas);

            Button selectImagesButton = findViewById(R.id.selectImagesButton);
            selectImagesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Open gallery to select images
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); // Allow multiple selection
                    startActivityForResult(intent, PICK_IMAGES_REQUEST);
                }
            });
        }

        // Handle the result from image selection
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGES_REQUEST && resultCode == RESULT_OK && data != null) {
                // Handle multiple images (if supported by the device)
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imageUris.add(imageUri);
                        addImageToCanvas(imageUri); // Add each image to canvas
                    }
                } else if (data.getData() != null) {
                    // Single image selection
                    Uri imageUri = data.getData();
                    imageUris.add(imageUri);
                    addImageToCanvas(imageUri); // Add the image to canvas
                }
            }
        }

        // Add selected image to the canvas inside the ZoomLayout
        private void addImageToCanvas(Uri imageUri) {
            try {
                // Load image as Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                ImageView imageView = new ImageView(this);
                imageView.setImageBitmap(bitmap);

                // Set initial layout parameters for positioning and resizing
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(200, 200); // Initial size
                imageView.setLayoutParams(params);

                // Add the imageView to the canvas inside ZoomLayout
                canvas.addView(imageView);

                // Enable dragging and resizing for the ImageView
                enableImageDragAndResize(imageView);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            }
        }

        // Enable dragging and resizing for ImageView
        private void enableImageDragAndResize(final ImageView imageView) {
            imageView.setOnTouchListener(new View.OnTouchListener() {
                float dX, dY; // For dragging
                float initialWidth = imageView.getWidth();
                float initialHeight = imageView.getHeight();
                float initialX, initialY; // Store the initial touch position

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // Capture initial position when touch starts
                            dX = imageView.getX() - event.getRawX();
                            dY = imageView.getY() - event.getRawY();
                            initialX = event.getRawX();
                            initialY = event.getRawY();
                            break;

                        case MotionEvent.ACTION_MOVE:
                            // Drag image by adjusting its position
                            imageView.setX(event.getRawX() + dX);
                            imageView.setY(event.getRawY() + dY);

                            // Optionally implement resizing logic (dragging the corners to resize)
                            float deltaX = event.getRawX() - initialX;
                            float deltaY = event.getRawY() - initialY;
                            if (Math.abs(deltaX) > 50 || Math.abs(deltaY) > 50) {
                                // For example, resize based on the drag distance (or pinch-to-zoom for resizing)
                                imageView.getLayoutParams().width = (int) (initialWidth + deltaX);
                                imageView.getLayoutParams().height = (int) (initialHeight + deltaY);
                                imageView.requestLayout();
                            }
                            break;

                        case MotionEvent.ACTION_UP:
                            // Finalize position after dragging
                            break;
                    }
                    return true;
                }
            });
        }
    }
