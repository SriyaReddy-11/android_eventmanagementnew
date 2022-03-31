package com.eventmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eventmanagement.Entities.CustomerEnitity;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class CustomerLoginActivity extends AppCompatActivity {

    TextView registerButton,tvforgot;
    EditText email, password;
    Button login;
    Button orgLogin;
    SignInButton gmail_btn;
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;
    public static final String TAG = "GoogleSignIn";
    public static final int RC_SIGN_IN = 321;
    private SignInButton btnSignInWithGoogle;
    private GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);
        gmail_btn=findViewById(R.id.gmail_btn);
       mAuth = FirebaseAuth.getInstance();
        registerButton = findViewById(R.id.registerButton);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);

        tvforgot=findViewById(R.id.tvforgot);
        tvforgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CustomerLoginActivity.this, CustomerForgotActivity.class);
                startActivity(i);
            }
        });

        login = findViewById(R.id.btn_login);
        orgLogin = findViewById(R.id.orglogin);
        orgLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CustomerLoginActivity.this, OrganiserLoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent i = new Intent(CustomerLoginActivity.this, UserHomeActivity.class);
                    SharedPreferences sp = getSharedPreferences("AA", 0);
                    SharedPreferences.Editor et = sp.edit();
                    et.putString("uname", email.getText().toString());
                    et.commit();
                    startActivity(i);
                }else{
                    Toast.makeText(CustomerLoginActivity.this,"failed to Login",Toast.LENGTH_LONG).show();
                }
            }
        });
            }
        });
        requestGoogleSignIn();
        gmail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CustomerLoginActivity.this, CustomerRegistrationActivity.class);

                startActivity(i);
            }
        });



    }

    private void requestGoogleSignIn() {
        // Configure sign-in to request the user’s basic profile like name and email
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(String idToken) {

        //getting user credentials with the help of AuthCredential method and also passing user Token Id.
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        //trying to sign in user using signInWithCredential and passing above credentials of user.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithCredential:success");
                            SharedPreferences preferences = CustomerLoginActivity.this.getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            String userName = preferences.getString("username", "");
                            String userEmail = preferences.getString("useremail", "");
                            String userImageUrl = preferences.getString("userPhoto", "");

                            FirebaseDatabase db = FirebaseDatabase.getInstance();
                            databaseReference = db.getReference("Customers");
                            Query query = databaseReference.orderByChild("email").equalTo(userEmail);

                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    boolean flag=false;
                                    for (DataSnapshot data : snapshot.getChildren()) {
                                        Toast.makeText(CustomerLoginActivity.this, "exists", Toast.LENGTH_SHORT).show();
                                        Log.d("asdf", "easd");
                                        CustomerEnitity customerEnitity = snapshot.getValue(CustomerEnitity.class);

                                        flag = true;
                                    }

                                    if(!flag){
                                        CustomerEnitity customerEnitity = new CustomerEnitity(userName, userEmail, String.valueOf(System.currentTimeMillis() % 1000), "", "Male");

                                        FirebaseDatabase.getInstance().getReference("Customers").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(customerEnitity).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(CustomerLoginActivity.this, "User Registered Successfull", Toast.LENGTH_LONG).show();
                                                    finish();
                                                } else {
                                                    Toast.makeText(CustomerLoginActivity.this, "Failed to register", Toast.LENGTH_LONG).show();

                                                }
                                            }
                                        });
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });





                            // Sign in success, navigate user to Profile Activity
                            Intent intent = new Intent(CustomerLoginActivity.this, UserHomeActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(CustomerLoginActivity.this, "User authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }






    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                //authenticating user with firebase using received token id


                //assigning user information to variables
                String userName = account.getDisplayName();
                String userEmail = account.getEmail();
                String userPhoto = account.getPhotoUrl().toString();
                userPhoto = userPhoto + "?type=large";

                //create sharedPreference to store user data when user signs in successfully
                SharedPreferences.Editor editor = getApplicationContext()
                        .getSharedPreferences("MyPrefs", MODE_PRIVATE)
                        .edit();
                editor.putString("username", userName);
                editor.putString("useremail", userEmail);
                editor.putString("userPhoto", userPhoto);
                editor.apply();

                firebaseAuthWithGoogle(account.getIdToken());
                Log.i(TAG, "onActivityResult: Success");

            } catch (ApiException e) {
                Log.e(TAG, "onActivityResult: " + e.getMessage());
            }
        }
    }
}
