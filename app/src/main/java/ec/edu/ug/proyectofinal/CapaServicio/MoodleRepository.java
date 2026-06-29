package ec.edu.ug.proyectofinal.CapaServicio;

import java.util.Arrays;
import ec.edu.ug.proyectofinal.CapaDatos.Models.Cursos;
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

                }else{listener.onError("Error " + response.code());}
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                listener.onError(t.getMessage());
            }
        });
    }

    public void obtenerCursos(String token, int userId, ApiListener listener){
        api.getCourseData(token, "core_enrol_get_users_courses", "json", userId).enqueue(new Callback<Cursos[]>() {
            @Override
            public void onResponse(Call<Cursos[]> call, Response<Cursos[]> response) {
                if(response.isSuccessful() && response.body()!=null){
                    listener.onSuccess(Arrays.asList(response.body()));
                }else{
                    listener.onError("Error " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Cursos[]> call, Throwable t) {
                listener.onError(t.getMessage());
            }

        });

    }

}