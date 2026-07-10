package ec.edu.ug.proyectofinal.CapaServicio;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ec.edu.ug.proyectofinal.CapaDatos.Models.Cursos.TeacherCourseResponse;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Cursos.UserCourse;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Foros.Foros;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Foros.ForoResultados;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Foros.NuevaDiscusion;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Recursos;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Tareas.Assignment;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Tareas.AssignmentConfig;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Tareas.AssignmentResponse;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Tareas.Tarea;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Tareas.UploadResponse;
import ec.edu.ug.proyectofinal.CapaDatos.Models.User;
import ec.edu.ug.proyectofinal.CapaServicio.Listener.ApiListener;
import ec.edu.ug.proyectofinal.CapaServicio.Network.RetrofitClient;
import okhttp3.MultipartBody;
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

    public void guardarEntrega(String token, int assignmentId, String texto, int fileItemId, ApiListener<Object> listener) {
        String textoSeguro = texto != null ? texto : "";

        api.submitAssignment(token, "mod_assign_save_submission", "json", assignmentId, textoSeguro, 1, 0, fileItemId).enqueue(new Callback<Object>() {

            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (!response.isSuccessful()) {
                    listener.onError("Error HTTP al guardar entrega: " + response.code());
                    return;
                }

                Log.d("MOODLE_SUBMISSION", "Entrega guardada correctamente");

                listener.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Object> call, Throwable throwable) {
                Log.e("MOODLE_SUBMISSION", "Error al guardar entrega", throwable);

                listener.onError(throwable.getMessage() != null ? throwable.getMessage() : "Error de conexión");
            }
        });
    }

    public void subirArchivo(String token, MultipartBody.Part file, ApiListener<Integer> listener) {
        api.uploadFile(token, "/", file).enqueue(new Callback<List<UploadResponse>>() {

            @Override
            public void onResponse(Call<List<UploadResponse>> call, Response<List<UploadResponse>> response) {
                if (!response.isSuccessful()) {
                    listener.onError("Error HTTP al subir archivo: " + response.code());
                    return;
                }

                List<UploadResponse> archivos = response.body();

                if (archivos == null || archivos.isEmpty()) {
                    listener.onError("Moodle no devolvió información del archivo");
                    return;
                }

                UploadResponse archivo = archivos.get(0);

                if (archivo.getItemid() <= 0) {
                    listener.onError("Moodle devolvió un itemid inválido");
                    return;
                }

                Log.d("MOODLE_UPLOAD", "Archivo subido. Nombre: " + archivo.getFilename() + ", itemid: " + archivo.getItemid());

                listener.onSuccess(archivo.getItemid());
            }

            @Override
            public void onFailure(Call<List<UploadResponse>> call, Throwable throwable) {
                Log.e("MOODLE_UPLOAD", "Error al subir archivo", throwable);

                listener.onError(throwable.getMessage() != null ? throwable.getMessage() : "Error de conexión al subir archivo");
            }
        });
    }

    public void obtenerTareas(String token, int courseId, ApiListener<List<Tarea>> listener) {
        api.getAssignments(token, "mod_assign_get_assignments", "json", courseId).enqueue(new Callback<AssignmentResponse>() {

            @Override
            public void onResponse(Call<AssignmentResponse> call, Response<AssignmentResponse> response) {
                if (!response.isSuccessful()) {
                    listener.onError("Error HTTP al cargar tareas: " + response.code());
                    return;
                }

                AssignmentResponse body = response.body();

                if (body == null || body.getCourses() == null) {
                    listener.onError("Moodle no devolvió información de tareas");
                    return;
                }

                List<Tarea> tareas = new ArrayList<>();

                for (AssignmentResponse.CourseAssignments course : body.getCourses()) {

                    if (course.getId() != courseId) {
                        continue;
                    }

                    if (course.getAssignments() == null) {
                        break;
                    }

                    for (Assignment assignment : course.getAssignments()) {

                        boolean permiteTexto = false;
                        boolean permiteArchivo = false;

                        if (assignment.getConfigs() != null) {
                            for (AssignmentConfig config : assignment.getConfigs()) {

                                Log.d("ASSIGN_CONFIG", "Tarea=" + assignment.getName() + ", plugin=" + config.getPlugin() + ", name=" + config.getName() + ", value=" + config.getValue());

                                if ("onlinetext".equalsIgnoreCase(config.getPlugin()) && "enabled".equalsIgnoreCase(config.getName()) && "1".equals(config.getValue())) {

                                    permiteTexto = true;
                                }

                                if ("file".equalsIgnoreCase(config.getPlugin()) && "enabled".equalsIgnoreCase(config.getName()) && "1".equals(config.getValue())) {

                                    permiteArchivo = true;
                                }
                            }
                        }

                        Tarea tarea = new Tarea(assignment.getId(), assignment.getName(), assignment.getIntro(), assignment.getDuedate(), permiteTexto, permiteArchivo);

                        tareas.add(tarea);
                    }

                    break;
                }

                Log.d("MOODLE_TASKS", "Tareas procesadas: " + tareas.size());

                listener.onSuccess(tareas);
            }

            @Override
            public void onFailure(Call<AssignmentResponse> call, Throwable throwable) {
                Log.e("MOODLE_TASKS", "Error al consultar tareas", throwable);

                listener.onError(throwable.getMessage() != null ? throwable.getMessage() : "Error de conexión");
            }
        });
    }
}