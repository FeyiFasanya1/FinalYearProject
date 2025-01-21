    package com.example.app.Activity;

    import android.os.Bundle;

    import androidx.appcompat.app.AppCompatActivity;

    import androidx.activity.result.ActivityResult;
    import androidx.activity.result.ActivityResultCallback;
    import androidx.activity.result.ActivityResultLauncher;
    import androidx.activity.result.contract.ActivityResultContracts;

    import com.example.app.Domain.QuantityDomain;
    import com.example.app.databinding.ActivityUploadBinding;
    import com.google.firebase.database.DataSnapshot;
    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import android.app.Activity;
    import android.app.AlertDialog;
    import android.content.Intent;
    import android.net.Uri;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Toast;
    import com.example.app.Domain.ItemsDomain;
    import com.google.android.gms.tasks.OnCompleteListener;
    import com.google.android.gms.tasks.OnFailureListener;
    import com.google.android.gms.tasks.OnSuccessListener;
    import com.google.android.gms.tasks.Task;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.storage.FirebaseStorage;
    import com.google.firebase.storage.StorageReference;
    import com.google.firebase.storage.UploadTask;

    import java.text.DateFormat;
    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.HashMap;
    import java.util.Map;

    public class UploadActivity extends AppCompatActivity {

        private ActivityUploadBinding binding;
        private String imageURL;
        private Uri uri;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityUploadBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            // Initialize ActivityResultLauncher for image selection
            ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent data = result.getData();
                                uri = data.getData();
                                binding.uploadImage.setImageURI(uri);
                            } else {
                                Toast.makeText(UploadActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );

            // Set click listener for the uploadImage ImageView
            binding.uploadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent photoPicker = new Intent(Intent.ACTION_PICK);
                    photoPicker.setType("image/*");
                    activityResultLauncher.launch(photoPicker);
                }
            });

            binding.backBtn2.setOnClickListener(v -> {
                Intent intent = new Intent(UploadActivity.this, StockActivity.class);
                startActivity(intent);
                finish();

            });

            // Set click listener for the saveButton
            binding.saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveData();
                }
            });
        }

        public void saveData() {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("Android Images")
                    .child(uri.getLastPathSegment());

            AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();

            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
                    Uri urlImage = uriTask.getResult();
                    imageURL = urlImage.toString();
                    uploadData();
                    dialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        public void uploadData() {
            String title = binding.uploadTitle.getText().toString();
            String desc = binding.uploadDesc.getText().toString();
            String price = binding.uploadPrice.getText().toString();
            String productId = binding.uploadProductId.getText().toString();
            String categoryId = binding.uploadCategoryId.getText().toString();


            int smallQty = Integer.parseInt(binding.smallQuantity.getText().toString());
            int mediumQty = Integer.parseInt(binding.mediumQuantity.getText().toString());
            int largeQty = Integer.parseInt(binding.largeQuantity.getText().toString());

            // Create the quantity map with uppercase keys
            Map<String, Integer> quantities = new HashMap<>();
            quantities.put("SMALL", smallQty);
            quantities.put("MEDIUM", mediumQty);
            quantities.put("LARGE", largeQty);


            if (title.isEmpty() || desc.isEmpty() || price.isEmpty() || productId.isEmpty() || categoryId.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            QuantityDomain quantity = new QuantityDomain(smallQty, mediumQty, largeQty);


            // Create the list for image URLs
            ArrayList<String> imageUrls = new ArrayList<>();
            imageUrls.add(imageURL);

//            // Initialize QuantityDomain (can be customized or set with default values)
//            QuantityDomain defaultQuantity = new QuantityDomain(); // Set this with actual quantity logic if needed

            // Create the ItemsDomain object
            ItemsDomain item = new ItemsDomain(
                    title,
                    desc,
                    imageUrls,
                    Double.parseDouble(price),
                    0.0,
                    0,
                    0.0,
                    0,
                    categoryId,
                    quantity,
                    productId,
                    null
            );

            // Reference to the Items node in Firebase
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            database.getReference("Items").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        DataSnapshot snapshot = task.getResult();

                        // Determine the next key
                        long nextKey = snapshot.exists() ? snapshot.getChildrenCount() + 1 : 1;

                        // Save the item in Firebase with the next key
                        database.getReference("Items").child(String.valueOf(nextKey))
                                .setValue(item)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(UploadActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(UploadActivity.this, "Failed to fetch item count", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
