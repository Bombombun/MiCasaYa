package com.unal.micasaya.ui.calculadora;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.unal.micasaya.R;
import android.widget.Toast;
import com.unal.micasaya.ui.calculadora.view.PlanoEstructuralView;


public class CalculadoraActivity extends AppCompatActivity {

    private static final String TAG = "CalculadoraActivity";

    EditText etLargo, etAncho, etHabitaciones, etPisos;
    Button btnCalcular;
    TextView tvResultado;
    FrameLayout contenedorPlano;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora); // Paso 2: crearemos este layout

        etLargo = findViewById(R.id.etLargo);
        etAncho = findViewById(R.id.etAncho);
        etHabitaciones = findViewById(R.id.etHabitaciones);
        etPisos = findViewById(R.id.etPisos);
        btnCalcular = findViewById(R.id.btnCalcular);
        tvResultado = findViewById(R.id.tvResultado);
        contenedorPlano = findViewById(R.id.contenedorPlano);

        btnCalcular.setOnClickListener(view -> {
            Log.d("CALCULADORA", "Botón presionado");
            Toast.makeText(this, "Botón presionado", Toast.LENGTH_SHORT).show();
            calcularEstructura();
        });

    }

    private void calcularEstructura() {
        String largoStr = etLargo.getText().toString().trim();
        String anchoStr = etAncho.getText().toString().trim();
        String habitacionesStr = etHabitaciones.getText().toString().trim();
        String pisosStr = etPisos.getText().toString().trim();

        if (largoStr.isEmpty() || anchoStr.isEmpty() || habitacionesStr.isEmpty() || pisosStr.isEmpty()) {
            tvResultado.setText(getString(R.string.error_campos_incompletos));
            contenedorPlano.removeAllViews();
            return;
        }

        try {
            double largo = Double.parseDouble(largoStr);
            double ancho = Double.parseDouble(anchoStr);
            int habitaciones = Integer.parseInt(habitacionesStr);
            int pisos = Integer.parseInt(pisosStr);

            if (largo <= 0 || ancho <= 0 || habitaciones <= 0 || pisos <= 0) {
                tvResultado.setText(getString(R.string.error_valores_positivos));
                contenedorPlano.removeAllViews();
                return;
            }

            int columnasFila = (int) (largo / 4) + 1;
            int columnasColumna = (int) (ancho / 4) + 1;
            int totalColumnas = columnasFila * columnasColumna;

            int vigasHorizontales = (columnasFila - 1) * columnasColumna;
            int vigasVerticales = (columnasColumna - 1) * columnasFila;
            int totalVigas = vigasHorizontales + vigasVerticales;

            String cimentacion;
            if (pisos <= 2) {
                cimentacion = "Zapatas aisladas";
            } else if (pisos <= 4) {
                cimentacion = "Zapatas corridas o losa de cimentación";
            } else {
                cimentacion = "Losa de cimentación o pilotes (requiere estudio especializado)";
            }

            String resultado = getString(R.string.label_columnas) + ": " + totalColumnas + "\n" +
                    getString(R.string.label_vigas) + ": " + totalVigas + "\n" +
                    getString(R.string.label_cimentacion) + ": " + cimentacion;

            tvResultado.setText(resultado);

            // 🔹 Simulación de IA (opcional)
            String respuestaIA = GeminiHelper.generarRespuestaIA(largo, ancho, habitaciones, pisos);
            tvResultado.append("\n\nRecomendación IA:\n" + respuestaIA);

            // 🔹 Mostrar plano estructural
            contenedorPlano.removeAllViews();
            PlanoEstructuralView plano = new PlanoEstructuralView(this, columnasFila, columnasColumna);
            contenedorPlano.addView(plano);

        } catch (NumberFormatException e) {
            tvResultado.setText(getString(R.string.error_numeros_invalidos));
            contenedorPlano.removeAllViews();
            Log.e(TAG, "Error de formato numérico: ", e);
        } catch (Exception e) {
            tvResultado.setText(getString(R.string.error_inesperado));
            contenedorPlano.removeAllViews();
            Log.e(TAG, "Error inesperado en calcularEstructura: ", e);
        }
    }
}
