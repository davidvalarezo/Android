package org.dvalarez.valarezod_u2;

import android.app.Activity;
import android.os.Bundle;

public class PreferenciasActivity extends Activity {

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PreferenciasFragment())
                .commit();
    }
}
