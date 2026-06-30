package ec.edu.ug.proyectofinal.CapaPresentacion;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

import ec.edu.ug.proyectofinal.CapaServicio.MoodleAuthManager;
import ec.edu.ug.proyectofinal.R;


public class MainActivity extends AppCompatActivity {
    private static String moodleUrl= "https://192.168.100.5.nip.io/admin/tool/mobile/launch.php";
    private static String  service = "moodle_mobile_app";
    private static String  urlScheme = "miapp";
    private static String  passport = String.valueOf(new Random().nextInt(99999));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        checkAndRedirect(getIntent());

        Button btn = findViewById(R.id.btnGoogle);
        btn.setOnClickListener(v-> {
            String url = moodleUrl + "?service=" + service + "&passport=" + passport + "&urlscheme=" + urlScheme + "&loginforapp=1";
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(url));
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkAndRedirect(intent);
    }

    private void checkAndRedirect(Intent intent) {
        if (intent != null && intent.getData() != null) {
            String tokenLimpio = MoodleAuthManager.extractWsToken(intent.getData());
            if (tokenLimpio != null) {
                getSharedPreferences("SessionPrefs", MODE_PRIVATE).edit().putString("WSTOKEN", tokenLimpio).apply();
                redigirACursos(tokenLimpio);
                return;
            }
        }
        String tokenGuardado = getSharedPreferences("SessionPrefs", MODE_PRIVATE).getString("WSTOKEN", null);
        if (tokenGuardado != null) {
            redigirACursos(tokenGuardado);
        }
    }

    private void redigirACursos(String token) {
        Intent intentCursos = new Intent(MainActivity.this, CursosActivity.class);
        intentCursos.putExtra("WSTOKEN", token);
        startActivity(intentCursos);
        finish();
    }
}