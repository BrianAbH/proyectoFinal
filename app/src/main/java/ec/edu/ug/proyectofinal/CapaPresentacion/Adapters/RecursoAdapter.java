package ec.edu.ug.proyectofinal.CapaPresentacion.Adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import ec.edu.ug.proyectofinal.CapaDatos.Models.Foros.Foros;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Recursos;
import ec.edu.ug.proyectofinal.R;

public class RecursoAdapter extends RecyclerView.Adapter<RecursoAdapter.ViewHolder> {
    private List<Recursos.Modulo> mData;
    private String token;

    public RecursoAdapter(List<Recursos.Modulo> mData, String token) {
        this.mData = mData;
        this.token = token;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recurso, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recursos.Modulo modulo = mData.get(position);
        holder.tvNombreRecurso.setText(modulo.name);
        if (modulo.contents != null && !modulo.contents.isEmpty()) {
            Recursos.Contenido archivoFisico = modulo.contents.get(0);

            holder.tvNombreArchivo.setText(archivoFisico.filename);

            holder.itemView.setOnClickListener(v -> {
                String urlDescarga = archivoFisico.fileurl+"&token="+token;
                Intent iRecurso = new Intent(Intent.ACTION_VIEW, Uri.parse(urlDescarga));
                holder.itemView.getContext().startActivity(iRecurso);
            });
        } else {
            holder.tvNombreArchivo.setText("Archivo no disponible");
            holder.itemView.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombreArchivo,tvNombreRecurso;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreRecurso = itemView.findViewById(R.id.txtTitulo);
            tvNombreArchivo = itemView.findViewById(R.id.txtTituloPdf);
        }
    }

    public void actualizarDatos(List<Recursos.Modulo> nuevaLista) {
        this.mData.clear();
        this.mData.addAll(nuevaLista);
        notifyDataSetChanged();
    }
}