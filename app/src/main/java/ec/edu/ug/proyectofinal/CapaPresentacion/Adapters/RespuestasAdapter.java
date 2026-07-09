package ec.edu.ug.proyectofinal.CapaPresentacion.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ec.edu.ug.proyectofinal.CapaDatos.Models.Foros.ForoResultados;
import ec.edu.ug.proyectofinal.R;

public class RespuestasAdapter extends RecyclerView.Adapter<RespuestasAdapter.ViewHolder>{
    private List<ForoResultados.Discusio> listaRespuestas;

    public RespuestasAdapter(List<ForoResultados.Discusio> listaRespuestas) {
        this.listaRespuestas = listaRespuestas;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_respuestas, parent, false);

        return new RespuestasAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ForoResultados.Discusio foros = listaRespuestas.get(position);

        holder.tvNombre.setText(foros.userfullname);
        holder.tvFecha.setText(foros.getfecha());
        holder.tvMensaje.setText(foros.getCleanmessage());
    }


    @Override
    public int getItemCount() {
            return listaRespuestas.size();
        }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardRespuestas;
        TextView tvNombre, tvFecha, tvMensaje;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardRespuestas = itemView.findViewById(R.id.cardRespuestas);
            tvNombre = itemView.findViewById(R.id.tvtNombre);
            tvFecha = itemView.findViewById(R.id.tvtFecha);
            tvMensaje = itemView.findViewById(R.id.tvtMensaje);
        }
    }
}
