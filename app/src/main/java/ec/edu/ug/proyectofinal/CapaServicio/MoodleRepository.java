package ec.edu.ug.proyectofinal.CapaServicio;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ec.edu.ug.proyectofinal.CapaDatos.Models.Cursos.TeacherCourseResponse;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Cursos.UserCourse;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Foros.Foros;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Foros.ForoResultados;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Foros.NuevaDiscusion;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Recursos;
import ec.edu.ug.proyectofinal.CapaDatos.Models.User;
import ec.edu.ug.proyectofinal.CapaServicio.Listener.ApiListener;
import ec.edu.ug.proyectofinal.CapaServicio.Network.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoodleRepository {

    private final MoodleApiService api;

    public MoodleRepository() {api = RetrofitClient.getApi();}

    public void obtenerUsuario(String token, ApiListener listener) {
        api.getUserData(token, "core_webservice_get_site_info", "json").enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful() && response.body()!=null){
                    listener.onSuccess(response.body());

                }else{
                    listener.onError("Error " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });
    }

    public void obtenerCursos(String token, int userId, ApiListener listener){
        api.getCourseData(token, "core_enrol_get_users_courses", "json", userId).enqueue(new Callback<UserCourse[]>() {
            @Override
            public void onResponse(Call<UserCourse[]> call, Response<UserCourse[]> response) {
                if(response.isSuccessful() && response.body()!=null){
                    listener.onSuccess(Arrays.asList(response.body()));
                }else{
                    listener.onError("Error " + response.code());
                }
            }
            @Override
            public void onFailure(Call<UserCourse[]> call, Throwable t) {
                listener.onError(t.getMessage());
            }

        });

    }

    public void obtenerCursosTeacher(String token, int courseId, ApiListener<TeacherCourseResponse> listener){
        api.getCourseTeacher(token, "core_course_get_courses_by_field", "json", "id", courseId).enqueue(new Callback<TeacherCourseResponse>() {
            @Override
            public void onResponse(Call<TeacherCourseResponse> call, Response<TeacherCourseResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    listener.onSuccess(response.body());
                } else {
                    listener.onError("Error " + response.code());
                }
            }
            @Override
            public void onFailure(Call<TeacherCourseResponse> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });
    }

    public void obtenerRecursosCursos(String token, int courseId, ApiListener<List<Recursos.Modulo>> listener) {
        api.getResourcesCourse(token, "core_course_get_contents", "json", courseId).enqueue(new Callback<List<Recursos.Seccion>>() {
            @Override
            public void onResponse(Call<List<Recursos.Seccion>> call, Response<List<Recursos.Seccion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Recursos.Seccion> secciones = response.body();
                    List<Recursos.Modulo> soloRecursos = new ArrayList<>();
                    for (Recursos.Seccion seccion : secciones) {
                        if ("Recursos".equalsIgnoreCase(seccion.name)) {
                            if (seccion.modules != null) {
                                for (Recursos.Modulo modulo : seccion.modules) {
                                    if ("resource".equalsIgnoreCase(modulo.modname)) {
                                        soloRecursos.add(modulo);
                                    }
                                }
                            }
                            break;
                        }
                    }
                    new Handler(Looper.getMainLooper()).post(() -> {
                        listener.onSuccess(soloRecursos);
                    });
                } else {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        listener.onError("Error del servidor: " + response.code());
                    });
                }
            }
            @Override
            public void onFailure(Call<List<Recursos.Seccion>> call, Throwable t) {
                listener.onError(t.getMessage() != null ? t.getMessage() : "Error de conexión");
            }
        });
    }

    public void obtenerForos(String token, int forumid, ApiListener listener){
        api.getForosCourse(token, "mod_forum_get_forums_by_courses", "json", forumid).enqueue(new Callback<Foros[]>() {
            @Override
            public void onResponse(Call<Foros[]> call, Response<Foros[]> response) {
                if(response.isSuccessful() && response.body()!=null){
                    listener.onSuccess(Arrays.asList(response.body()));
                }else{
                    listener.onError("Error " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Foros[]> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });
    }


    public void obtenerRespuestas(String token, int forumid, ApiListener<List<ForoResultados.Discusio>> listener){
        api.getRespuestasForo(token, "mod_forum_get_forum_discussions", "json", forumid).enqueue(new Callback<ForoResultados.Respuesta>() {
            @Override
            public void onResponse(Call<ForoResultados.Respuesta> call, Response<ForoResultados.Respuesta> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<ForoResultados.Discusio> listaReal = response.body().discussions;
                    listener.onSuccess(listaReal);
                } else {
                    listener.onError("Error " + response.code());
                }
            }
            @Override
            public void onFailure(Call<ForoResultados.Respuesta> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });
    }

    public void enviarRespuesta(String token, int forumid, String subject, String message, ApiListener<NuevaDiscusion> listener) {
        api.crearDiscusion(token, "mod_forum_add_discussion", "json", forumid, subject, message).enqueue(new Callback<NuevaDiscusion>() {
            @Override
            public void onResponse(Call<NuevaDiscusion> call, Response<NuevaDiscusion> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Si Moodle responde bien, le pasamos el objeto respuesta al listener
                    listener.onSuccess(response.body());
                } else {
                    listener.onError("Error en el servidor: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<NuevaDiscusion> call, Throwable t) {
                // Error de conexión o red
                listener.onError(t.getMessage());
            }
        });
    }
}