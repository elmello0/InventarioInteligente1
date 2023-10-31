package com.example.inventariointeligente;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;

import com.example.inventariointeligente.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ADD_COFRE = 1;

    private ArrayList<Cofre> cofres = new ArrayList<>();
    private CofreAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewCofres);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CofreAdapter(cofres);
        recyclerView.setAdapter(adapter);

        cargarCofresDesdeFirestore();
    }

    private void cargarCofresDesdeFirestore() {
        db.collection("cofres").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                cofres.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Cofre cofre = document.toObject(Cofre.class);
                    cofres.add(cofre);
                }
                adapter.notifyDataSetChanged();
            } else {

            }
        });
    }

    public void agregarProducto(View view) {
        Intent intent = new Intent(this, AgregarCofreActivity.class);
        startActivityForResult(intent, REQUEST_ADD_COFRE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADD_COFRE && resultCode == RESULT_OK) {

            cargarCofresDesdeFirestore();
        }
    }
}
