package ec.edu.ug.proyectofinal.CapaServicio.Network;

import ec.edu.ug.proyectofinal.CapaServicio.MoodleApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://192.168.100.5.nip.io/";

    private static Retrofit retrofit;

    private RetrofitClient(){}

    public static MoodleApiService getApi(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(UnsafeOkHttpClient.getClient()).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit.create(MoodleApiService.class);
    }

}