package org.example.asteroides;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import org.example.asteroides.R;

public class PreferenciasFragment extends PreferenceFragment {

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);

        final EditTextPreference fragmentos = (EditTextPreference) findPreference(/*key*/getString(R.string.key_fragments));
        fragmentos.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int valor;
                try{
                    valor = Integer.parseInt((String)newValue);
                }catch (Exception ev ){
                    Toast.makeText(getActivity(), getString(R.string.Error_ET_pref), Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (valor>=0 && valor<=9) {
                    fragmentos.setSummary( getString(R.string.ms_NumAsteroides)+" ("+valor+")");
                    return true;
                }else {
                    Toast.makeText(getActivity(), "Máximo de fragmentos 9", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });
    }
}
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                