package ec.edu.ug.proyectofinal.CapaServicio;

import android.util.Log;

import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import ec.edu.ug.proyectofinal.CapaDatos.Models.Cursos;
import ec.edu.ug.proyectofinal.CapaDatos.Models.User;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoodleCursos {
    private static final String BASE_URL = "https://192.168.100.5.nip.io";
    public interface MoodleUserListener {
        void onUserReceived(User usuario);

        void onError(String error);
    }
    public  interface MoodleCursoListener{
        void onCursosReceived(List<Cursos> cursos);
        void onError(String error);
    }

    public void obtenerUsuario(String token, final MoodleUserListener listener) {
        OkHttpClient unsafeClient = getUnsafeOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(unsafeClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MoodleApiService apiService = retrofit.create(MoodleApiService.class);
        Call<User> call = apiService.getUserData(token, "core_webservice_get_site_info", "json");

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User usuario = response.body();

                    if (listener != null) {
                        listener.onUserReceived(usuario);
                    }
                } else {
                    if (listener != null) {
                        listener.onError("Error en el servidor. Código: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("API_MOODLE", "Fallo en la conexión: " + t.getMessage());
                if (listener != null) {
                    listener.onError(t.getMessage());
                }
            }
        });
    }


    public void obtenerCursos(String token,int userid, final MoodleCursoListener listener) {
        OkHttpClient unsafeClient = getUnsafeOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(unsafeClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MoodleApiService apiService = retrofit.create(MoodleApiService.class);
        Call<Cursos[]> call = apiService.getCourseData(token, "core_enrol_get_users_courses", "json", userid);

        call.enqueue(new Callback<Cursos[]>() {
            @Override
            public void onResponse(Call<Cursos[]> call, Response<Cursos[]> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Cursos> cursos = Arrays.asList(response.body());

                    if (listener != null) {
                        listener.onCursosReceived(cursos);
                    }
                } else {
                    if (listener != null) {
                        listener.onError("Error en el servidor. Código: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<Cursos[]> call, Throwable t) {
                Log.e("API_MOODLE", "Fallo en la conexión: " + t.getMessage());
                if (listener != null) {
                    listener.onError(t.getMessage());
                }
            }
        });
    }


    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {}
                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {return new java.security.cert.X509Certificate[]{};}
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);

            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {return true;}
            });
            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
