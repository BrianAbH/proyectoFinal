package ec.edu.ug.proyectofinal.CapaServicio;

import java.util.List;

import ec.edu.ug.proyectofinal.CapaDatos.Models.Cursos.TeacherCourseResponse;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Cursos.UserCourse;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Foros.Foros;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Foros.ForoResultados;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Foros.NuevaDiscusion;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Recursos;
import ec.edu.ug.proyectofinal.CapaDatos.Models.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MoodleApiService {
    @GET("webservice/rest/server.php")
    Call<User> getUserData(
            @Query("wstoken") String token,
            @Query("wsfunction") String function,
            @Query("moodlewsrestformat") String format
    );

    @GET("webservice/rest/server.php")
    Call<UserCourse[]> getCourseData(
            @Query("wstoken") String token,
            @Query("wsfunction") String function,
            @Query("moodlewsrestformat") String format,
            @Query("userid") int id
    );

    @GET("webservice/rest/server.php")
    Call<TeacherCourseResponse> getCourseTeacher(
            @Query("wstoken") String token,
            @Query("wsfunction") String function,
            @Query("moodlewsrestformat") String format,
            @Query("field") String field,
            @Query("value") int idCourse
    );

    @GET("webservice/rest/server.php")
    Call<List<Recursos.Seccion>> getResourcesCourse(
            @Query("wstoken") String token,
            @Query("wsfunction") String function,
            @Query("moodlewsrestformat") String format,
            @Query("courseid") int idCourse
    );

    @GET("webservice/rest/server.php")
    Call<Foros[]> getForosCourse(
            @Query("wstoken") String token,
            @Query("wsfunction") String function,
            @Query("moodlewsrestformat") String format,
            @Query("courseids[0]") int courseids
    );

    @GET("webservice/rest/server.php")
    Call<ForoResultados.Respuesta> getRespuestasForo(
            @Query("wstoken") String token,
            @Query("wsfunction") String function,
            @Query("moodlewsrestformat") String format,
            @Query("forumid") int forumid
    );


    @FormUrlEncoded
    @POST("webservice/rest/server.php")
    Call<NuevaDiscusion> crearDiscusion(
            @Field("wstoken") String token,
            @Field("wsfunction") String function,
            @Field("moodlewsrestformat") String format,
            @Field("forumid") int forumId,
            @Field("subject") String subject,
            @Field("message") String message
    );


}