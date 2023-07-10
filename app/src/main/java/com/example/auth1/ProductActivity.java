package com.example.auth1;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
public class ProductActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText mProductName;
    private EditText mProductDescription;
    private EditText mContactInfo;
    private ImageView mProductImage;
    private Button mUploadButton;
    private Spinner mCurrencySpinner; // Added Spinner for currency selection

    private Uri mImageUri;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;

    private boolean isUploading = false; // Flag to track if a file is being uploaded

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Enable the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("products");
        mStorageRef = FirebaseStorage.getInstance().getReference().child("product_images");

        mProductName = findViewById(R.id.product_name);
        mProductDescription = findViewById(R.id.product_description);
        mContactInfo = findViewById(R.id.contact_info);
        mProductImage = findViewById(R.id.product_image);
        mUploadButton = findViewById(R.id.upload_button);
        mCurrencySpinner = findViewById(R.id.currencySpinner); // Initialize the currency spinner

        // Populate the currency spinner with options
        String[] currencyOptions = { "USD", "KGS", "EUR", "JPY", "GBP", "CAD" };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencyOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCurrencySpinner.setAdapter(adapter);

        mProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isUploading) { // Check if a file is currently being uploaded
                    uploadFile();
                }
            }
        });
    }

    // Handle the back button click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            isUploading = true; // Set the flag to true when starting the upload
            mUploadButton.setEnabled(false); // Disable the upload button

            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get the URL of the uploaded file
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String productName = mProductName.getText().toString().trim();
                                    String productDescription = mProductDescription.getText().toString().trim();
                                    String contactInfo = mContactInfo.getText().toString().trim();
                                    String imageUrl = uri.toString();
                                    String selectedCurrency = mCurrencySpinner.getSelectedItem().toString(); // Retrieve the selected currency

                                    String productId = mDatabaseRef.push().getKey();
                                    Product product = new Product(productId, productName, productDescription, contactInfo, imageUrl, mAuth.getCurrentUser().getUid(), selectedCurrency); // Pass the selected currency to the Product constructor
                                    mDatabaseRef.child(productId).setValue(product)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(ProductActivity.this, "Product uploaded successfully", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    } else {
                                                        Toast.makeText(ProductActivity.this, "Failed to upload product", Toast.LENGTH_SHORT).show();
                                                    }
                                                    isUploading = false; // Set the flag to false after the upload is complete
                                                    mUploadButton.setEnabled(true); // Enable the upload button
                                                }
                                            });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            isUploading = false; // Set the flag to false if the upload fails
                            mUploadButton.setEnabled(true); // Enable the upload button
                        }
                    });
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(mProductImage);
        }
    }
}

