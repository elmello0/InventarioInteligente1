package com.example.inventariointeligente;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.inventariointeligente.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class AgregarCofreActivity extends AppCompatActivity {
    private EditText nombreProducto;
    private ImageView imagenCofre;
    private String nombre;
    private Uri imagenUri;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_cofre);

        nombreProducto = findViewById(R.id.e_nombreitem);
        imagenCofre = findViewById(R.id.cofre_imagen);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void agregarCofre(View view) {
        nombre = nombreProducto.getText().toString();
        if(imagenUri == null) {

            Toast.makeText(this, "Por favor, selecciona una imagen", Toast.LENGTH_SHORT).show();
            return;
        }
        Cofre nuevoCofre = new Cofre(null, nombre, imagenUri.toString());
        guardarCofreEnFirestore(nuevoCofre);
    }

    private void guardarCofreEnFirestore(Cofre cofre) {
        db.collection("cofres").add(cofre).addOnSuccessListener(documentReference -> {
            String docId = documentReference.getId();
            cofre.setId(docId);


            db.collection("cofres").document(docId).update("id", docId)
                    .addOnSuccessListener(aVoid -> {
                        Intent respuestaIntent = new Intent();
                        respuestaIntent.putExtra("COFRE_NOMBRE", cofre.getNombre());
                        respuestaIntent.putExtra("COFRE_IMAGEN_URL", cofre.getImagenUrl());
                        setResult(RESULT_OK, respuestaIntent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al actualizar el ID del cofre.", Toast.LENGTH_SHORT).show();
                    });

        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error al guardar el cofre.", Toast.LENGTH_SHORT).show();
        });
    }

    public void seleccionarImagen(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imagenUri = data.getData();
            imagenCofre.setImageURI(imagenUri);
        }
    }
}
