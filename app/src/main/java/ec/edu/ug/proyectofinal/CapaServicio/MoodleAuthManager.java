package ec.edu.ug.proyectofinal.CapaServicio;

import android.net.Uri;

import android.util.Base64;
import android.util.Log;

import ec.edu.ug.proyectofinal.CapaPresentacion.MainActivity;

public class MoodleAuthManager {
    private static final String TAG = "MoodleAuthManager";
    private static final String SCHEME_TARGET = "miapp";
    private static final String TOKEN_SEPARATOR = "token=";
    private static final String MOODLE_DELIMITER = ":::";

    public static String extractWsToken(Uri data) {
        if (data == null || !SCHEME_TARGET.equals(data.getScheme())) {
            return null;
        }

        String uriString = data.toString();
        if (!uriString.contains(TOKEN_SEPARATOR)) {
            Log.e(TAG, "La URL devuelta por Moodle no contiene el parámetro 'token='");
            return null;
        }

        try {
            String[] urlParts = uriString.split(TOKEN_SEPARATOR);
            if (urlParts.length < 2) return null;
            String encodedToken = urlParts[1];

            byte[] decodedBytes = Base64.decode(encodedToken, Base64.DEFAULT);
            String decodedString = new String(decodedBytes);

            String[] moodleParts = decodedString.split(MOODLE_DELIMITER);
            if (moodleParts.length >= 2) {
                String wsToken = moodleParts[1];
                Log.d(TAG, "¡Token extraído con éxito de la clase independiente!");
                return wsToken;
            } else {
                Log.e(TAG, "La estructura decodificada de Moodle no tiene las secciones esperadas.");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error crítico al decodificar la cadena Base64: " + e.getMessage());
        }
        return null;
    }


    public static void logoutFromApp(android.content.Context context) {

        android.content.SharedPreferences prefs = context.getSharedPreferences("SessionPrefs", android.content.Context.MODE_PRIVATE);
        prefs.edit().remove("WSTOKEN").apply();

        android.content.Intent intent = new android.content.Intent(context, MainActivity.class);
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
