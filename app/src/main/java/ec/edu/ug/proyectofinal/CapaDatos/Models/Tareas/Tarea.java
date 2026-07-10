package ec.edu.ug.proyectofinal.CapaDatos.Models.Tareas;

public class Tarea {

    private int id;
    private String name;
    private String description;
    private long duedate;

    private boolean allowOnlineText;
    private boolean allowFileSubmission;

    public Tarea() {
    }

    public Tarea(int id, String name, String description, long duedate, boolean allowOnlineText, boolean allowFileSubmission) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duedate = duedate;
        this.allowOnlineText = allowOnlineText;
        this.allowFileSubmission = allowFileSubmission;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDuedate() {
        return duedate;
    }

    public void setDuedate(long duedate) {
        this.duedate = duedate;
    }

    public boolean isAllowOnlineText() {
        return allowOnlineText;
    }

    public void setAllowOnlineText(boolean allowOnlineText) {
        this.allowOnlineText = allowOnlineText;
    }

    public boolean isAllowFileSubmission() {
        return allowFileSubmission;
    }

    public void setAllowFileSubmission(boolean allowFileSubmission) {
        this.allowFileSubmission = allowFileSubmission;
    }
}