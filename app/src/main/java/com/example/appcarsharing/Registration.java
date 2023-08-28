package com.example.appcarsharing;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Registration extends AppCompatActivity {

    EditText editTextEmail, editTextPassword, editTextNome, editTextCognome, editTextTelefono;
    Button buttonReg;
    ImageButton buttonPhoto;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;

    DatabaseReference myRef;
    FirebaseStorage storage;
    StorageReference folderRef;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    Uri selectedImageUri;
    private static final int PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 1;
    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        myRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        storage = FirebaseStorage.getInstance();
        folderRef = storage.getReference().child("fotoProfilo");

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        editTextNome = findViewById(R.id.nome);
        editTextCognome = findViewById(R.id.cognome);
        editTextTelefono = findViewById(R.id.telefono);
        buttonReg = findViewById(R.id.btn_register);
        buttonPhoto = findViewById(R.id.fotoProfilo);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email = String.valueOf(editTextEmail.getText());
                String password = String.valueOf(editTextPassword.getText());
                String nome = String.valueOf(editTextNome.getText());
                String cognome = String.valueOf(editTextCognome.getText());
                String telefono = String.valueOf(editTextTelefono.getText());

                if (TextUtils.isEmpty(nome)) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Registration.this, "Enter nome", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(cognome)) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Registration.this, "Enter cognome", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Registration.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Registration.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(telefono)) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Registration.this, "Enter telephone number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(selectedImageUri == null){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Registration.this, "Enter image profile", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(Registration.this, "Account created.",
                                            Toast.LENGTH_SHORT).show();
                                    //inserisce l'utente dentro il db di realtime
                                    Utente user = new Utente(email, hashWith256(password), nome, cognome, telefono);
                                    String id = email.substring(0, email.indexOf("@"));
                                    //inserire il controllo sul dominio della email
                                    myRef.child("Utenti").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Intent intent = new Intent(Registration.this, Login.class);
                                            startActivity(intent);
                                        }
                                    });

                                    //ora carico la foto dentro lo storage
                                    StorageReference imageRef = folderRef.child(email.substring(0, email.indexOf("@")));
                                    UploadTask uploadTask = imageRef.putFile(selectedImageUri);
                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            // Upload completato con successo
                                            Toast.makeText(Registration.this, "image uploaded successfully",
                                                    Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Gestisci l'errore durante l'upload
                                            Toast.makeText(Registration.this, "Error loading image",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } else {
                                    //registrazione fallita
                                    Toast.makeText(Registration.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        buttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              requestStoragePermission();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
        }
    }

    public String hashWith256(String textToHash){
        String encoded = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] byteOfTextToHash = textToHash.getBytes(StandardCharsets.UTF_8);
            byte[] hashedByetArray = digest.digest(byteOfTextToHash);
            encoded = Base64.getEncoder().encodeToString(hashedByetArray);
        }catch (NoSuchAlgorithmException exception){
            System.out.println("L'algoritmo specifica per l'hashing non è valido");
            return textToHash;
        }
        return encoded;
    }

    //autorizzazione per l'accesso ai dati multimediali
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(Registration.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Registration.this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            // L'autorizzazione è già stata concessa
            openImagePicker();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // L'autorizzazione è stata concessa
                openImagePicker();
            } else {
                // L'autorizzazione è stata negata
                if (isPermissionPermanentlyDenied(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // L'utente ha selezionato "Non chiedere mai più" per l'autorizzazione
                    showPermissionDeniedDialog();
                }
            }
        }
    }

    private boolean isPermissionPermanentlyDenied(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!shouldShowRequestPermissionRationale(permission)) {
                return ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }


    private void showPermissionDeniedDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Autorizzazione richiesta");
        builder.setMessage("Per utilizzare questa funzionalità, devi concedere l'autorizzazione per accedere ai file. Vuoi aprire le impostazioni dell'app per abilitare l'autorizzazione?");
        builder.setPositiveButton("Apri impostazioni", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Annulla", null);

        AlertDialog dialog = builder.show();
        dialog.show();
    }


    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }


}