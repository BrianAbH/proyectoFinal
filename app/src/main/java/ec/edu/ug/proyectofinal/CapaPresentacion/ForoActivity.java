package ec.edu.ug.proyectofinal.CapaPresentacion;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.List;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Foros.ForoResultados;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Foros.NuevaDiscusion;
import ec.edu.ug.proyectofinal.CapaPresentacion.Adapters.RespuestasAdapter;
import ec.edu.ug.proyectofinal.CapaServicio.Listener.ApiListener;
import ec.edu.ug.proyectofinal.CapaServicio.MoodleRepository;
import ec.edu.ug.proyectofinal.R;

public class ForoActivity extends AppCompatActivity {

    private int forumid, respuestas;
    private String titulo, profesor, fecha, token, descripcion;
    private SharedPreferences preferences;

    private TextView tvTitulo, tvDescripcion, tvProfesor, tvFecha, tvRespuestas;
    private RecyclerView rvRespuestas;
    private CardView cardRespuesta;
    private MaterialButton btnResponder;
    private Button btnCancel, btnPublish;
    private ScrollView scrollView;
    private EditText etComment;

    private MoodleRepository moodle;

    private List<ForoResultados.Discusio> listaRespuestas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foro);
        moodle = new MoodleRepository();
        obtenerIntents();
        iniciarComponentes();
        setearTextos();
        mostrarRespuestas();
        eventos();

    }

    private void eventos(){
        btnResponder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
                cardRespuesta.setVisibility(View.VISIBLE);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardRespuesta.setVisibility(View.GONE);
            }
        });

        btnPublish.setOnClickListener(v->{
            enviarRespuesta();
            cardRespuesta.setVisibility(View.GONE);
        });
    }

    private void iniciarComponentes(){
        scrollView = findViewById(R.id.scrollView);
        tvTitulo = findViewById(R.id.tvTitulo);
        tvDescripcion = findViewById(R.id.tvDescripcion);
        tvProfesor = findViewById(R.id.tvProfesor);
        tvFecha = findViewById(R.id.tvFecha);
        tvRespuestas = findViewById(R.id.tvRespuestas);
        rvRespuestas = findViewById(R.id.rvRespuestas);
        cardRespuesta = findViewById(R.id.cardRespuesta);
        btnResponder = findViewById(R.id.btnResponder);
        btnCancel = findViewById(R.id.btnCancel);
        btnPublish = findViewById(R.id.btnPublish);
        etComment = findViewById(R.id.etComment);
    }

    private void obtenerIntents(){
        forumid = getIntent().getIntExtra("forumid",0);
        titulo = getIntent().getStringExtra("titulo");
        descripcion = getIntent().getStringExtra("descripcion");
        profesor = getIntent().getStringExtra("profesor");
        fecha = getIntent().getStringExtra("fecha");
        respuestas = getIntent().getIntExtra("respuestas",0);
        preferences = getSharedPreferences("SessionPrefs", MODE_PRIVATE);
        token = preferences.getString("WSTOKEN","Valor por defecto");
    }

    private void setearTextos(){
        tvTitulo.setText(titulo);
        tvDescripcion.setText(descripcion);
        tvProfesor.setText(profesor);
        tvFecha.setText(fecha);
        tvRespuestas.setText(String.valueOf(respuestas));
    }

    private void mostrarRespuestas(){
        moodle.obtenerRespuestas(token, forumid, new ApiListener<List<ForoResultados.Discusio>>() {
            @Override
            public void onSuccess(List<ForoResultados.Discusio> data) {
                try {
                    if (data == null) return;

                    listaRespuestas = data;

                    RespuestasAdapter foros = new RespuestasAdapter(listaRespuestas);
                    if (!ForoActivity.this.isFinishing()) {
                        rvRespuestas.setLayoutManager(new LinearLayoutManager(ForoActivity.this));
                        rvRespuestas.setAdapter(foros);
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
    }

    private void enviarRespuesta(){
        String message = etComment.getText().toString().trim();
        if(message.isEmpty() || etComment.length()==0){
            etComment.requestFocus();
            etComment.setError(getString(R.string.message_vali));
            return;
        }

        moodle.enviarRespuesta(token,forumid,titulo, message, new ApiListener<NuevaDiscusion>() {
            @Override
            public void onSuccess(NuevaDiscusion data) {
                int nuevoId = data.getDiscussionid();
                Toast.makeText(ForoActivity.this, getString(R.string.disc_toast_success) + nuevoId, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(ForoActivity.this, getString(R.string.disc_toast_fall) + errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void reiniciarActividad(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
