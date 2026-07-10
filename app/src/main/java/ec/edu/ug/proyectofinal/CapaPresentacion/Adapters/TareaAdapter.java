package ec.edu.ug.proyectofinal.CapaPresentacion.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ec.edu.ug.proyectofinal.CapaDatos.Models.Tareas.Tarea;
import ec.edu.ug.proyectofinal.CapaPresentacion.EnviarTareaActivity;
import ec.edu.ug.proyectofinal.R;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.ViewHolder> {

    private final List<Tarea> lista;

    public TareaAdapter(List<Tarea> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarea, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tarea tarea = lista.get(position);

        holder.tvTitulo.setText(tarea.getName());

        String descripcion = tarea.getDescription();

        if (descripcion == null || descripcion.trim().isEmpty()) {
            holder.tvDescripcion.setVisibility(View.GONE);
        } else {
            holder.tvDescripcion.setVisibility(View.VISIBLE);
            holder.tvDescripcion.setText(Html.fromHtml(descripcion, Html.FROM_HTML_MODE_LEGACY));
        }

        holder.tvFecha.setText("Entrega: " + formatearFecha(tarea.getDuedate()));

        holder.btnEntregar.setOnClickListener(v -> {
            Context context = v.getContext();

            Intent intent = new Intent(context, EnviarTareaActivity.class);

            intent.putExtra("assignmentId", tarea.getId());

            intent.putExtra("assignmentName", tarea.getName());

            intent.putExtra("allowOnlineText", tarea.isAllowOnlineText());

            intent.putExtra("allowFileSubmission", tarea.isAllowFileSubmission());

            context.startActivity(intent);
        });
    }

    private String formatearFecha(long timestamp) {
        if (timestamp <= 0) {
            return "Sin fecha límite";
        }

        Date fecha = new Date(timestamp * 1000L);

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        return formato.format(fecha);
    }

    @Override
    public int getItemCount() {
        return lista != null ? lista.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitulo;
        TextView tvDescripcion;
        TextView tvFecha;
        MaterialButton btnEntregar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitulo = itemView.findViewById(R.id.tvTituloTarea);

            tvDescripcion = itemView.findViewById(R.id.tvDescripcionTarea);

            tvFecha = itemView.findViewById(R.id.tvFechaTarea);

            btnEntregar = itemView.findViewById(R.id.btnEntregar);
        }
    }
}