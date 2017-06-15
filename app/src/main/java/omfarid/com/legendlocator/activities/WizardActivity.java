package omfarid.com.legendlocator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.orm.SugarRecord;
import com.orm.SugarTransactionHelper;

import java.util.List;

import omfarid.com.legendlocator.R;
import omfarid.com.legendlocator.models.Propinsi;

/**
 * Created by farid on 1/7/2017.
 */

public class WizardActivity extends FragmentActivity {

    //public List<String> sql;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        long jumlah = SugarRecord.count(Propinsi.class);
        Log.i("sigpodes", "Jumlah propinsi = "+jumlah+"");
        if(jumlah>0) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);
    }
}