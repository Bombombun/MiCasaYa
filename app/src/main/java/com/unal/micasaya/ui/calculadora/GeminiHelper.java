package com.unal.micasaya.ui.calculadora;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GeminiHelper {

    private static final String TAG = "GeminiHelper";
    private static final String API_KEY = "AIzaSyDDalR_XIhD9C0W4eCKLRprA8KaBV-1RDE";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + API_KEY;

    public static String generarRespuestaIA(double largo, double ancho, int habitaciones, int pisos) {
        try {
            Thread.sleep(2000);

            String prompt = "Dame una recomendación estructural para una casa de " +
                    largo + " metros de largo, " +
                    ancho + " metros de ancho, con " +
                    habitaciones + " habitaciones y " +
                    pisos + " pisos. Sé preciso y claro.";

            OkHttpClient client = new OkHttpClient();

            JSONObject part = new JSONObject();
            part.put("text", prompt);
            JSONArray parts = new JSONArray();
            parts.put(part);

            JSONObject content = new JSONObject();
            content.put("parts", parts);
            JSONArray contents = new JSONArray();
            contents.put(content);

            JSONObject requestJson = new JSONObject();
            requestJson.put("contents", contents);

            RequestBody body = RequestBody.create(
                    requestJson.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(body)
                    .build();

            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();

            if (!response.isSuccessful() || responseBody == null) {
                Log.e(TAG, "Error en la respuesta: " + response.code());
                return respuestaGenerica();
            }

            String responseText = responseBody.string();
            JSONObject json = new JSONObject(responseText);
            JSONArray candidates = json.optJSONArray("candidates");

            if (candidates != null && candidates.length() > 0) {
                JSONObject firstCandidate = candidates.getJSONObject(0);
                JSONObject contentObj = firstCandidate.getJSONObject("content");
                JSONArray partsArray = contentObj.getJSONArray("parts");
                return partsArray.getJSONObject(0).getString("text");
            } else {
                return respuestaGenerica();
            }

        } catch (InterruptedException e) {
            Log.e(TAG, "Error en sleep: ", e);
            return respuestaGenerica();
        } catch (IOException e) {
            Log.e(TAG, "Error de red: ", e);
            return respuestaGenerica();
        } catch (Exception e) {
            Log.e(TAG, "Error inesperado: ", e);
            return respuestaGenerica();
        }
    }

    private static String respuestaGenerica() {
        return "Análisis estructural preliminar:\n" +
                "- Total estimado de columnas: 12\n" +
                "- Total estimado de vigas: 18\n" +
                "- Tipo recomendado de cimentación: Zapatas aisladas\n\n" +
                "Recomendaciones:\n" +
                "1. Verificar la capacidad portante del suelo mediante un estudio geotécnico.\n" +
                "2. Evaluar las cargas vivas y muertas de la estructura según la norma NSR-10.\n" +
                "3. Asegurar una correcta conexión entre columnas y vigas para evitar fallas por corte.\n" +
                "4. Implementar juntas de dilatación si la longitud de la edificación supera los 30 metros.\n";
    }
}
