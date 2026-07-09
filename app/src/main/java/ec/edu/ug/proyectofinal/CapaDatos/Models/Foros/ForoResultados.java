package ec.edu.ug.proyectofinal.CapaDatos.Models.Foros;
import com.google.gson.annotations.SerializedName;
import java.util.List;
public class ForoResultados {

    public static class Respuesta {
        @SerializedName("discussions")
        public List<Discusio> discussions;
    }

    public static class Discusio {
        public String userfullname;
        public String message;
        public long created;

        public String getCleanmessage() {
            if (this.message == null) return "";
            return this.message.replaceAll("<[^>]*>", "");
        }

        public String getfecha(){
            java.util.Date date = new java.util.Date(created * 1000);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+8"));
            String fechaFormateada = sdf.format(date);
            return fechaFormateada;
        }
    }
}
