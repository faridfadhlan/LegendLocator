package omfarid.com.legendlocator.wizard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.orm.SugarRecord;
import com.orm.SugarTransactionHelper;

import org.codepond.wizardroid.WizardFlow;
import org.codepond.wizardroid.WizardFragment;
import org.codepond.wizardroid.persistence.ContextVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import omfarid.com.legendlocator.R;
import omfarid.com.legendlocator.activities.MainActivity;
import omfarid.com.legendlocator.helpers.FaridHelpers;

/**
 * Created by farid on 1/8/2017.
 */

public class MyWizard extends WizardFragment implements View.OnClickListener {

    /**
     * Note that initially BasicWizardLayout inherits from {@link android.support.v4.app.Fragment} and therefore you must have an empty constructor
     */
    private HashMap<String, Integer> map_kab = new HashMap<>();
    List<String> kabs = new ArrayList<>();

    List<String> sql = new ArrayList<>();
    Button nextButton;


    public MyWizard() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        map_kab.put("SAMBAS", R.raw.kab_01);
        map_kab.put("BENGKAYANG", R.raw.kab_02);
        map_kab.put("LANDAK", R.raw.kab_03);
        map_kab.put("MEMPAWAH", R.raw.kab_04);
        map_kab.put("SANGGAU", R.raw.kab_05);
        map_kab.put("KETAPANG", R.raw.kab_06);
        map_kab.put("SINTANG", R.raw.kab_07);
        map_kab.put("KAPUAS HULU", R.raw.kab_08);
        map_kab.put("SEKADAU", R.raw.kab_09);
        map_kab.put("MELAWI", R.raw.kab_10);
        map_kab.put("KAYONG UTARA", R.raw.kab_11);
        map_kab.put("KUBU RAYA", R.raw.kab_12);
        map_kab.put("PONTIANAK", R.raw.kab_71);
        map_kab.put("SINGKAWANG", R.raw.kab_72);


        View wizardLayout = inflater.inflate(R.layout.wizard_container, container, false);
        nextButton = (Button) wizardLayout.findViewById(R.id.wizard_next_button);
        nextButton.setOnClickListener(this);
        //updateWizardControls();

        return wizardLayout;
    }


    //You must override this method and create a wizard flow by
    //using WizardFlow.Builder as shown in this example
    @Override
    public WizardFlow onSetup() {



        /* Optionally, you can set different labels for the control buttons
        setNextButtonLabel("Advance");
        setBackButtonLabel("Return");
        setFinishButtonLabel("Finalize"); */


        return new WizardFlow.Builder()
                .addStep(TutorialStep1.class)
                .create();
    }

    @Override
    public void onWizardComplete() {
        startActivity(new Intent(getActivity(), MainActivity.class));
        super.onWizardComplete();
        getActivity().finish();
    }

    @Override
    public void onStepChanged() {
        super.onStepChanged();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.wizard_next_button:
                String kab = FaridHelpers.getPreferenceByKey(getActivity(), "kab");

                if(kab == null) {
                    //Tell the wizard to go to next step
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Error")
                            .setMessage("Anda belum memilih kabupaten!")
                            .setIcon(android.R.drawable.ic_dialog_alert)

                            .setCancelable(false)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            })
                            .show();
                }

                else {
                    sql = FaridHelpers.importSQL(getActivity(), map_kab.get(kab));

                    //sp_kabupaten.setSelection(0);

                    //if(jumlah == 0) {
                    SugarTransactionHelper.doInTransaction(new SugarTransactionHelper.Callback() {
                        @Override
                        public void manipulateInTransaction() {
                            for (int i = 0; i < sql.size(); i++) {
                                Log.i("sigpodes", sql.get(i));
                                SugarRecord.executeQuery(sql.get(i));
                            }
                        }
                    });
                    onWizardComplete();
                }

                break;
        }
    }

    /**
     * Updates the UI according to current step position
     */
    private void updateWizardControls() {
        nextButton.setText(wizard.isLastStep()
                ? R.string.action_finish
                : R.string.action_next);
    }
}
