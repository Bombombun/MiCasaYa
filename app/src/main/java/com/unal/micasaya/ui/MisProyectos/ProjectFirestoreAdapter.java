package com.unal.micasaya.ui.MisProyectos; // Or whatever package you want to put it in

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView; // Example: if your project item layout has a TextView
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.unal.micasaya.R; // For your layout resources
import com.unal.micasaya.ui.Resultados.Project; // Assuming your Project model is here
import java.util.List;

public class ProjectFirestoreAdapter extends RecyclerView.Adapter<ProjectFirestoreAdapter.ProjectViewHolder> {

    private List<Project> projectList;

    public ProjectFirestoreAdapter(List<Project> projectList) {
        this.projectList = projectList;
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate your item layout for the RecyclerView
        // Make sure you have a layout file e.g., R.layout.item_project
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_project, parent, false); // Replace R.layout.item_project with your actual item layout
        return new ProjectViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project project = projectList.get(position);
        // Bind data to your ViewHolder's views
        // Example:
        // holder.projectNameTextView.setText(project.getName());
        // holder.projectDescriptionTextView.setText(project.getDescription());
        // ... and so on for other fields in your Project class and views in your item_project.xml
    }

    @Override
    public int getItemCount() {
        return projectList.size();
    }

    static class ProjectViewHolder extends RecyclerView.ViewHolder {
        // Declare the views in your item layout
        // Example:
        // TextView projectNameTextView;
        // TextView projectDescriptionTextView;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize the views
            // Example:
            // projectNameTextView = itemView.findViewById(R.id.projectNameTextView); // Replace with actual ID
            // projectDescriptionTextView = itemView.findViewById(R.id.projectDescriptionTextView); // Replace with actual ID
        }
    }
}
