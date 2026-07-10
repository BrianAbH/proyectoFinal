package ec.edu.ug.proyectofinal.CapaDatos.Models.Tareas;

import com.google.gson.annotations.SerializedName;

public class UploadResponse {

    @SerializedName("component")
    private String component;

    @SerializedName("contextid")
    private int contextid;

    @SerializedName("userid")
    private int userid;

    @SerializedName("filearea")
    private String filearea;

    @SerializedName("filename")
    private String filename;

    @SerializedName("filepath")
    private String filepath;

    @SerializedName("itemid")
    private int itemid;

    @SerializedName("license")
    private String license;

    @SerializedName("author")
    private String author;

    @SerializedName("source")
    private String source;

    public String getFilename() {
        return filename;
    }

    public int getItemid() {
        return itemid;
    }

    public String getFilearea() {
        return filearea;
    }
}