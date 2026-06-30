package ec.edu.ug.proyectofinal.CapaPresentacion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Cursos.UserCourse;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Cursos.TeacherCourseResponse;
import ec.edu.ug.proyectofinal.CapaPresentacion.Adapters.CursoAdapter;
import ec.edu.ug.proyectofinal.CapaDatos.Models.User;
import ec.edu.ug.proyectofinal.CapaServicio.Listener.ApiListener;
import ec.edu.ug.proyectofinal.CapaServicio.MoodleRepository;
import ec.edu.ug.proyectofinal.R;


public class CursosActivity extends AppCompatActivity {

    private TextView txtNombre;
    private RecyclerView recyclerView;
    private MoodleRepository repository;
    private List<UserCourse> listaCursos;
    private CursoAdapter cursoAdapter;

    private CardView btnCurso, btnPerfil;
    private User usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso);
        inicializarComponentes();
        repository = new MoodleRepository();
        cargarUsuario(getIntent().getStringExtra("WSTOKEN"));

        btnPerfil = findViewById(R.id.btnPerfil);
        btnPerfil.setOnClickListener(v->{
            Intent iPerfil = new Intent(CursosActivity.this, PerfilActivity.class);
            iPerfil.putExtra("nombre", usuario.getUsername());
            iPerfil.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(iPerfil);
            overridePendingTransition(0, 0);
        });
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
                usuario = data;
                cargarCursos(token, data.getUserid());
            }
            @Override
            public void onError(String error) {
                mostrarError();

            }
        });
    }
    private void cargarCursos(String token, int userId) {
        repository.obtenerCursos(token, userId, new ApiListener<List<UserCourse>>() {
            @Override
            public void onSuccess(List<UserCourse> data) {
                listaCursos = data;
                cursoAdapter = new CursoAdapter(listaCursos);
                recyclerView.setAdapter(cursoAdapter);

                for (int i = 0; i < listaCursos.size(); i++) {
                    final int posicionActual = i;
                    UserCourse curso = listaCursos.get(posicionActual);
                    buscarProfesor(token, curso.getId(), posicionActual);
                }
            }
            @Override
            public void onError(String error) { mostrarError(); }
        });
    }

    private void buscarProfesor(String token, int courseId, final int posicion) {
        repository.obtenerCursosTeacher(token, courseId, new ApiListener<TeacherCourseResponse>() {
            @Override
            public void onSuccess(TeacherCourseResponse data) {
                if (data != null && data.getCourses() != null && !data.getCourses().isEmpty()) {
                    TeacherCourseResponse.TeacherCourseDetail detalle = data.getCourses().get(0);

                    if (detalle.getContacts() != null && !detalle.getContacts().isEmpty()) {
                        String nombreProfesor = detalle.getContacts().get(0).getFullname();

                        listaCursos.get(posicion).setTeacherName(nombreProfesor);

                        cursoAdapter.notifyItemChanged(posicion);
                    } else {
                        listaCursos.get(posicion).setTeacherName("Sin profesor");
                        cursoAdapter.notifyItemChanged(posicion);
                    }
                }
            }
            @Override
            public void onError(String error) {
                listaCursos.get(posicion).setTeacherName("Error al cargar");
                cursoAdapter.notifyItemChanged(posicion);
            }
        });
    }

    private void mostrarNombre(User usuario){
        String saludo = getString(R.string.greeting_hello, usuario.getFirstname());
        txtNombre.setText(saludo);
    }


    private void mostrarError(){
        txtNombre.setText("Error al cargar datos");
    }



}
