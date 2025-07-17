package com.unal.micasaya.ui.Resultados;


import com.google.firebase.firestore.ServerTimestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Project {
    private String documentId;
    private String name;
    private String description;
    @ServerTimestamp
    private Date creationDate;
    private List<Map<String, Double>> polygonPoints;

    public Project() {

    }

    public Project(String name, String description, List<Map<String, Double>> polygonPoints) {
        this.name = name;
        this.description = description;
        this.polygonPoints = polygonPoints;
    }

    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getCreationDate() { return creationDate; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }

    public List<Map<String, Double>> getPolygonPoints() { return polygonPoints; }
    public void setPolygonPoints(List<Map<String, Double>> polygonPoints) { this.polygonPoints = polygonPoints; }
}