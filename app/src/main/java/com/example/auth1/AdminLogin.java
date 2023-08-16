//package com.example.auth1;
//
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//
//public class AdminLogin extends AppCompatActivity {
//    EditText mEmail, mPassword;
//    Button mLoginBtn;
//    TextView mCreateBtn;
//    ProgressBar progressBar;
//    FirebaseAuth fAuth;
//
//    @SuppressLint("MissingInflatedId")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//
//        setContentView(R.layout.activity_login);
//        mEmail = findViewById(R.id.Email1);
//        mPassword = findViewById(R.id.Password);
//        progressBar = findViewById(R.id.progressBar);
//        fAuth = FirebaseAuth.getInstance();
//        mLoginBtn = findViewById(R.id.loginBtn);
//        mCreateBtn = findViewById(R.id.createText);
//
//        // Check if the user is already logged in
//        if (fAuth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), SellActivity.class));
//            finish();
//        }
//
//    }
//   // public void onClickRegisterFromLogin(View view) {
//     //   startActivity(new Intent(getApplicationContext(),register.class));
//   // }
//    public void onClickLogin(View view) {
//        String email = mEmail.getText().toString().trim();
//        String password = mPassword.getText().toString().trim();
//        if(!(email.equals("admin.admin@ucentralasia.org")))
//        {
//            mEmail.setError("You are not admin!!!");
//            return ;
//        }
//        if(TextUtils.isEmpty(email)){
//            mEmail.setError("Email is Required.");
//            return;
//        }
//        if(TextUtils.isEmpty(password)){
//            mPassword.setError("Password is Required.");
//            return;
//        }
//        if(password.length() < 6){
//            mPassword.setError("Password Must be >= 6 Characters");
//            return;
//        }
//        progressBar.setVisibility(View.VISIBLE);
//
//        // Now, we authenticate the user
//        fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    Toast.makeText(AdminLogin.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(getApplicationContext(), SellActivity.class));
//                } else {
//                    Toast.makeText(AdminLogin.this, "Error your are not admin ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                    progressBar.setVisibility(View.GONE);
//                }
//            }
//        });
//    }
//}