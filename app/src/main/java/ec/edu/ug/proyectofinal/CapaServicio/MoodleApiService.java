package ec.edu.ug.proyectofinal.CapaServicio;

import ec.edu.ug.proyectofinal.CapaDatos.Models.Cursos;
import ec.edu.ug.proyectofinal.CapaDatos.Models.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MoodleApiService {
    @GET("webservice/rest/server.php")
    Call<User> getUserData(
            @Query("wstoken") String token,
            @Query("wsfunction") String function,
            @Query("moodlewsrestformat") String format
    );

    @GET("webservice/rest/server.php")
    Call<Cursos[]> getCourseData(
            @Query("wstoken") String token,
            @Query("wsfunction") String function,
            @Query("moodlewsrestformat") String format,
            @Query("userid") int id
    );



}