package com.example.inventariointeligente;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.inventariointeligente.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditarProductoActivity extends AppCompatActivity {

    private EditText editNombreProducto;
    private EditText editCantidadProducto;
    private EditText editPrecioUnidad;
    private Button btnGuardarCambios;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private String productoId;
    private String cofreId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_producto);

        editNombreProducto = findViewById(R.id.edit_nombre_producto);
        editCantidadProducto = findViewById(R.id.edit_cantidad_producto);
        editPrecioUnidad = findViewById(R.id.edit_precio_unidad);
        btnGuardarCambios = findViewById(R.id.btn_guardar_cambios);

        cofreId = getIntent().getStringExtra("COFRE_ID"); // Agregado
        productoId = getIntent().getStringExtra("PRODUCTO_ID");

        if (cofreId == null || cofreId.isEmpty() || productoId == null || productoId.isEmpty()) {
            Toast.makeText(this, "Error al cargar el producto", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarDatosIniciales();

        btnGuardarCambios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarCambios();
            }
        });
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

    private void cargarDatosIniciales() {
        db.collection("cofres").document(cofreId)
                .collection("productos").document(productoId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        editNombreProducto.setText(documentSnapshot.getString("nombre"));
                        Long cantidad = documentSnapshot.getLong("cantidad");
                        Long precio = documentSnapshot.getLong("precioUnidad"); // Corrección aquí
                        if (cantidad != null) {
                            editCantidadProducto.setText(String.valueOf(cantidad));
                        }
                        if (precio != null) {
                            editPrecioUnidad.setText(String.valueOf(precio));
                        }
                    } else {
                        Toast.makeText(this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                        Log.d("ERROR_FIRESTORE", "El producto con ID: " + productoId + " no se encontró en el cofre con ID: " + cofreId);
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
                    Log.e("ERROR_FIRESTORE", "Error al cargar el producto con ID: " + productoId + " del cofre con ID: " + cofreId, e);
                    finish();
                });
    }

    private void guardarCambios() {
        String nombre = editNombreProducto.getText().toString();
        String cantidadStr = editCantidadProducto.getText().toString();
        String precioStr = editPrecioUnidad.getText().toString();

        if (nombre.isEmpty() || cantidadStr.isEmpty() || precioStr.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            int precio = Integer.parseInt(precioStr);

            Map<String, Object> productoActualizado = new HashMap<>();
            productoActualizado.put("nombre", nombre);
            productoActualizado.put("cantidad", cantidad);
            productoActualizado.put("precioUnidad", precio); // Corrección aquí

            db.collection("cofres").document(cofreId)
                    .collection("productos").document(productoId)
                    .update(productoActualizado)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Cambios guardados con éxito", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK); // Agregado
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error al guardar cambios: " + e.getMessage(), Toast.LENGTH_LONG).show());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Por favor, introduce números válidos", Toast.LENGTH_SHORT).show();
        }
    }
}