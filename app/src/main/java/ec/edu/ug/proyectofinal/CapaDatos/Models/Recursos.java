package ec.edu.ug.proyectofinal.CapaDatos.Models;

import java.util.List;
public class Recursos {
    public static class Seccion {
        public int id;
        public String name;
        public List<Modulo> modules;
    }

    public static class Modulo {
        public int id;
        public String name;
        public String modname;
        public List<Contenido> contents;
    }

    public static class Contenido {
        public String filename;
        public String fileurl;
    }
}