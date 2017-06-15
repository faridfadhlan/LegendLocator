package omfarid.com.legendlocator.fragments.legends;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import omfarid.com.legendlocator.R;
import omfarid.com.legendlocator.activities.DetailLegendActivity;
import omfarid.com.legendlocator.activities.FormLegendActivity;
import omfarid.com.legendlocator.adapters.LegendaAdapter;
import omfarid.com.legendlocator.helpers.FaridHelpers;
import omfarid.com.legendlocator.models.Foto;
import omfarid.com.legendlocator.models.Legenda;
import omfarid.com.legendlocator.models.Status;

/**
 * Created by farid on 12/2/2016.
 */

public class LegendSavedFragment extends Fragment {

    List<Legenda> legendas;
    Legenda legenda;
    LegendaAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.saved_legend_fragment, container, false);
        //TextView tvTitle = (TextView) view.findViewById(R.id.wf_tv);
        //tvTitle.setText("Fragment Waiting");
        legendas = SugarRecord.find(Legenda.class, "KODESTATUS = ?", new String[]{Status.TERSIMPAN}, null, "WAKTU DESC", null);
        adapter = new LegendaAdapter(getActivity(), legendas);

        ListView lv_saved = (ListView)view.findViewById(R.id.lv_saved);
        lv_saved.setAdapter(adapter);

        lv_saved.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), DetailLegendActivity.class);
                intent.putExtra("id", legendas.get(position).getId().toString());
                //Log.i("dd", legends.get(position).getId().toString());
                startActivity(intent);
            }
        });

        registerForContextMenu(lv_saved);
        try {
            resizeImages();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return view;
    }

    public void resizeImages() throws IOException {
        for(int i=0;i<legendas.size();i++) {
            for(int j=0;j<legendas.get(i).getFotos().size();j++) {
                File file = new File(legendas.get(i).getFotos().get(j).paththum);
                if(file.length() > (1024*600)){
                    File f_awal = new File(legendas.get(i).getFotos().get(j).paththum);
                    File f_akhir = f_awal;
                    Foto f = legendas.get(i).getFotos().get(j);
                    if(!f.paththum.contains("LegendLocator")) {
                        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "LegendLocator");
                        if (!dir.exists()){
                            dir.mkdirs();
                        }
                        String nama = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        f_akhir = new File(dir,  nama + "-" + j +".jpg");
                        f.paththum = f_akhir.getAbsolutePath();
                        SugarRecord.save(f);
                    }
                    FaridHelpers.resize(getActivity(), Uri.fromFile(f_awal), f_akhir);



                }

            }

            //legendas.get(i).getFotos().get(0).paththum =
            //SugarRecord.save(legendas.get(i));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        legendas.clear();
        legendas.addAll(SugarRecord.find(Legenda.class, "KODESTATUS = ?", new String[]{Status.TERSIMPAN}, null, "WAKTU DESC", null));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lv_saved) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            //Legenda legenda = (Legenda) lv.getItemAtPosition(acmi.position);
            menu.add(1, (int) acmi.id, 100, "Edit");
            menu.add(1, (int) acmi.id, 100, "Hapus");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        legenda = legendas.get(item.getItemId());
        CharSequence title = item.getTitle();
        if(title.equals("Edit") && item.getGroupId() == 1) {
            Intent intent = new Intent(getActivity(), FormLegendActivity.class);
            intent.putExtra("action", FormLegendActivity.ACTION_EDIT);
            intent.putExtra("id", legenda.getId().toString());
            startActivity(intent);
        }

        if(title.equals("Hapus") && item.getGroupId() == 1) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Anda yakin akan menghapus "+legenda.nama+"?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            SugarRecord.deleteInTx(legenda.getFotos());
                            SugarRecord.delete(legenda);
                            legendas.clear();
                            legendas.addAll(SugarRecord.find(Legenda.class, "KODESTATUS = ?", new String[]{Status.TERSIMPAN}, null, "WAKTU DESC", null));
                            adapter.notifyDataSetChanged();
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }

        return super.onContextItemSelected(item);
    }
}
