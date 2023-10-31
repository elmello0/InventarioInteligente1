package com.example.inventariointeligente;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.inventariointeligente.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AgregarProductoActivity extends AppCompatActivity {

    private EditText editTextNombre;
    private EditText editTextCantidad;
    private EditText editTextPrecioUnidad;
    private Button btnAgregar;
    private String cofreId;
    private ImageView imagenProducto;
    private Uri imagenUri;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);

        cofreId = getIntent().getStringExtra("COFRE_ID");

        if (cofreId == null || cofreId.isEmpty()) {
            Toast.makeText(this, "No se proporcionó el ID del cofre.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }



        editTextNombre = findViewById(R.id.editTextNombreProducto);
        editTextCantidad = findViewById(R.id.editTextCantidadProducto);
        editTextPrecioUnidad = findViewById(R.id.editTextPrecioUnidadProducto);
        btnAgregar = findViewById(R.id.botonAgregarProducto);
        imagenProducto = findViewById(R.id.imagenProducto);

        btnAgregar.setOnClickListener(v -> agregarProducto());

        Button btnSeleccionarImagen = findViewById(R.id.btnSeleccionarImagen);
        btnSeleccionarImagen.setOnClickListener(v -> seleccionarImagen());


        storageReference = FirebaseStorage.getInstance().getReference();
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

    public void seleccionarImagen() {
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
            imagenProducto.setImageURI(imagenUri);
        }
    }

    private void agregarProducto() {
        String nombre = editTextNombre.getText().toString().trim();
        String cantidadStr = editTextCantidad.getText().toString().trim();
        String precioUnidadStr = editTextPrecioUnidad.getText().toString().trim();

        if (nombre.isEmpty() || cantidadStr.isEmpty() || precioUnidadStr.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad = Integer.parseInt(cantidadStr);
        int precioUnidad = Integer.parseInt(precioUnidadStr);


        if (cantidad <= 0 || precioUnidad <= 0) {
            Toast.makeText(this, "La cantidad y el precio deben ser positivos.", Toast.LENGTH_SHORT).show();
            return;
        }


        String imageUrl = (imagenUri != null) ? imagenUri.toString() : "";

        Producto producto = new Producto(nombre, cantidad, precioUnidad, imageUrl);
        guardarProductoEnFirestore(producto);
    }

    private void guardarProductoEnFirestore(Producto producto) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("cofres").document(cofreId).collection("productos").add(producto)
                .addOnSuccessListener(documentReference -> {
                    Log.d("AgregarProductoActivity", "Producto agregado con éxito en el cofre: " + cofreId);
                    Toast.makeText(AgregarProductoActivity.this, "Producto agregado con éxito", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("AgregarProductoActivity", "Error al agregar el producto en el cofre: " + cofreId, e);
                    Toast.makeText(AgregarProductoActivity.this, "Error al agregar el producto", Toast.LENGTH_SHORT).show();
                });
    }


}
