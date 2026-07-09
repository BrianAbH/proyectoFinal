package ec.edu.ug.proyectofinal.CapaDatos.Models.Foros;

public class Foros {
    private int id;
    private String name;
    private int numdiscussions;
    private boolean cancreatediscussions;
    private String intro;
    private long duedate;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumdiscussions() {
        return numdiscussions;
    }

    public boolean isCancreatediscussions() {
        return cancreatediscussions;
    }

    public String getfecha(){
        java.util.Date date = new java.util.Date(duedate * 1000);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("GMT+8"));
        String fechaFormateada = sdf.format(date);
        return fechaFormateada;
    }

    public String getCleanintro() {
        if (this.intro == null) return "";
        return this.intro.replaceAll("<[^>]*>", "");
    }
}
