package org.dvalarez.valarezod_u2;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import org.dvalarez.valarezod_u2.R;

public class PreferenciasFragment extends PreferenceFragment {
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}