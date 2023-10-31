package com.example.inventariointeligente;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Pair;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.inventariointeligente.R;

import java.util.ArrayList;
import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> implements View.OnCreateContextMenuListener {

    private List<Pair<String, Producto>> productosList;
    private List<Boolean> selectedItems;
    private int itemPosition;

    public ProductoAdapter(List<Pair<String, Producto>> productos) {
        this.productosList = productos;
        this.selectedItems = new ArrayList<>();
        for (int i = 0; i < productosList.size(); i++) {
            selectedItems.add(false);
        }
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.producto_item, parent, false);
        return new ProductoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        Pair<String, Producto> item = productosList.get(position);
        Producto producto = item.second;

        holder.textViewNombre.setText(producto.getNombre());
        holder.textViewCantidad.setText("Cantidad: " + producto.getCantidad());
        holder.textViewPrecioUnidad.setText("Precio unidad: $" + producto.getPrecioUnidad());


        Glide.with(holder.itemView.getContext())
                .load(producto.getImagenUrl())
                .into(holder.imageViewProducto);

        holder.itemView.setSelected(selectedItems.get(position));
        holder.itemView.setOnLongClickListener(v -> {
            itemPosition = position;
            return false;
        });
        holder.itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public int getItemCount() {
        return productosList.size();
    }

    public void updateData(List<Pair<String, Producto>> nuevosProductos) {
        productosList.clear();
        productosList.addAll(nuevosProductos);
        selectedItems.clear();
        for (int i = 0; i < productosList.size(); i++) {
            selectedItems.add(false);
        }
        notifyDataSetChanged();
    }

    public Pair<String, Producto> getProductoAtPosition() {
        return productosList.get(itemPosition);
    }

    public List<Pair<String, Producto>> getProductosSeleccionados() {
        List<Pair<String, Producto>> productosSeleccionados = new ArrayList<>();
        for (int i = 0; i < productosList.size(); i++) {
            if (selectedItems.get(i)) {
                productosSeleccionados.add(productosList.get(i));
            }
        }
        return productosSeleccionados;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Opciones");
        menu.add(Menu.NONE, R.id.menu_editar, 1, "Editar");
        menu.add(Menu.NONE, R.id.menu_eliminar, 2, "Eliminar");
    }

    class ProductoViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNombre;
        ImageView imageViewProducto;
        TextView textViewCantidad;
        TextView textViewPrecioUnidad;

        ProductoViewHolder(View view) {
            super(view);
            textViewNombre = view.findViewById(R.id.producto_nombre);
            imageViewProducto = view.findViewById(R.id.producto_imagen);
            textViewCantidad = view.findViewById(R.id.producto_cantidad);
            textViewPrecioUnidad = view.findViewById(R.id.producto_precioUnidad);

            view.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    selectedItems.set(position, !selectedItems.get(position));
                    notifyItemChanged(position);
                }
            });
        }
    }
}
