package ec.edu.ug.proyectofinal.CapaPresentacion;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import ec.edu.ug.proyectofinal.CapaServicio.MoodleAuthManager;
import ec.edu.ug.proyectofinal.R;

public class PerfilActivity extends AppCompatActivity {

    private CardView cardSesion;
    private MoodleAuthManager auth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        String nombre = getIntent().getStringExtra("nombre");

        TextView txtNombre = findViewById(R.id.txtNombre);
        cardSesion = findViewById(R.id.cardSesion);
        txtNombre.setText(nombre);

        cardSesion.setOnClickListener(v->{
            auth.logoutFromApp(PerfilActivity.this);
        });

    }
}
