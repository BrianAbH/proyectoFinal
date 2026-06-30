package ec.edu.ug.proyectofinal.CapaPresentacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import ec.edu.ug.proyectofinal.CapaServicio.MoodleAuthManager;
import ec.edu.ug.proyectofinal.R;

public class PerfilActivity extends AppCompatActivity {

    private CardView cardSesion,btnCurso;
    private MoodleAuthManager auth;
    private TextView txtNombre, txtUni;
    private ImageView imgPerfil;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        String nombre = getIntent().getStringExtra("nombre");
        String uni = getIntent().getStringExtra("uni");

        IniciarComponentes();
        mostrarNombre(nombre, uni);

        btnCurso.setOnClickListener(v->{
            finish();
            overridePendingTransition(0, 0);
        });

        cardSesion.setOnClickListener(v->{
            auth.logoutFromApp(PerfilActivity.this);
        });
    }

    private void IniciarComponentes(){
        txtNombre = findViewById(R.id.txtNombre);
        txtUni = findViewById(R.id.txtUni);
        cardSesion = findViewById(R.id.cardSesion);
        btnCurso = findViewById(R.id.btnCurso);
        imgPerfil = findViewById(R.id.igPerfil);
        imgPerfil.setColorFilter(ContextCompat.getColor(this, R.color.code_orange), android.graphics.PorterDuff.Mode.SRC_IN);
    }


    private void mostrarNombre(String nombre, String uni){
        txtNombre.setText(nombre);
        txtUni.setText(uni);
    }

}
