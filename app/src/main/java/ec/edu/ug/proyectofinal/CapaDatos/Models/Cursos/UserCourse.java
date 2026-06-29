package ec.edu.ug.proyectofinal.CapaDatos.Models.Cursos;

import java.util.List;

public class UserCourse {
    private int id;
    private String fullname;
    private String shortname;
    // Campo nuevo con su Getter y Setter
    private String teacherName = "Cargando...";
    // Getters y Setters
    public int getId() { return id; }
    public String getFullname() { return fullname; }
    public String getShortname() { return shortname; }

    public String getTeacherName() { return teacherName; }
    public void setTeacherName(String teacherName) { this.teacherName = teacherName; }

}
