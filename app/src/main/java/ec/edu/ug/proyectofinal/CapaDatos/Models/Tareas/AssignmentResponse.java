package ec.edu.ug.proyectofinal.CapaDatos.Models.Tareas;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AssignmentResponse {

    @SerializedName("courses")
    private List<CourseAssignments> courses;

    public List<CourseAssignments> getCourses() {
        return courses;
    }

    public static class CourseAssignments {

        @SerializedName("id")
        private int id;

        @SerializedName("fullname")
        private String fullname;

        @SerializedName("shortname")
        private String shortname;

        @SerializedName("assignments")
        private List<Assignment> assignments;

        public int getId() {
            return id;
        }

        public String getFullname() {
            return fullname;
        }

        public String getShortname() {
            return shortname;
        }

        public List<Assignment> getAssignments() {
            return assignments;
        }
    }
}