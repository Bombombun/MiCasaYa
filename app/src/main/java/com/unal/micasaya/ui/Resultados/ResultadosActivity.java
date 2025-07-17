package com.unal.micasaya.ui.Resultados;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.EditText;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unal.micasaya.R;
import com.unal.micasaya.ui.MisProyectos.MisProyectosActivity;
import com.unal.micasaya.ui.calculadora.GeminiHelper;
import com.unal.micasaya.ui.calculadora.view.PlanoEstructuralView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResultadosActivity extends AppCompatActivity {

    private static final String TAG = "ResultadosActivity";

    private TextView textViewRecommendations;
    private ImageView imageViewFloorPlan;
    private Button buttonSaveProject;
    private Button btnCalcularEstructura;

    private FirebaseFirestore db;
    private ArrayList<LatLng> polygonPoints;
    private String soilType;
    private int numFloors;
    private String seismicRisk;
    private String buildingUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_resultados);

        textViewRecommendations = findViewById(R.id.textViewRecommendations);
        imageViewFloorPlan = findViewById(R.id.imageViewFloorPlan);
        buttonSaveProject = findViewById(R.id.buttonSaveProject);

        getProjectDataFromIntent();
        generateRecommendationsWithGemini();
        generateFloorPlanWithAI();

        buttonSaveProject.setOnClickListener(v -> {
            String projectName = "Proyecto " + System.currentTimeMillis();
            String description = "Descripción para " + projectName;
            if (buildingUse != null && !buildingUse.isEmpty()) {
                description += " - Uso: " + buildingUse;
            }
            saveProjectToFirestore(projectName, description);
        });
    }

    private void getProjectDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            polygonPoints = intent.getParcelableArrayListExtra("polygonPoints");
            soilType = intent.getStringExtra("soilType");
            numFloors = intent.getIntExtra("numFloors", 0);
            seismicRisk = intent.getStringExtra("seismicRisk");
            buildingUse = intent.getStringExtra("buildingUse");
        }
    }

    private void generateRecommendationsWithGemini() {
        textViewRecommendations.setText("Generando recomendaciones...");

        // Parámetros de ejemplo, podrías calcularlos con base en el polígono u otra lógica
        double largo = 10.0;
        double ancho = 8.0;
        int habitaciones = 3;

        new Thread(() -> {
            String respuestaIA = GeminiHelper.generarRespuestaIA(largo, ancho, habitaciones, numFloors);
            runOnUiThread(() -> textViewRecommendations.setText(respuestaIA));
        }).start();
    }

    private void generateFloorPlanWithAI() {
        // Imagen ficticia como ejemplo (puedes cambiar por generación real)
        String base64Image = ""; // Aquí deberías colocar una cadena base64 válida si quieres una imagen
        if (!base64Image.isEmpty()) {
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageViewFloorPlan.setImageBitmap(decodedByte);
        } else {
            imageViewFloorPlan.setImageResource(R.drawable.placeholder_plan);
        }
        Toast.makeText(this, "Plano en planta simulado.", Toast.LENGTH_SHORT).show();
    }

    private void calcularEstructura() {
        EditText etLargo = findViewById(R.id.etLargo);
        EditText etAncho = findViewById(R.id.etAncho);
        EditText etPisos = findViewById(R.id.etPisos);

        double largo = Double.parseDouble(etLargo.getText().toString());
        double ancho = Double.parseDouble(etAncho.getText().toString());
        int numFloors = Integer.parseInt(etPisos.getText().toString());

        int columnasFila = (int) (ancho / 3) + 1;
        int columnasColumna = (int) (largo / 3) + 1;
        int totalColumnas = columnasFila * columnasColumna;
        int totalVigas = (columnasFila - 1) * columnasColumna + (columnasColumna - 1) * columnasFila;
        String cimentacion = (numFloors > 2) ? "Zapata corrida" : "Zapata aislada";

        String resultado = getString(R.string.label_columnas, totalColumnas) + "\n" +
                getString(R.string.label_vigas, totalVigas) + "\n" +
                getString(R.string.label_cimentacion, cimentacion);

        Toast.makeText(this, resultado, Toast.LENGTH_LONG).show();

        // Agrega visualización del plano estructural si quieres (opcional)
        PlanoEstructuralView planoView = new PlanoEstructuralView(this, columnasFila, columnasColumna);
        setContentView(planoView);
    }

    private void saveProjectToFirestore(String projectName, String projectDescription) {
        if (projectName == null || projectName.trim().isEmpty()) {
            Toast.makeText(this, "El nombre del proyecto no puede estar vacío.", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Map<String, Double>> firestorePoints = new ArrayList<>();
        if (polygonPoints != null) {
            for (LatLng point : polygonPoints) {
                Map<String, Double> pointMap = new HashMap<>();
                pointMap.put("latitude", point.latitude);
                pointMap.put("longitude", point.longitude);
                firestorePoints.add(pointMap);
            }
        }

        Project newProject = new Project(projectName, projectDescription, firestorePoints);

        db.collection("projects")
                .add(newProject)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Proyecto guardado con ID: " + documentReference.getId());
                    Toast.makeText(this, "Proyecto guardado correctamente.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MisProyectosActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al guardar proyecto", e);
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();



                });
    }
}
