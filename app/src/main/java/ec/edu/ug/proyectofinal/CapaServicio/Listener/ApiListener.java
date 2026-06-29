package ec.edu.ug.proyectofinal.CapaServicio.Listener;

public interface ApiListener<T>{

    void onSuccess(T data);

    void onError(String message);

}