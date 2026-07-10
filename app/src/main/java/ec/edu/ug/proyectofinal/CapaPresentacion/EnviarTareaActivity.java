package ec.edu.ug.proyectofinal.CapaPresentacion;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import ec.edu.ug.proyectofinal.CapaServicio.Listener.ApiListener;
import ec.edu.ug.proyectofinal.CapaServicio.MoodleRepository;
import ec.edu.ug.proyectofinal.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EnviarTareaActivity extends AppCompatActivity {

    private static final String TAG = "ENVIAR_TAREA";

    private EditText edtTexto;
    private TextView txtFile;
    private TextView txtTituloEnviar;
    private TextView txtTipoEntrega;
    private TextView txtErrorConfiguracion;

    private LinearLayout contenedorTexto;
    private LinearLayout contenedorArchivo;

    private Button btnArchivo;
    private Button btnEnviar;

    private MoodleRepository repository;

    private Uri fileUri;
    private String fileName;

    private int assignmentId;
    private String assignmentName;
    private String token;

    private boolean allowOnlineText;
    private boolean allowFileSubmission;

    private final ActivityResultLauncher<String> selectorArchivo = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        if (uri == null) {
            return;
        }

        fileUri = uri;
        fileName = obtenerNombreArchivo(uri);

        txtFile.setText(fileName != null ? fileName : "Archivo seleccionado");

        Log.d(TAG, "Archivo seleccionado: " + fileName);
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_tarea);

        repository = new MoodleRepository();

        enlazarComponentes();
        obtenerDatos();
        configurarPantalla();
        configurarEventos();
    }

    private void enlazarComponentes() {
        edtTexto = findViewById(R.id.edtTexto);
        txtFile = findViewById(R.id.txtFile);
        txtTituloEnviar = findViewById(R.id.txtTituloEnviar);
        txtTipoEntrega = findViewById(R.id.txtTipoEntrega);
        txtErrorConfiguracion = findViewById(R.id.txtErrorConfiguracion);

        contenedorTexto = findViewById(R.id.contenedorTexto);
        contenedorArchivo = findViewById(R.id.contenedorArchivo);

        btnArchivo = findViewById(R.id.btnArchivo);
        btnEnviar = findViewById(R.id.btnEnviar);
    }

    private void obtenerDatos() {
        assignmentId = getIntent().getIntExtra("assignmentId", -1);

        assignmentName = getIntent().getStringExtra("assignmentName");

        allowOnlineText = getIntent().getBooleanExtra("allowOnlineText", false);

        allowFileSubmission = getIntent().getBooleanExtra("allowFileSubmission", false);

        token = getSharedPreferences("SessionPrefs", MODE_PRIVATE).getString("WSTOKEN", null);

        Log.d(TAG, "assignmentId=" + assignmentId + ", texto=" + allowOnlineText + ", archivo=" + allowFileSubmission);
    }

    private void configurarPantalla() {
        txtTituloEnviar.setText(assignmentName != null ? assignmentName : "Enviar tarea");

        contenedorTexto.setVisibility(allowOnlineText ? View.VISIBLE : View.GONE);

        contenedorArchivo.setVisibility(allowFileSubmission ? View.VISIBLE : View.GONE);

        if (allowOnlineText && allowFileSubmission) {
            txtTipoEntrega.setText("Esta tarea permite texto y archivo.");

        } else if (allowOnlineText) {
            txtTipoEntrega.setText("Esta tarea permite solamente texto.");

        } else if (allowFileSubmission) {
            txtTipoEntrega.setText("Esta tarea permite solamente archivos.");

        } else {
            txtTipoEntrega.setText("No se detectó un tipo de entrega compatible.");

            txtErrorConfiguracion.setVisibility(View.VISIBLE);
            btnEnviar.setEnabled(false);
        }
    }

    private void configurarEventos() {
        btnArchivo.setOnClickListener(v -> selectorArchivo.launch("*/*"));

        btnEnviar.setOnClickListener(v -> comenzarEnvio());
    }

    private void comenzarEnvio() {
        if (!validarDatos()) {
            return;
        }

        btnEnviar.setEnabled(false);
        btnEnviar.setText("Enviando...");

        if (allowFileSubmission && fileUri != null) {
            subirArchivo();
        } else {
            guardarEntrega(0);
        }
    }

    private boolean validarDatos() {
        if (assignmentId <= 0) {
            mostrarMensaje("El identificador de la tarea no es válido");
            return false;
        }

        if (token == null || token.trim().isEmpty()) {
            mostrarMensaje("No existe una sesión válida de Moodle");
            return false;
        }

        String texto = edtTexto.getText().toString().trim();

        if (allowOnlineText && !allowFileSubmission && texto.isEmpty()) {

            mostrarMensaje("Escribe una respuesta antes de enviar");
            return false;
        }

        if (allowFileSubmission && !allowOnlineText && fileUri == null) {

            mostrarMensaje("Selecciona un archivo antes de enviar");
            return false;
        }

        if (allowOnlineText && allowFileSubmission && texto.isEmpty() && fileUri == null) {

            mostrarMensaje("Escribe una respuesta o selecciona un archivo");
            return false;
        }

        return true;
    }

    private void subirArchivo() {
        MultipartBody.Part filePart = prepararArchivo(fileUri);

        if (filePart == null) {
            restaurarBoton();
            mostrarMensaje("No se pudo preparar el archivo");
            return;
        }

        repository.subirArchivo(token, filePart, new ApiListener<Integer>() {
            @Override
            public void onSuccess(Integer itemId) {
                Log.d(TAG, "Itemid recibido: " + itemId);

                guardarEntrega(itemId);
            }

            @Override
            public void onError(String message) {
                restaurarBoton();

                mostrarMensaje("Error al subir archivo: " + message);
            }
        });
    }

    private void guardarEntrega(int fileItemId) {
        String texto = allowOnlineText ? edtTexto.getText().toString().trim() : "";

        repository.guardarEntrega(token, assignmentId, texto, fileItemId, new ApiListener<Object>() {
            @Override
            public void onSuccess(Object data) {
                Toast.makeText(EnviarTareaActivity.this, "Tarea guardada correctamente", Toast.LENGTH_LONG).show();

                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(String message) {
                restaurarBoton();

                mostrarMensaje("Error al guardar entrega: " + message);
            }
        });
    }

    private MultipartBody.Part prepararArchivo(Uri uri) {
        if (uri == null) {
            return null;
        }

        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);

            if (inputStream == null) {
                return null;
            }

            String nombreSeguro = fileName != null && !fileName.trim().isEmpty() ? fileName : "archivo";

            File file = new File(getCacheDir(), nombreSeguro);

            try (InputStream entrada = inputStream; OutputStream salida = new FileOutputStream(file)) {
                byte[] buffer = new byte[8192];
                int bytesLeidos;

                while ((bytesLeidos = entrada.read(buffer)) != -1) {
                    salida.write(buffer, 0, bytesLeidos);
                }
            }

            String mimeType = getContentResolver().getType(uri);

            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }

            RequestBody requestBody = RequestBody.create(file, MediaType.parse(mimeType));

            return MultipartBody.Part.createFormData("file", nombreSeguro, requestBody);

        } catch (Exception exception) {
            Log.e(TAG, "Error al preparar el archivo", exception);

            return null;
        }
    }

    private String obtenerNombreArchivo(Uri uri) {
        String nombre = null;

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor != null) {
            try {
                int indice = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

                if (indice >= 0 && cursor.moveToFirst()) {
                    nombre = cursor.getString(indice);
                }
            } finally {
                cursor.close();
            }
        }

        return nombre;
    }

    private void restaurarBoton() {
        btnEnviar.setEnabled(true);
        btnEnviar.setText("Enviar tarea");
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }
}