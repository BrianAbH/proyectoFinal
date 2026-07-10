package ec.edu.ug.proyectofinal.CapaDatos.Models.Tareas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Assignment {

    @SerializedName("id")
    private int id;

    @SerializedName("cmid")
    private int cmid;

    @SerializedName("course")
    private int course;

    @SerializedName("name")
    private String name;

    @SerializedName("intro")
    private String intro;

    @SerializedName("allowsubmissionsfromdate")
    private long allowsubmissionsfromdate;

    @SerializedName("duedate")
    private long duedate;

    @SerializedName("cutoffdate")
    private long cutoffdate;

    @SerializedName("configs")
    private List<AssignmentConfig> configs;

    public int getId() {
        return id;
    }

    public int getCmid() {
        return cmid;
    }

    public int getCourse() {
        return course;
    }

    public String getName() {
        return name;
    }

    public String getIntro() {
        return intro;
    }

    public long getAllowsubmissionsfromdate() {
        return allowsubmissionsfromdate;
    }

    public long getDuedate() {
        return duedate;
    }

    public long getCutoffdate() {
        return cutoffdate;
    }

    public List<AssignmentConfig> getConfigs() {
        return configs;
    }
}