package ec.edu.ug.proyectofinal.CapaDatos.Models.Tareas;

public class Submission {

    private int assignmentid;
    private String text;
    private String fileUrl; // opcional

    public Submission(int assignmentid, String text, String fileUrl) {
        this.assignmentid = assignmentid;
        this.text = text;
        this.fileUrl = fileUrl;
    }

    public int getAssignmentid() { return assignmentid; }
    public String getText() { return text; }
    public String getFileUrl() { return fileUrl; }
}