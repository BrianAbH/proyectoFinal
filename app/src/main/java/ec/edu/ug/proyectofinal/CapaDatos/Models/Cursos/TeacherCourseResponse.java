package ec.edu.ug.proyectofinal.CapaDatos.Models.Cursos;

import java.util.List;

public class TeacherCourseResponse {
    private List<TeacherCourseDetail> courses;

    public List<TeacherCourseDetail> getCourses() { return courses; }

    public static class TeacherCourseDetail {
        private int id;
        private String fullname;
        private List<Contact> contacts;

        public List<Contact> getContacts() { return contacts; }
    }

    public static class Contact {
        private int id;
        private String fullname;

        public String getFullname() { return fullname; }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }
    }
}

