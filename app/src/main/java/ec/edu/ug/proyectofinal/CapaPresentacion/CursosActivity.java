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
import ec.edu.ug.proyectofinal.CapaServicio.Listener.ApiListener;
import ec.edu.ug.proyectofinal.CapaServicio.MoodleRepository;
import ec.edu.ug.proyectofinal.R;


public class CursosActivity extends AppCompatActivity {

    private TextView txtNombre;
    private RecyclerView recyclerView;
    private MoodleRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_curso);

        inicializarComponentes();

        repository = new MoodleRepository();

        cargarUsuario(
                getIntent().getStringExtra("WSTOKEN")
        );

    }

    private void inicializarComponentes() {
        txtNombre = findViewById(R.id.txtNombre);
        recyclerView = findViewById(R.id.rvCursos);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
    }

    private void cargarUsuario(String token){
        repository.obtenerUsuario(token, new ApiListener<User>() {
            @Override
            public void onSuccess(User data) {
                mostrarNombre(data);
                cargarCursos(token, data.getUserid());
            }
            @Override
            public void onError(String error) {
                mostrarError();

            }
        });
    }

    private void cargarCursos(String token,int userId){
        repository.obtenerCursos(token, userId, new ApiListener<List<Cursos>>() {
            @Override
            public void onSuccess(List<Cursos> data) {
                mostrarCursos(data);
            }

            @Override
            public void onError(String error) {
                mostrarError();
            }
        });

    }

    private void mostrarNombre(User usuario){
        String saludo = getString(R.string.greeting_hello, usuario.getFirstname());
        txtNombre.setText(saludo);
    }

    private void mostrarCursos(List<Cursos> cursos){
        CursoAdapter adapter = new CursoAdapter(cursos);
        recyclerView.setAdapter(adapter);
    }

    private void mostrarError(){
        txtNombre.setText("Error al cargar datos");
    }


}
