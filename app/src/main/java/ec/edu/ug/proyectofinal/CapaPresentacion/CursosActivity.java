package ec.edu.ug.proyectofinal.CapaPresentacion;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ec.edu.ug.proyectofinal.CapaPresentacion.Adapters.CursoAdapter;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Cursos;
import ec.edu.ug.proyectofinal.CapaDatos.Models.User;
import ec.edu.ug.proyectofinal.CapaServicio.MoodleCursos;
import ec.edu.ug.proyectofinal.R;


public class CursosActivity extends AppCompatActivity {

    private TextView txtNombre,txt;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso);

        txtNombre = findViewById(R.id.txtNombre);
        recyclerView = findViewById(R.id.rvCursos);
        recyclerView.setLayoutManager(new GridLayoutManager(CursosActivity.this, 2));

        String tk = getIntent().getStringExtra("WSTOKEN");
        MoodleCursos moodleCurso = new MoodleCursos();

        moodleCurso.obtenerUsuario(tk, new MoodleCursos.MoodleUserListener() {
            @Override
            public void onUserReceived(User usuario) {
                String saludo_nombre = getString(R.string.greeting_hello,usuario.getFirstname());
                txtNombre.setText(saludo_nombre);
                moodleCurso.obtenerCursos(tk,usuario.getUserid(), new MoodleCursos.MoodleCursoListener() {
                    @Override
                    public void onCursosReceived(List<Cursos> cursos) {
                        CursoAdapter adapter = new CursoAdapter(cursos);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onError(String error) {
                        txtNombre.setText("Error al cargar datos");
                    }
                });
            }
            @Override
            public void onError(String error) {
                txtNombre.setText("Error al cargar datos");
            }
        });




    }


}
