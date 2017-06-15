package omfarid.com.legendlocator.wizard;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.orm.SugarRecord;
import com.orm.SugarTransactionHelper;

import org.codepond.wizardroid.WizardStep;
import org.codepond.wizardroid.persistence.ContextVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import omfarid.com.legendlocator.R;
import omfarid.com.legendlocator.helpers.FaridHelpers;

/**
 * Created by farid on 1/8/2017.
 */

public class TutorialStep1 extends WizardStep {

    @ContextVariable
    private String kab;

    MaterialSpinner sp_kabupaten;
    List<String> kabs = new ArrayList<>();
    List<String> sql = new ArrayList<>();
    int pos;


    //Wire the layout to the step
    public TutorialStep1() {

    }



    public void setKab(String kab) {
        this.kab = kab;
    }

    //Set your layout here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.step_tutorial, container, false);
        TextView wizard_text = (TextView) v.findViewById(R.id.wizard_text);
        wizard_text.setText("Untuk instalasi pertama kali, Anda harus memilih kabupaten wilayah kerja terlebih dahulu");
        sp_kabupaten = (MaterialSpinner) v.findViewById(R.id.sp_kabupaten);

        kabs.add("SAMBAS");
        kabs.add("BENGKAYANG");
        kabs.add("LANDAK");
        kabs.add("MEMPAWAH");
        kabs.add("SANGGAU");
        kabs.add("KETAPANG");
        kabs.add("SINTANG");
        kabs.add("KAPUAS HULU");
        kabs.add("SEKADAU");
        kabs.add("MELAWI");
        kabs.add("KAYONG UTARA");
        kabs.add("KUBU RAYA");
        kabs.add("PONTIANAK");
        kabs.add("SINGKAWANG");

        ArrayAdapter<String> kabupatenadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, kabs);
        kabupatenadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp_kabupaten.setAdapter(kabupatenadapter);

        sp_kabupaten.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>=0) FaridHelpers.setPreference(getActivity(), "kab", kabs.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }



}
