package com.example.marceblockdenotas;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText etTarea;
    Button btnAgregar;
    ListView lvTareas;

    ArrayList<String> tareas = new ArrayList<>();
    ArrayAdapter<String> adaptador;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTarea = findViewById(R.id.etTarea);
        btnAgregar = findViewById(R.id.btnAgregar);
        lvTareas = findViewById(R.id.lvTareas);

        prefs = getSharedPreferences("misDatos", MODE_PRIVATE);

        cargarTareas();

        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tareas);
        lvTareas.setAdapter(adaptador);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = etTarea.getText().toString().trim();

                if (texto.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Escribe algo primero", Toast.LENGTH_SHORT).show();
                    return;
                }

                tareas.add(texto);
                etTarea.setText("");
                adaptador.notifyDataSetChanged();
                guardarTareas();
            }
        });

        lvTareas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tareas.remove(position);
                adaptador.notifyDataSetChanged();
                guardarTareas();
                Toast.makeText(MainActivity.this, "Tarea borrada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void guardarTareas() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tareas.size(); i++) {
            if (i > 0) sb.append("||");
            sb.append(tareas.get(i));
        }
        prefs.edit().putString("tareas", sb.toString()).apply();
    }

    private void cargarTareas() {
        String guardado = prefs.getString("tareas", "");
        if (!guardado.isEmpty()) {
            String[] partes = guardado.split("\\|\\|");
            for (String t : partes) {
                tareas.add(t);
            }
        }
    }
}