package ec.edu.ug.proyectofinal.CapaPresentacion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import ec.edu.ug.proyectofinal.CapaDatos.Models.Foros.Foros;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Recursos;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Tareas.Tarea;
import ec.edu.ug.proyectofinal.CapaPresentacion.Adapters.ForosAdapter;
import ec.edu.ug.proyectofinal.CapaPresentacion.Adapters.RecursoAdapter;
import ec.edu.ug.proyectofinal.CapaPresentacion.Adapters.TareaAdapter;
import ec.edu.ug.proyectofinal.CapaServicio.Listener.ApiListener;
import ec.edu.ug.proyectofinal.CapaServicio.MoodleRepository;
import ec.edu.ug.proyectofinal.R;

public class DetalleCursoActivity extends AppCompatActivity {
    // TextView para mostrar la información principal del curso
    private TextView txtNombreCurso, txtShortName, txtProfesor;

    // Botón para regresar a la pantalla principal
    private ImageButton btnBack;

    // Control de pestañas (Recursos, Foros, etc.)
    private TabLayout tabLayout;

    // RecyclerView donde se mostrará el contenido de cada pestaña
    private RecyclerView rvContenido;

    // Repositorio encargado de realizar las consultas a Moodle
    private MoodleRepository moodle;

    // Adaptador y lista para mostrar los recursos del curso
    private RecursoAdapter recursoAdapter;
    private List<Recursos.Modulo> listaRecursos = new ArrayList<>();

    // Adaptador y lista para mostrar los foros del curso
    private ForosAdapter forosAdapter;
    private List<Foros> listaForos = new ArrayList<>();

    //Adaptador y lista para mostrar las tareas del curso
    private TareaAdapter tareaAdapter;
    private List<Tarea> listaTarea = new ArrayList<>();

    // Datos del curso y sesión del usuario
    private int idCourse;
    private String fullname, shortname, teachername, token;
    // Preferencias compartidas donde se almacena el token de sesión
    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_curso);
        // Inicializa el repositorio para consumir la API de Moodle
        moodle = new MoodleRepository();
        // Obtiene los datos enviados desde la actividad anterior
        obtenerPreferences();
        // Inicializa los componentes de la interfaz
        iniciarComponentes();
        // Configura el botón de regreso
        volver();
        // Muestra la información del curso en pantalla
        mostarNombres();
        // Configura el RecyclerView y carga inicialmente los recursos
        iniciarAdapters();
        // Listener para detectar el cambio entre pestañas
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                // Pestaña Tareas
                if (tab.getPosition() == 0) {
                    rvContenido.setAdapter(tareaAdapter);
                    cargarTareas();
                }
                // Pestaña Foros
                else if (tab.getPosition() == 1) {
                    rvContenido.setAdapter(forosAdapter);
                    mostrarForos();

                }
                // Pestaña Recursos
                else {
                    rvContenido.setAdapter(recursoAdapter);
                    mostrarRecursos();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // Ajusta el padding para respetar las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detalle), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Inicializa todos los componentes gráficos de la actividad.
     */
    private void iniciarComponentes() {
        txtNombreCurso = findViewById(R.id.txtNombreCurso);
        txtShortName = findViewById(R.id.txtShortName);
        txtProfesor = findViewById(R.id.txtProfesor);
        btnBack = findViewById(R.id.btnBack);
        tabLayout = findViewById(R.id.tabLayout);
        rvContenido = findViewById(R.id.rvContenido);
    }

    /**
     * Configura el RecyclerView y los adaptadores.
     * Inicialmente se muestran los recursos del curso.
     */
    private void iniciarAdapters() {
        rvContenido.setLayoutManager(new LinearLayoutManager(this));
        // Adaptador para las tareas
        tareaAdapter = new TareaAdapter(listaTarea);
        // Adaptador para los foros
        forosAdapter = new ForosAdapter(listaForos, teachername);
        // Adaptador para los recursos
        recursoAdapter = new RecursoAdapter(listaRecursos, token);
        // Se establece como adaptador inicial
        rvContenido.setAdapter(tareaAdapter);
        // Carga los recursos desde Moodle
        cargarTareas();
    }

    /**
     * Obtiene los recursos del curso desde Moodle
     * y actualiza el RecyclerView.
     */

    private void cargarTareas() {
        moodle.obtenerTareas(token, idCourse, new ApiListener<List<Tarea>>() {
            @Override
            public void onSuccess(List<Tarea> data) {
                if (data == null) {return;}
                if (data.isEmpty()) {rvContenido.setAdapter(null);
                    Toast.makeText(DetalleCursoActivity.this, "Este curso no tiene tareas", Toast.LENGTH_SHORT).show();
                    return;
                }
                tareaAdapter.actualizarDatos(data);
            }
            @Override
            public void onError(String message) {
                Toast.makeText(DetalleCursoActivity.this, "No se pudieron cargar las tareas", Toast.LENGTH_LONG).show();
            }
        });
    }
    private void mostrarRecursos() {
        moodle.obtenerRecursosCursos(token, idCourse, new ApiListener<List<Recursos.Modulo>>() {
            @Override
            public void onSuccess(List<Recursos.Modulo> data) {
                try {
                    if (data == null || DetalleCursoActivity.this.isFinishing()) return;
                    recursoAdapter.actualizarDatos(data);
                } catch (Exception e) {
                    Log.e("CRASH_UI", "Error al actualizar el RecyclerView", e);
                }
            }
            @Override
            public void onError(String message) {
                Log.e("API_ERROR", message != null ? message : "Error desconocido");
            }
        });
    }

    /*Obtiene los foros del curso desde Moodle y actualiza el RecyclerView.*/
    private void mostrarForos() {
        moodle.obtenerForos(token, idCourse, new ApiListener<List<Foros>>() {
            @Override
            public void onSuccess(List<Foros> data) {
                try {
                    if (data == null || DetalleCursoActivity.this.isFinishing()) return;
                    forosAdapter.actualizarDatos(data);
                } catch (Exception e) {
                    Log.e("CRASH_UI", "Error al actualizar el RecyclerView", e);
                }
            }
            @Override
            public void onError(String message) {
                Log.e("API_ERROR", message != null ? message : "Error desconocido");
            }
        });
    }

    /**
     * Obtiene la información del curso enviada mediante Intent
     * y recupera el token almacenado en SharedPreferences.
     */
    private void obtenerPreferences() {
        idCourse = getIntent().getIntExtra("idCourse", 0);
        fullname = getIntent().getStringExtra("fullname");
        shortname = getIntent().getStringExtra("shortname");
        teachername = getIntent().getStringExtra("teacher");
        // Recupera el token de autenticación
        preferences = getSharedPreferences("SessionPrefs", MODE_PRIVATE);
        token = preferences.getString("WSTOKEN", "Valor por defecto");
    }

    /**
     * Configura el botón para regresar al menú principal.
     */
    private void volver() {
        btnBack.setOnClickListener(v -> {
            Intent iback = new Intent(DetalleCursoActivity.this, MainActivity.class);
            startActivity(iback);
            finish();
        });
    }

    /**
     * Muestra el nombre completo del curso,
     * su nombre corto y el profesor.
     */
    private void mostarNombres() {
        txtNombreCurso.setText(fullname);
        txtShortName.setText(shortname);
        txtProfesor.setText(teachername);
    }
}