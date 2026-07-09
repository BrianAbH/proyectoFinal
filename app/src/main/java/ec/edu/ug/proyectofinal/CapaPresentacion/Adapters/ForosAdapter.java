package ec.edu.ug.proyectofinal.CapaPresentacion.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import ec.edu.ug.proyectofinal.CapaDatos.Models.Foros.Foros;
import ec.edu.ug.proyectofinal.CapaPresentacion.ForoActivity;
import ec.edu.ug.proyectofinal.R;
public class ForosAdapter extends RecyclerView.Adapter<ec.edu.ug.proyectofinal.CapaPresentacion.Adapters.ForosAdapter.ViewHolder>{
    private List<Foros> listaForos;
    private String profesor;

    public ForosAdapter(List<Foros> listaForos, String profesor) {
            this.listaForos = listaForos;
            this.profesor = profesor;
        }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foros, parent, false);

        return new ec.edu.ug.proyectofinal.CapaPresentacion.Adapters.ForosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Foros foros = listaForos.get(position);

        holder.txtTitulo.setText(foros.getName());
        holder.txtRespuestas.setText(String.valueOf(foros.getNumdiscussions()));
        holder.txtProfesor.setText(profesor);
        holder.txtFecha.setText(foros.getfecha());
        holder.itemView.setOnClickListener(v->{
            Context contexto = v.getContext();
            Intent iDetalle = new Intent(contexto, ForoActivity.class);
                iDetalle.putExtra("forumid",foros.getId());
                iDetalle.putExtra("titulo",foros.getName());
                iDetalle.putExtra("descripcion",foros.getCleanintro());
                iDetalle.putExtra("profesor",profesor);
                iDetalle.putExtra("fecha",foros.getfecha());
                iDetalle.putExtra("respuestas",foros.getNumdiscussions());
            contexto.startActivity(iDetalle);
        });
    }


    @Override
    public int getItemCount() {
            return listaForos.size();
        }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardForo;
        TextView txtTitulo,txtRespuestas,txtProfesor,txtFecha;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardForo = itemView.findViewById(R.id.cardForo);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtProfesor = itemView.findViewById(R.id.txtProfesor);
            txtRespuestas = itemView.findViewById(R.id.txtRespuestas);
            txtFecha = itemView.findViewById(R.id.txtFecha);
        }
    }
}
