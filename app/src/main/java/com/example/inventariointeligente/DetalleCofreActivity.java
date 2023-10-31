package com.example.inventariointeligente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import com.example.inventariointeligente.R;


import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DetalleCofreActivity extends AppCompatActivity {
    private String cofreId;
    private RecyclerView recyclerViewProductos;
    private ProductoAdapter productoAdapter;
    private Button btnAgregarProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cofre);

        cofreId = getIntent().getStringExtra("COFRE_ID");

        recyclerViewProductos = findViewById(R.id.recyclerViewProductos);
        btnAgregarProductos = findViewById(R.id.btnagregarproductos);

        productoAdapter = new ProductoAdapter(new ArrayList<Pair<String, Producto>>());

        recyclerViewProductos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProductos.setAdapter(productoAdapter);


        registerForContextMenu(recyclerViewProductos);

        btnAgregarProductos.setOnClickListener(v -> {
            Intent intentAgregar = new Intent(DetalleCofreActivity.this, AgregarProductoActivity.class);
            intentAgregar.putExtra("COFRE_ID", cofreId);
            startActivityForResult(intentAgregar, 1);
        });

        if (cofreId != null && !cofreId.isEmpty()) {
            obtenerProductosPorCofreId(cofreId);
        } else {
            Toast.makeText(this, "Error al obtener el ID del cofre.", Toast.LENGTH_SHORT).show();
            finish();
        }
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

    private void obtenerProductosPorCofreId(String id) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("cofres").document(id)
                .collection("productos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<Pair<String, Producto>> listaProductos = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Producto producto = document.toObject(Producto.class);
                            listaProductos.add(new Pair<>(document.getId(), producto));
                        }
                        productoAdapter.updateData(listaProductos);
                    } else {
                        Toast.makeText(this, "Error al cargar los productos del cofre.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void eliminarProducto(String productoId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String cofreId = getIntent().getStringExtra("COFRE_ID");
        db.collection("cofres").document(cofreId).collection("productos").document(productoId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(DetalleCofreActivity.this, "Producto eliminado exitosamente", Toast.LENGTH_SHORT).show();
                    // Refrescar la lista de productos después de la eliminación
                    obtenerProductosPorCofreId(cofreId);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(DetalleCofreActivity.this, "Error al eliminar el producto", Toast.LENGTH_SHORT).show();
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1 || requestCode == 2) { // Manejar ambos códigos de solicitud
                String cofreId = getIntent().getStringExtra("COFRE_ID");
                if (cofreId != null && !cofreId.isEmpty()) {
                    obtenerProductosPorCofreId(cofreId);
                }
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.producto_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Pair<String, Producto> productoSeleccionado = productoAdapter.getProductoAtPosition();

        int itemId = item.getItemId();
        if (itemId == R.id.menu_editar) {
            Intent intentEditar = new Intent(this, EditarProductoActivity.class);
            intentEditar.putExtra("PRODUCTO_ID", productoSeleccionado.first);
            intentEditar.putExtra("COFRE_ID", cofreId);
            intentEditar.putExtra("PRODUCTO_DATA", productoSeleccionado.second);
            startActivityForResult(intentEditar, 2); // Cambio aquí
            return true;
        } else if (itemId == R.id.menu_eliminar) {
            eliminarProducto(productoSeleccionado.first);
            return true;
        } else {
            return super.onContextItemSelected(item);
        }
    }
}
