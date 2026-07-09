package ec.edu.ug.proyectofinal.CapaPresentacion.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import ec.edu.ug.proyectofinal.CapaDatos.Models.Cursos.UserCourse;
import ec.edu.ug.proyectofinal.CapaPresentacion.DetalleCursoActivity;
import ec.edu.ug.proyectofinal.R;
public class ForosAdapter extends RecyclerView.Adapter<ec.edu.ug.proyectofinal.CapaPresentacion.Adapters.ForosAdapter.ViewHolder>{
    private List<String> listaCursos;

    public ForosAdapter(List<String> listaCursos) {
            this.listaCursos = listaCursos;
        }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foros, parent, false);

        return new ec.edu.ug.proyectofinal.CapaPresentacion.Adapters.ForosAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String curso = listaCursos.get(position);

        //holder.tvNombre.setText(curso.getFullname());

        /*holder.itemView.setOnClickListener(v->{
            Context contexto = v.getContext();
            Intent iDetalle = new Intent(contexto, DetalleCursoActivity.class);
            iDetalle.putExtra("idCourse",curso.getId());
            iDetalle.putExtra("fullname",curso.getFullname());
            iDetalle.putExtra("shortname",curso.getShortname());
            iDetalle.putExtra("teacher",curso.getTeacherName());
            contexto.startActivity(iDetalle);
        });*/
    }


    @Override
    public int getItemCount() {
            return listaCursos.size();
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
