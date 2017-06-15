package omfarid.com.legendlocator.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.orm.SugarRecord;

import net.gotev.uploadservice.Logger;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import omfarid.com.legendlocator.R;
import omfarid.com.legendlocator.fragments.legends.LegendFragment;
import omfarid.com.legendlocator.helpers.FaridHelpers;
import omfarid.com.legendlocator.models.Legenda;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        LegendFragment.OnFragmentInteractionListener,
        UploadStatusDelegate,
        Callback
{
    Fragment fragment = new Fragment();
    NavigationView navigationView;
    String serverUrlString = "http://kalbar.bps.go.id/sigpodes/legenda/upload2";
    HashMap<String, Legenda> map = new HashMap<>();
    List<Legenda> legenda_upload = new ArrayList<>();
    private final OkHttpClient client = new OkHttpClient();
    HashMap<String, String> paramsync = new HashMap<>();
    //List<Legenda> legendas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FormLegendActivity.class);
                intent.putExtra("action", FormLegendActivity.ACTION_NEW);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_legenda);
        selectItem(R.id.nav_legenda);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.legendmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sync) {
            try {
                run();
            } catch (Exception e) {
                Log.e("sigpodes", e.toString());
                //e.printStackTrace();
            }
            legenda_upload = SugarRecord.find(Legenda.class, "KODESTATUS = ?", "1");
            if(isValid(legenda_upload)) {
                start();
            }
            else {
                //FaridHelpers.showAlertDialog(this, "Error", "Tidak ada legenda yang siap diupload.");


            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void run() throws Exception {
        List<Legenda> lg = SugarRecord.find(Legenda.class, "KODESTATUS <> '1'");
        //String idservers[] = new String[lg.size()];
        for(int i=0;i<lg.size();i++) {
            paramsync.put("id["+i+"]", lg.get(i).idserver);
        }

        FormBody.Builder builder = new FormBody.Builder();

        //for(int i=0;i<params.size();i++)
        for (Map.Entry<String,String> entry : paramsync.entrySet()) {
            //Log.i("sigpodes", "key = "+entry.getKey()+", value = "+entry.getValue());
            builder.add(entry.getKey(), entry.getValue());
        }

        Log.i("sigpodes", "Started");

        Request request = new Request.Builder()
                .url("http://kalbar.bps.go.id/sigpodes/legenda/synchronize")
                .post(builder.build())
                .build();

        client.newCall(request).enqueue(this);

    }

    private boolean isValid(List<Legenda> legendas) {
        int i = 0;
        for(int j=0;j<legendas.size();j++) {
            if(legendas.get(j).getFotos().size() > 0) {
                i++;
            }
        }
        if(i > 0) return true;
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        selectItem(id);

        return true;
    }

    public void selectItem(int item) {


        switch (item) {
            case R.id.nav_legenda:
                fragment = new LegendFragment();
                break;
            case R.id.nav_share:

                break;
            default:
                fragment = new LegendFragment();
                break;
        }

        if(fragment!=null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private UploadNotificationConfig getNotificationConfig(String filename) {
        return new UploadNotificationConfig()
                .setTitle(filename)
                .setInProgressMessage("Uploading")
                .setCompletedMessage("Berhasil diupload")
                .setErrorMessage("Gagal diupload")
                .setClickIntent(new Intent(this, MainActivity.class))
                .setClearOnAction(true)
                .setRingToneEnabled(true);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void upload() {
        for (int i=0;i<legenda_upload.size();i++) {
            if(legenda_upload.get(i).getFotos().size()>0) {
                try {
                    //final String filename = getFilename(fileToUploadPath);
                    Logger.setLogLevel(Logger.LogLevel.DEBUG);

                    MultipartUploadRequest req = new MultipartUploadRequest(this, serverUrlString);
                    for(int j=0;j<legenda_upload.get(i).getFotos().size();j++) {
                        req.addFileToUpload(legenda_upload.get(i).getFotos().get(j).paththum, "foto[]");
                    }

                    String uploadId = req
                            .addParameter("kode_prop", legenda_upload.get(i).kodeprop)
                            .addParameter("kode_kabkota", legenda_upload.get(i).kodekab)
                            .addParameter("kode_kec", legenda_upload.get(i).kodekec)
                            .addParameter("kode_desa", legenda_upload.get(i).kodedesa)
                            .addParameter("id_jenis", String.valueOf(Integer.parseInt(legenda_upload.get(i).kodekategori)))
                            .addParameter("nama", legenda_upload.get(i).nama.trim())
                            .addParameter("deskripsi", legenda_upload.get(i).deskripsi)
                            .addParameter("latlong", legenda_upload.get(i).longitude + "," + legenda_upload.get(i).latitude)
                            .setNotificationConfig(getNotificationConfig(legenda_upload.get(i).nama.trim()))
                            .setMaxRetries(3)
                            .setDelegate(this)
                            .startUpload();

                    map.put(uploadId, legenda_upload.get(i));

                    //addUploadToList(uploadID, filename);

                    // these are the different exceptions that may be thrown
                } catch (FileNotFoundException exc) {
                    showToast(exc.getMessage());
                } catch (IllegalArgumentException exc) {
                    showToast("Missing some arguments. " + exc.getMessage());
                } catch (MalformedURLException exc) {
                    showToast(exc.getMessage());
                }
            }
        }
    }

    private static final int MY_PERMISSION_READ_EXTERNAL_STORAGE = 2;
    private void checkReadPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            new AlertDialog.Builder(this)
                    .setTitle("Izin Membaca File")
                    .setMessage("Legend Locator membutuhkan izin untuk mengakses gambar dari penyimpanan.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    MY_PERMISSION_READ_EXTERNAL_STORAGE );
                        }
                    })
                    .create()
                    .show();


        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSION_READ_EXTERNAL_STORAGE );
        }

    }

    public void start() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                checkReadPermission();
            }
            else {
                upload();
            }
        }
        else {
            upload();
        }

    }

    @Override
    public void onProgress(UploadInfo uploadInfo) {

    }

    @Override
    public void onError(UploadInfo uploadInfo, Exception exception) {

    }

    @Override
    public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
        Log.i("sigpodes", serverResponse.getBodyAsString());
        try {
            JSONObject result = new JSONObject(serverResponse.getBodyAsString());
            if(result.getString("status").equals("sukses")) {
                Legenda legenda = map.get(uploadInfo.getUploadId());
                legenda.kodestatus = "2";
                legenda.idserver = result.getString("id_server");
                SugarRecord.save(legenda);
            }
            else {
                FaridHelpers.showAlertDialog(this, "Error", "Gagal Upload");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        selectItem(R.id.nav_legenda);
    }

    @Override
    public void onCancelled(UploadInfo uploadInfo) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        upload();


                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String result = response.body().string();
        Log.i("sigpodes", "Result = "+result);
        try {
            JSONArray ja = new JSONArray(result);
            for(int i=0;i<ja.length();i++) {
                JSONObject lg = ja.getJSONObject(i);
                Log.i("sigpodes", "ID = "+lg.getString("id")+", Status = "+lg.getString("status"));
                Legenda mylg = SugarRecord.find(Legenda.class, "IDSERVER = ?", lg.getString("id")).get(0);
                if(!mylg.kodestatus.equals(lg.getString("status"))) {
                    mylg.kodestatus = lg.getString("status");
                    SugarRecord.save(mylg);
                }
            }
            //selectItem(R.id.nav_legenda);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                selectItem(R.id.nav_legenda);
            }
        });
    }
}
