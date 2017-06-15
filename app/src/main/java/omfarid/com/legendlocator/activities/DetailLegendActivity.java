package omfarid.com.legendlocator.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.orm.SugarRecord;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import omfarid.com.legendlocator.R;
import omfarid.com.legendlocator.adapters.FotoAdapter;
import omfarid.com.legendlocator.entities.PhotosChoosen;
import omfarid.com.legendlocator.models.Foto;
import omfarid.com.legendlocator.models.Legenda;

public class DetailLegendActivity extends AppCompatActivity implements OnMapReadyCallback{

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LatLng titik;
    RecyclerView horizontal_recycler_view;
    FotoAdapter horizontalAdapter;
    Legenda legenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_legend);
        String id = getIntent().getStringExtra("id");
        legenda = SugarRecord.findById(Legenda.class, Integer.parseInt(id));

        mapFrag = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_detail);
        mapFrag.getMapAsync(this);

        ImageView detilfoto = (ImageView) findViewById(R.id.detilfoto);
        horizontal_recycler_view = (RecyclerView)findViewById(R.id.recycler_foto);
        TextView txt_koordinat = (TextView) findViewById(R.id.txt_koordinat);
        TextView txt_kab = (TextView) findViewById(R.id.txt_kab);
        TextView txt_kec = (TextView) findViewById(R.id.txt_kec);
        TextView txt_desa = (TextView) findViewById(R.id.txt_desa);
        TextView txt_kategori = (TextView) findViewById(R.id.txt_kategori);
        TextView txt_deskripsi = (TextView) findViewById(R.id.txt_deskripsi);
        TextView txt_status = (TextView) findViewById(R.id.txt_status);
        TextView txt_waktu = (TextView) findViewById(R.id.txt_waktu);
        TextView txt_id_server = (TextView) findViewById(R.id.txt_id_server);

        txt_koordinat.setText(": "+legenda.latitude+", "+legenda.longitude);
        txt_kab.setText(": ("+legenda.kodekab+") "+legenda.getDesa().getKecamatan().getKabupaten().nama);
        txt_kec.setText(": ("+legenda.kodekec+") "+legenda.getDesa().getKecamatan().nama);
        txt_desa.setText(": ("+legenda.kodedesa+") "+legenda.getDesa().nama);
        txt_kategori.setText(": "+legenda.getKategori().nama);
        txt_deskripsi.setText(": "+legenda.deskripsi);
        txt_status.setText(": "+legenda.getStatus().keterangan);
        txt_waktu.setText(": "+legenda.waktu);
        txt_id_server.setText(legenda.idserver == null?": -Not Available-":": "+legenda.idserver);


        List<Foto> fotos = legenda.getFotos();
        List<PhotosChoosen> choosens = new ArrayList<PhotosChoosen>();
        for(int i = 0;i< fotos.size(); i++) {
            choosens.add(new PhotosChoosen(fotos.get(i).paththum));
        }

        horizontalAdapter=new FotoAdapter(this, choosens);


        horizontal_recycler_view.setAdapter(horizontalAdapter);
        horizontal_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        titik = new LatLng(legenda.latitude, legenda.longitude);
        collapsingToolbarLayout.setTitle(legenda.nama);

        if(legenda.getFotos().size()>0) {
            Glide.with(this)
                    .load(Uri.fromFile(new File(legenda.getFotos().get(0).paththum)))
                    .centerCrop()
                    .crossFade()
                    .into(detilfoto);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_edit:
                Intent intent = new Intent(this, FormLegendActivity.class);
                intent.putExtra("action", FormLegendActivity.ACTION_EDIT);
                intent.putExtra("id", legenda.getId().toString());
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(titik);
        markerOptions.title("Lokasi");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mGoogleMap.addMarker(markerOptions);

        //move map camera
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(titik));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(18));
    }
}
