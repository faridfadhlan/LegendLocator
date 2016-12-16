package omfarid.com.legendlocator.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

import omfarid.com.legendlocator.R;
import omfarid.com.legendlocator.models.Legends;

public class DetailLegendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_legend);

        ImageView detilfoto = (ImageView) findViewById(R.id.detilfoto);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        collapsingToolbarLayout.setTitle("Judul");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        String id = getIntent().getStringExtra("id");
        Legends legend = Legends.findById(Legends.class, Integer.parseInt(id));

        Picasso.with(this)
                .load(Uri.fromFile(new File(legend.getPhotos().get(0).path)))
                .fit()
                .centerInside()
                .into(detilfoto, new Callback() {
                    @Override
                    public void onSuccess() {
                        //Log.i(TAG, "Picasso Success Loading Thumbnail - " + path);
                    }

                    @Override
                    public void onError() {
                        //Log.i(TAG, "Picasso Error Loading Thumbnail Small - " + path);
                    }
                });

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
}
