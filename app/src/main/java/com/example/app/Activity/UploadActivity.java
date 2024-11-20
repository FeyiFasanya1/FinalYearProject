    package com.example.app.Activity;

    import android.os.Bundle;

    import androidx.appcompat.app.AppCompatActivity;

    import androidx.activity.result.ActivityResult;
    import androidx.activity.result.ActivityResultCallback;
    import androidx.activity.result.ActivityResultLauncher;
    import androidx.activity.result.contract.ActivityResultContracts;

    import com.example.app.Domain.QuantityDomain;
    import com.example.app.databinding.ActivityUploadBinding;
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
                    while (!uriTask.isComplete());
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
            String itemSize = binding.itemSizeValue.getText().toString();



            // Create the list for image URLs
            ArrayList<String> imageUrls = new ArrayList<>();
            imageUrls.add(imageURL);

            // Initialize QuantityDomain (can be customized or set with default values)
            QuantityDomain defaultQuantity = new QuantityDomain(); // Set this with actual quantity logic if needed

            // Create the ItemsDomain object
            ItemsDomain itemsDomain = new ItemsDomain(
                    title,
                    desc,
                    imageUrls,  // Pass the image URL as part of the list
                    Double.parseDouble(price),  // Convert price to double
                    0.0,  // Old price
                    0,  // Review
                    0.0,  // Rating
                    0,  // Number in cart default
                    categoryId,
                    defaultQuantity,  // Can initialize this with actual values if needed
                    productId,
                    itemSize
            );

            String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

            FirebaseDatabase.getInstance().getReference("Items").child(currentDate)
                    .setValue(itemsDomain)
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
        }
    }
