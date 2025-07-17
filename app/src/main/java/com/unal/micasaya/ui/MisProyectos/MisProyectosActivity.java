package com.unal.micasaya.ui.MisProyectos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.unal.micasaya.R;
import com.unal.micasaya.ui.Resultados.Project;
import com.unal.micasaya.ui.Map.MapaActivity;

import java.util.ArrayList;
import java.util.List;

public class MisProyectosActivity extends AppCompatActivity {
    private static final String TAG = "MisProyectosActivity";

    private RecyclerView recyclerViewProjects;
    private TextView emptyProjectsMessage;
    private Button buttonCreateProjectFromEmpty;
    private ProjectFirestoreAdapter projectAdapter; // Cambia a un adaptador que use List<Project>
    private List<Project> projectsList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_proyectos);

        recyclerViewProjects = findViewById(R.id.recyclerViewProjects);
        emptyProjectsMessage = findViewById(R.id.emptyProjectsMessage);
        buttonCreateProjectFromEmpty = findViewById(R.id.buttonCreateProjectFromEmpty);

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Configurar RecyclerView
        recyclerViewProjects.setLayoutManager(new LinearLayoutManager(this));
        projectAdapter = new ProjectFirestoreAdapter(projectsList); // Asume que tienes este adaptador
        recyclerViewProjects.setAdapter(projectAdapter);

        buttonCreateProjectFromEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MisProyectosActivity.this, MapaActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProjectsFromFirestore(); // Cargar proyectos cuando la actividad se resume
    }

    private void loadProjectsFromFirestore() {
        db.collection("projects") // Nombre de tu colección
                // .orderBy("creationDate", Query.Direction.DESCENDING) // Opcional: ordenar
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            projectsList.clear(); // Limpiar lista anterior
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Project project = document.toObject(Project.class);
                                project.setDocumentId(document.getId()); // Guardar el ID del documento
                                projectsList.add(project);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                            projectAdapter.notifyDataSetChanged(); // Notificar al adaptador
                            checkProjectsAndDisplay();
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                            Toast.makeText(MisProyectosActivity.this, "Error al cargar proyectos.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkProjectsAndDisplay() {
        if (projectsList.isEmpty()) {
            recyclerViewProjects.setVisibility(View.GONE);
            emptyProjectsMessage.setVisibility(View.VISIBLE);
            buttonCreateProjectFromEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerViewProjects.setVisibility(View.VISIBLE);
            emptyProjectsMessage.setVisibility(View.GONE);
            buttonCreateProjectFromEmpty.setVisibility(View.GONE);
        }
    }
}