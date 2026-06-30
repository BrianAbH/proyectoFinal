package ec.edu.ug.proyectofinal.CapaPresentacion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import java.util.List;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Recursos;
import ec.edu.ug.proyectofinal.CapaPresentacion.Adapters.RecursoAdapter;
import ec.edu.ug.proyectofinal.CapaServicio.Listener.ApiListener;
import ec.edu.ug.proyectofinal.CapaServicio.MoodleRepository;
import ec.edu.ug.proyectofinal.R;

public class DetalleCursoActivity extends AppCompatActivity {

    private TextView txtNombreCurso, txtShortName, txtProfesor;
    private ImageButton btnBack;
    private TabLayout tabLayout;
    private RecyclerView rvContenido;
    private MoodleRepository moodle;

    private List<Recursos.Modulo> listaRecursos;

    private int idCourse;
    private String fullname, shortname, teachername, token;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_curso);
        idCourse = getIntent().getIntExtra("idCourse",0);
        fullname = getIntent().getStringExtra("fullname");
        shortname = getIntent().getStringExtra("shortname");
        teachername = getIntent().getStringExtra("teacher");
        preferences = getSharedPreferences("SessionPrefs", MODE_PRIVATE);
        token = preferences.getString("WSTOKEN","Valor por defecto");
        moodle = new MoodleRepository();

        iniciarComponentes();
        volver();
        mostarNombres();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                //Tab Recursos
                if (tab.getPosition() == 2) {
                    moodle.obtenerRecursosCursos(token, idCourse, new ApiListener<List<Recursos.Modulo>>() {
                        @Override
                        public void onSuccess(List<Recursos.Modulo> data) {
                            try {
                                if (data == null) return;
                                listaRecursos = data;
                                RecursoAdapter recur = new RecursoAdapter(data, token);
                                if (!DetalleCursoActivity.this.isFinishing()) {
                                    rvContenido.setLayoutManager(new LinearLayoutManager(DetalleCursoActivity.this));
                                    rvContenido.setAdapter(recur);
                                }
                            } catch (Exception e) {
                                Log.e("CRASH_UI", "Error al actualizar el RecyclerView", e);
                            }
                        }

                        @Override
                        public void onError(String message) {
                            Log.e("API_ERROR", message != null ? message : "Error desconocido");
                        }
                    });

                } else {
                    Log.e("API_ERROR", "Error desconocido");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detalle), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    private void iniciarComponentes(){
        txtNombreCurso = findViewById(R.id.txtNombreCurso);
        txtShortName = findViewById(R.id.txtShortName);
        txtProfesor = findViewById(R.id.txtProfesor);
        btnBack = findViewById(R.id.btnBack);
        tabLayout = findViewById(R.id.tabLayout);
        rvContenido = findViewById(R.id.rvContenido);
    }

    private void volver(){
        btnBack.setOnClickListener(v->{
            Intent iback = new Intent(DetalleCursoActivity.this,MainActivity.class);
            startActivity(iback);
            finish();
        });
    }
    private void mostarNombres(){
        txtNombreCurso.setText(fullname);
        txtShortName.setText(shortname);
        txtProfesor.setText(teachername);
    }

}
