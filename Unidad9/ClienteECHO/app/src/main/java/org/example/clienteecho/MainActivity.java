package org.example.clienteecho;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        output = findViewById(R.id.TextView01);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitNetwork().build());
        ejecutaCliente();
    }

    private void ejecutaCliente() {
        //String ip = "158.42.146.127";
        String ip = "192.168.0.20";
        int puerto = 7;
        log(" socket " + ip + " " + puerto);
        try {
            Socket sk = new Socket(ip, puerto);
            BufferedReader entrada = new BufferedReader( new InputStreamReader(sk.getInputStream()));
            PrintWriter salida = new PrintWriter( new OutputStreamWriter(sk.getOutputStream()), true);
            log("enviando... Hola Mundo ");
            salida.println("Hola Mundo");
            log("recibiendo ... " + entrada.readLine());
            sk.close();
        } catch (Exception e) {
            log("error: " + e.toString());
        }
    }

    private void log(String string) {
        output.append(string + "\n");
    }
}
