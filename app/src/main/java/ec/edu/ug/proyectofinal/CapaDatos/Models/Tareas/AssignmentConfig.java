package ec.edu.ug.proyectofinal.CapaDatos.Models.Tareas;

import com.google.gson.annotations.SerializedName;

public class AssignmentConfig {

    @SerializedName("plugin")
    private String plugin;

    @SerializedName("subtype")
    private String subtype;

    @SerializedName("name")
    private String name;

    @SerializedName("value")
    private String value;

    public String getPlugin() {
        return plugin;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}