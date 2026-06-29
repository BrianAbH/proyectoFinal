package ec.edu.ug.proyectofinal.CapaPresentacion.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;

import ec.edu.ug.proyectofinal.CapaDatos.Models.Cursos;
import ec.edu.ug.proyectofinal.R;

public class CursoAdapter extends RecyclerView.Adapter<CursoAdapter.ViewHolder>{

    private List<Cursos> listaCursos;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Cursos curso);
    }

    public CursoAdapter(List<Cursos> listaCursos/*, OnItemClickListener listener*/) {
        this.listaCursos = listaCursos;
        //this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_curso, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Cursos curso = listaCursos.get(position);

        holder.tvNombre.setText(curso.getFullname());
        holder.tvCodigo.setText(curso.getShortname());
        //holder.tvDocente.setText(curso.getDocente());

        //holder.headerCurso.setBackgroundColor(curso.getColor());

        /*holder.cardCurso.setOnClickListener(v ->
                listener.onItemClick(curso)
        );*/
    }

    @Override
    public int getItemCount() {
        return listaCursos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardCurso;
        LinearLayout headerCurso;

        TextView tvNombre;
        TextView tvCodigo;
        //TextView tvDocente;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardCurso = itemView.findViewById(R.id.cardCurso);
            headerCurso = itemView.findViewById(R.id.headerCurso);

            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCodigo = itemView.findViewById(R.id.tvCodigo);
            //tvDocente = itemView.findViewById(R.id.tvDocente);
        }
    }
}
