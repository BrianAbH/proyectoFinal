package ec.edu.ug.proyectofinal.CapaPresentacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ec.edu.ug.proyectofinal.R;

public class DetalleCursoActivity extends AppCompatActivity {

    private TextView txtNombreCurso, txtShortName, txtProfesor;
    private ImageButton btnBack;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_curso);

        String fullname = getIntent().getStringExtra("fullname");
        String shortname = getIntent().getStringExtra("shortname");
        String teachername = getIntent().getStringExtra("teacher");

        iniciarComponentes();
        volver();
        txtNombreCurso.setText(fullname);
        txtShortName.setText(shortname);
        txtProfesor.setText(teachername);

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
    }

    private void volver(){
        btnBack.setOnClickListener(v->{
            Intent iback = new Intent(DetalleCursoActivity.this,MainActivity.class);
            startActivity(iback);
            finish();
        });
    }

}
