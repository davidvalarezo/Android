package org.dvalarez.toolbar;

import android.app.Application;
import android.content.SharedPreferences;

public class Aplicacion extends Application {
    private int saldo;

    @Override public void onCreate() {

        super.onCreate();
        SharedPreferences preferences = getSharedPreferences("preference", MODE_PRIVATE);
        saldo = preferences.getInt("saldo_inicial", -1);
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }
}
