package com.example.appcarsharing;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Registration extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword, editTextNome, editTextCognome, editTextTelefono;
    Button buttonReg;
    ImageButton buttonPhoto;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;

    DatabaseReference myRef;
    FirebaseStorage storage;
    StorageReference folderRef; //riferimento alla cartella contenente le foto profilo utente

    Uri selectedImageUri;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
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
                                    Utente user = new Utente(email, password, nome, cognome, telefono);
                                    String id = email.substring(0, email.indexOf("@"));
                                    //inserire il controllo sul dominio della email
                                    myRef.child("Utenti").child(id).setValue(user);

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
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*"); // Specifica che si desidera selezionare solo immagini
               // intent.putExtra("email", String.valueOf(editTextEmail.getText()));
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            //String id = data.getStringExtra("email"); //sarà il nome della foto

            selectedImageUri = data.getData();
            // Esegui l'upload su Firebase Storage utilizzando l'URI ottenuto
           // StorageReference imageRef = folderRef.child(id.substring(0, id.indexOf("@")));

        }
    }

}