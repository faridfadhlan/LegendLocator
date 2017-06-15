package omfarid.com.legendlocator.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aviadmini.quickimagepick.PickCallback;
import com.aviadmini.quickimagepick.PickSource;
import com.aviadmini.quickimagepick.QiPick;
import com.aviadmini.quickimagepick.UriUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.orm.SugarRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import omfarid.com.legendlocator.FileHelper;
import omfarid.com.legendlocator.R;
import omfarid.com.legendlocator.adapters.FotoAdapter;
import omfarid.com.legendlocator.entities.PhotosChoosen;
import omfarid.com.legendlocator.helpers.FaridHelpers;
import omfarid.com.legendlocator.models.Desa;
import omfarid.com.legendlocator.models.Foto;
import omfarid.com.legendlocator.models.Kategori;
import omfarid.com.legendlocator.models.Kecamatan;
import omfarid.com.legendlocator.models.Legenda;
import omfarid.com.legendlocator.models.Status;

public class FormLegendActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerDragListener,
        LocationListener, PickCallback, GoogleMap.OnMapLongClickListener {

    private Uri imageresultUri;
    private PickSource pickSource;
    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    MarkerOptions markerOptions;
    SupportMapFragment mapFrag;
    MaterialSpinner sp_kecamatan, sp_desa, sp_kategori;
    EditText nama;
    EditText deskripsi;
    List<PhotosChoosen> fotos = new ArrayList<PhotosChoosen>();
    RecyclerView horizontal_recycler_view;
    FotoAdapter horizontalAdapter;
    Legenda legenda;
    public static final int ACTION_NEW = 1;
    public static final int ACTION_EDIT = 2;
    int formStatus;
    List<String> errors = new ArrayList<>();

    public static final int MY_PERMISSION_REQUEST_LOCATION = 1;
    public static final int MY_PERMISSION_WRITE_EXTERNAL_STORAGE = 2;
    public static final int MY_PERMISSION_READ_EXTERNAL_STORAGE = 3;

    private List<Kecamatan> kecamatans;
    private List<Desa> desas;
    private List<Foto> fotofoto;
    private List<Kategori> kategoris;
    private boolean init = false;
    private static final String LEGEND_DIR_NAME = "LegendLocator";
    private File outDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), LEGEND_DIR_NAME);
    ArrayAdapter<Desa> desaspinneradapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_legend);



        kecamatans = SugarRecord.listAll(Kecamatan.class);
        desas = new ArrayList<Desa>();
        kategoris = SugarRecord.listAll(Kategori.class);

        sp_kecamatan = (MaterialSpinner) findViewById(R.id.sp_kecamatan);
        sp_desa = (MaterialSpinner)findViewById(R.id.sp_desa);
        sp_kategori = (MaterialSpinner)findViewById(R.id.sp_kategori);

        nama = (EditText)findViewById(R.id.txt_nama);
        deskripsi = (EditText)findViewById(R.id.txt_deskripsi);

        // Creating adapter for spinner
        ArrayAdapter<Kategori> kategorispinneradapter = new ArrayAdapter<Kategori>(this, android.R.layout.simple_spinner_item, kategoris);
        kategorispinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<Kecamatan> kecamatanspinneradapter = new ArrayAdapter<Kecamatan>(this, android.R.layout.simple_spinner_item, kecamatans);
        kecamatanspinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        desaspinneradapter = new ArrayAdapter<Desa>(this, android.R.layout.simple_spinner_item, desas);
        desaspinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sp_kecamatan.setAdapter(kecamatanspinneradapter);
        sp_desa.setAdapter(desaspinneradapter);
        sp_kategori.setAdapter(kategorispinneradapter);



        switch(getIntent().getIntExtra("action", 1)) {
            case ACTION_NEW:
                legenda = new Legenda();
                formStatus = ACTION_NEW;
                break;
            case ACTION_EDIT:
                init = true;
                formStatus = ACTION_EDIT;
                legenda = SugarRecord.find(Legenda.class, "id = ?", getIntent().getStringExtra("id")).get(0);
                fotos = legenda.getPhotosChoosen();
                fotofoto = legenda.getFotos();
                mLastLocation = new Location("");
                mLastLocation.setLatitude(legenda.latitude);
                mLastLocation.setLongitude(legenda.longitude);
                int posi = getpositionkecamatan(kecamatans, legenda.getDesa().getKecamatan())+1;
                sp_kecamatan.setTag(posi);
                sp_kecamatan.setSelection(posi);
                sp_kategori.setSelection(getpositionkategori(kategoris, legenda.getKategori())+1);
                break;
        }

        if(savedInstanceState!=null) {
            sp_kategori.setSelection(savedInstanceState.getInt("kat"));
            sp_kecamatan.setTag(savedInstanceState.getInt("kec"));
            sp_kecamatan.setSelection(savedInstanceState.getInt("kec"));
            String[] statefotos = savedInstanceState.getStringArray("fotos");
            for(int i=0;i<statefotos.length;i++) {
                fotos.add(new PhotosChoosen(statefotos[i]));
            }
            if(savedInstanceState.getDouble("lat")!=1000) {
                mLastLocation = new Location("");
                mLastLocation.setLatitude(savedInstanceState.getDouble("lat"));
                mLastLocation.setLongitude(savedInstanceState.getDouble("long"));
            }

        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        horizontal_recycler_view = (RecyclerView)findViewById(R.id.horizontal_recycler_view);





        horizontalAdapter=new FotoAdapter(this, fotos);

        RecyclerView.LayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        horizontal_recycler_view.setLayoutManager(horizontalLayoutManagaer);
        horizontal_recycler_view.setAdapter(horizontalAdapter);



        mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapnya);
        mapFrag.getMapAsync(this);

        Button addphoto = (Button)findViewById(R.id.btn_tambah_foto);
        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QiPick.in(FormLegendActivity.this).fromMultipleSources("All sources", PickSource.CAMERA, PickSource.GALLERY);
//                QiPick.in(FormLegendActivity.this)
//                        .allowOnlyLocalContent(true)
//                        .withAllImageMimeTypesAllowed()
//                        .withCameraPicsDirectory(outDir)
//                        .withRequestType(1)
//                        .fromMultipleSources("All sources", PickSource.CAMERA, PickSource.GALLERY);
            }
        });

        Button btn_sipman = (Button) findViewById(R.id.btn_simpan);
        btn_sipman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valid()) {
                    simpan_legenda();
                }
                else {
                    showDialogError();
                }
            }
        });

        nama.setText(legenda.nama);
        deskripsi.setText(legenda.deskripsi);

        sp_kecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position >= 0) {
                    desas.clear();
                    desas.addAll(kecamatans.get(position).getDesas());
                    //desaspinneradapter.clear();
                    //desaspinneradapter = new ArrayAdapter<Desa>(getApplicationContext(), android.R.layout.simple_spinner_item, desas);
                    //Log.i("sigpodes", desaspinneradapter.getItem(position).toString());
                    desaspinneradapter.notifyDataSetChanged();
                    sp_desa.setSelection(0);

                    if((Integer)sp_kecamatan.getTag() == position) {
                        if(init) {
                            sp_desa.setSelection(getpositiondesa(desas, legenda.getDesa())+1);
                        }

                        if(savedInstanceState!=null) {
                            sp_desa.setSelection(savedInstanceState.getInt("desa"));
                        }

                    }
                }

                else {
                    desas.clear();
                    desaspinneradapter.notifyDataSetChanged();
                    sp_desa.setSelection(0);
                }

                sp_kecamatan.setTag(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }

    public void showDialogError() {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(FaridHelpers.joinList(errors))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .show();
    }


    public boolean valid() {

        boolean benar = true;
        errors.clear();

        int desa = sp_desa.getSelectedItemPosition();
        int kategori = sp_kategori.getSelectedItemPosition();
        legenda.nama = nama.getText().toString();
        legenda.deskripsi = deskripsi.getText().toString();

        if(mLastLocation == null) {
            errors.add("Lokasi belum ditemukan");
            benar = false;
        }

        if(legenda.nama.equals("")) {
            errors.add("Nama masih kosong");
            benar = false;
        }

        if(legenda.deskripsi.equals("")) {
            errors.add("Deskripsi masihkosong");
            benar = false;
        }

        if(desa == 0) {
            errors.add("Wilayah belum dipilih");
            benar = false;
        }

        if(kategori == 0) {
            errors.add("Kategori belum dipilih");
            benar = false;
        }

        return benar;
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    private void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    private int getpositionkecamatan(List<Kecamatan> kecamatans, Kecamatan kecamatan) {
        for(int i=0;i<kecamatans.size();i++) {
            if(kecamatans.get(i).getFullKodeKecamatan().equals(kecamatan.getFullKodeKecamatan())) return i;
        }
        return -1;
    }

    private int getpositiondesa(List<Desa> desas, Desa desa) {
        for(int i=0;i<desas.size();i++) {
            if(desas.get(i).getFullKodeDesa().equals(desa.getFullKodeDesa())) return i;
        }
        return -1;
    }

    private int getpositionkategori(List<Kategori> kategoris, Kategori kategori) {
        for(int i=0;i<kategoris.size();i++) {
            if(kategoris.get(i).equals(kategori)) return i;
        }
        return -1;
    }


    private void simpan_legenda() {

        Desa desa = desas.get(sp_desa.getSelectedItemPosition()-1);

        legenda.kodeprop = desa.kodeprop;
        legenda.kodekab = desa.kodekab;
        legenda.kodekec = desa.kodekec;
        legenda.kodedesa = desa.kodedesa;

        legenda.kodekategori = kategoris.get(sp_kategori.getSelectedItemPosition()-1).kode;
        legenda.nama = nama.getText().toString();
        legenda.deskripsi = deskripsi.getText().toString();
        legenda.latitude = mCurrLocationMarker.getPosition().latitude;
        legenda.longitude = mCurrLocationMarker.getPosition().longitude;
        legenda.kodestatus = Status.TERSIMPAN;
        if(formStatus == ACTION_NEW) legenda.waktu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        SugarRecord.save(legenda);

        Long legendaid = legenda.getId();

        if(fotofoto != null) SugarRecord.deleteInTx(fotofoto);

        if(fotos.size()>0) {
            for (int i = 0; i < fotos.size(); i++) {
                Foto foto = new Foto();
                foto.pathori = fotos.get(i).path;
                foto.paththum = fotos.get(i).path;
                foto.deskripsi = "";
                foto.legendaid = legendaid;
                foto.waktu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                SugarRecord.save(foto);
            }
        }
        //setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("kat", sp_kategori.getSelectedItemPosition());
        outState.putInt("kec", sp_kecamatan.getSelectedItemPosition());
        outState.putInt("desa", sp_desa.getSelectedItemPosition());
        String[] statefotos = new String[fotos.size()];
        for(int i=0;i<fotos.size();i++) {
            statefotos[i] = fotos.get(i).path;
        }
        outState.putStringArray("fotos", statefotos);
        outState.putDouble("lat", mCurrLocationMarker==null?1000:mCurrLocationMarker.getPosition().latitude);
        outState.putDouble("long", mCurrLocationMarker==null?1000:mCurrLocationMarker.getPosition().longitude);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        QiPick.handleActivityResult(getApplicationContext(), requestCode, resultCode, data, this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mGoogleMap.setOnMapLongClickListener(this);


        if(mLastLocation == null) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    checkLocationPermission();
                }
                else {
                    buildGoogleApiClient();
                }
            }
            else {
                buildGoogleApiClient();
            }
        }
        else {
            moveCamera(mLastLocation);
        }
    }

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();



    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);



        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
//        moveCamera(location);
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//        mLastLocation = location;

        if(location.getAccuracy() < 10.0 && location.getSpeed() < 6.95){

            //if(location.getAccuracy() < 10.0) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            //}

            if(mLastLocation != location || mLastLocation == null) {
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }
                moveCamera(location);
            }
            mLastLocation = location;
        }
    }

//    protected boolean isBetterLocation(Location location) {
//        if (mLastLocation == null) {
//            // A new location is always better than no location
//            return true;
//        }
//
//        // Check whether the new location fix is newer or older
//        long timeDelta = location.getTime() - mLastLocation.getTime();
//        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
//        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
//        boolean isNewer = timeDelta > 0;
//
//        // If it's been more than two minutes since the current location, use the new location
//        // because the user has likely moved
//        if (isSignificantlyNewer) {
//            return true;
//            // If the new location is more than two minutes older, it must be worse
//        } else if (isSignificantlyOlder) {
//            return false;
//        }
//
//        // Check whether the new location fix is more or less accurate
//        int accuracyDelta = (int) (location.getAccuracy() - mLastLocation.getAccuracy());
//        boolean isLessAccurate = accuracyDelta > 0;
//        boolean isMoreAccurate = accuracyDelta < 0;
//        boolean isSignificantlyLessAccurate = accuracyDelta > 200;
//
//        // Check if the old and new location are from the same provider
//        boolean isFromSameProvider = isSameProvider(location.getProvider(),
//                mLastLocation.getProvider());
//
//        // Determine location quality using a combination of timeliness and accuracy
//        if (location.getAccuracy() < 10) {
//            return true;
//        } else if (isMoreAccurate) {
//            return true;
//        } else if (isNewer && !isLessAccurate) {
//            return true;
//        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
//            return true;
//        }
//        return false;
//    }
//
//    /** Checks whether two providers are the same */
//    private boolean isSameProvider(String provider1, String provider2) {
//        if (provider1 == null) {
//            return provider2 == null;
//        }
//        return provider1.equals(provider2);
//    }

    public void moveCamera(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

        markerOptions.title("Lokasiku");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        markerOptions.draggable(true);

        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
        mGoogleMap.setOnMarkerDragListener(this);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(18));
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        mCurrLocationMarker = marker;
        Toast.makeText(this, marker.getPosition().latitude+", "+marker.getPosition().longitude, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onImagePicked(@NonNull PickSource pickSource, int i, @NonNull Uri uri) {
        this.pickSource = pickSource;
        this.imageresultUri = uri;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                checkWritePermission();
            }
            else {
                try {
                    saveImageResult();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            try {
                saveImageResult();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void saveImageResult() throws IOException {
        String path = new String();



        if (!outDir.exists()){
            outDir.mkdirs();
        }
        String nama = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file = new File(outDir, "IMG_" + nama + ".jpg");
        FaridHelpers.resize(this, this.imageresultUri, file);

//        if(this.pickSource == PickSource.CAMERA) {
//            if (!outDir.exists()){
//                outDir.mkdirs();
//            }
//            String nama = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//            File file = new File(outDir, "IMG_" + nama + ".jpg");
//            FaridHelpers.resize(this, this.imageresultUri, file);

            //Bitmap resized = FaridHelpers.decodeUri(this, this.imageresultUri, 600);
            //File file = new File(dir, "sketchpad" + pad.t_id + ".png");
            //FileOutputStream fOut = new FileOutputStream(file);

            //resized.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            //fOut.flush();
            //fOut.close();
            path = file.getAbsolutePath();

//            try {
//                UriUtils.saveContentToFile(this, this.imageresultUri, file);
//                path = file.getAbsolutePath();
//                Log.i("sigpodes", path);
//            } catch (IOException e) {
//                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
//            }
//        }
//
//        else {
//            path = FileHelper.getRealPath(this.imageresultUri, this);
//        }

        fotos.add(new PhotosChoosen(path));

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                checkReadPermission();
            }
            else {
                horizontalAdapter.notifyDataSetChanged();
            }
        }

        else horizontalAdapter.notifyDataSetChanged();

    }

    private void checkLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            new AlertDialog.Builder(this)
                    .setTitle("Izin Lokasi")
                    .setMessage("Legend Locator membutuhkan izin untuk mengakses lokasi Anda.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(FormLegendActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSION_REQUEST_LOCATION );
                        }
                    })
                    .create()
                    .show();


        } else {
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSION_REQUEST_LOCATION );
        }
    }

    private void checkWritePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Izin Penyimpanan File")
                        .setMessage("Legend Locator membutuhkan izin untuk menyimpan gambar dari kamera.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(FormLegendActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSION_WRITE_EXTERNAL_STORAGE );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_WRITE_EXTERNAL_STORAGE );
            }

    }

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
                            ActivityCompat.requestPermissions(FormLegendActivity.this,
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

    @Override
    public void onMultipleImagesPicked(int i, @NonNull List<Uri> list) {

    }

    @Override
    public void onError(@NonNull PickSource pickSource, int i, @NonNull String s) {

    }

    @Override
    public void onCancel(@NonNull PickSource pickSource, int i) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    buildGoogleApiClient();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case MY_PERMISSION_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    try {
                        saveImageResult();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case MY_PERMISSION_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    horizontalAdapter.notifyDataSetChanged();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {


        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        Location loc = new Location("");
        loc.setLongitude(latLng.longitude);
        loc.setLatitude(latLng.latitude);

        if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
        }

        mLastLocation = loc;


        //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

        markerOptions.title("Lokasiku");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        markerOptions.draggable(true);

        mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
        mGoogleMap.setOnMarkerDragListener(this);
    }
}
