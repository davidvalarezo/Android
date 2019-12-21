package org.example.asteroides;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;


public class PreferenciasFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
    public static int valor = 3, numStorage;
    private ListPreference almacenamiento, storageExt;
    private static final int SOLICITUD_PERMISO_WRITE_EXTERNAL_STORAGE = 0;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);

        final EditTextPreference fragmentos = (EditTextPreference) findPreference(/*key*/"fragmentos");
        fragmentos.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //int valor;
                try{
                    valor = Integer.parseInt((String)newValue);
                }catch (Exception ev ){
                    Toast.makeText(getActivity(), getString(R.string.Error_ET_pref), Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (valor>=0 && valor<=9) {
                    fragmentos.setSummary( getString(R.string.summary_fragments)+" ("+valor+")");
                    return true;
                }else {
                    Toast.makeText(getActivity(),/*texto*/getString(R.string.max_numFragments), Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });
        fragmentos.setSummary(getString(R.string.summary_fragments)+" ("+valor+")");
        fragmentos.setText(""+valor);

        almacenamiento = (ListPreference) findPreference("storage");
        almacenamiento.setOnPreferenceChangeListener(this);
        almacenamiento.setDefaultValue("0");

        storageExt = (ListPreference) findPreference("extDirPath");
        storageExt.setOnPreferenceChangeListener(this);
    }

    public boolean almacenamientoEnMemoria(){
        //numStorage = Integer.parseInt((String)nValue);
        if (numStorage>=0 && numStorage<=13) {
            if (numStorage>=2 && numStorage<=4) {
                if(!hayPermisoAlmacenamientoExterno()){
                    solicitarPermiso(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Sin el permiso"+ " de almacenamiento externo no puede escribir o leer las puntuaciones en memoria.",
                            SOLICITUD_PERMISO_WRITE_EXTERNAL_STORAGE, getActivity());
                }
                return hayPermisoAlmacenamientoExterno();
            }
            return true;
        }else{
            return false;
        }
    }

    public static void solicitarPermiso(final String permiso, String justificacion, final int requestCode, final Activity actividad) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad, permiso)){
            new AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ActivityCompat.requestPermissions(actividad, new String[]{permiso}, requestCode); }}).show();

        }else {
            ActivityCompat.requestPermissions(actividad, new String[]{permiso}, requestCode);
        }
    }

    public boolean hayPermisoAlmacenamientoExterno() {
        return (ActivityCompat.checkSelfPermission(
                this.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED);
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SOLICITUD_PERMISO_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { almacenamientoEnMemoria();} else {
                Toast.makeText(getActivity(), "Sin el permiso, no puedo realizar la " + "acción", Toast.LENGTH_SHORT).show(); }
        }
    }*/

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        boolean res = false;
        switch (preference.getKey()){
            case "storage":
                numStorage = Integer.parseInt((String)newValue);
                res = almacenamientoEnMemoria();
                if(!res){
                    //preference.setDefaultValue("0");
                    res = hayPermisoAlmacenamientoExterno();
                }
                break;
            case "extDirPath":
                if(!hayPermisoAlmacenamientoExterno()){
                    solicitarPermiso(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            "Sin el permiso"+ " de almacenamiento externo no puede escribir o leer las puntuaciones en memoria externa.",
                            SOLICITUD_PERMISO_WRITE_EXTERNAL_STORAGE, getActivity());
                }
                res = hayPermisoAlmacenamientoExterno();
                break;
        }
        return res;
    }
    //File / New / Folder / Assets Folder:

}
