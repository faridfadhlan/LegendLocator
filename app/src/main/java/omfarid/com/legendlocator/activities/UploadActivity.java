package omfarid.com.legendlocator.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.orm.SugarRecord;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.w3c.dom.Text;

import omfarid.com.legendlocator.FileHelper;
import omfarid.com.legendlocator.R;
import omfarid.com.legendlocator.models.Foto;
import omfarid.com.legendlocator.models.Legenda;

public class UploadActivity extends AppCompatActivity implements UploadStatusDelegate{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ImageView upload = (ImageView) findViewById(R.id.upload);
        TextView ket = (TextView) findViewById(R.id.ket);
        Button upload_btn = (Button) findViewById(R.id.upload_btn);
        final Legenda legenda = SugarRecord.listAll(Legenda.class).get(0);
        final String uristring = legenda.getFotos().get(0).paththum;
        ket.setText(legenda.nama.trim()+"\n"+String.valueOf(Integer.parseInt(legenda.kodekategori)));


        Glide.with(this)
                .load(Uri.parse(uristring))
                .centerCrop()
                .into(upload);

        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String uploadId =
                            new MultipartUploadRequest(getApplicationContext(), "http://kalbar.bps.go.id/sigpodes/legenda/upload")
                                    .addFileToUpload(FileHelper.getRealPath(uristring, getApplicationContext()), "foto")
                                    .addParameter("namafile",
                                            legenda.kodeprop+
                                            legenda.kodekab+
                                            legenda.kodekec+
                                            legenda.kodedesa+
                                            "_"+
                                            legenda.nama.trim().replace(" ", "_")+
                                            ".jpg"
                                    )
                                    .addParameter("kode_prop", legenda.kodeprop)
                                    .addParameter("kode_kabkota", legenda.kodekab)
                                    .addParameter("kode_kec", legenda.kodekec)
                                    .addParameter("kode_desa", legenda.kodedesa)
                                    .addParameter("id_jenis", String.valueOf(Integer.parseInt(legenda.kodekategori)))
                                    .addParameter("nama", legenda.nama.trim())
                                    .addParameter("deskripsi", legenda.deskripsi)
                                    .addParameter("latlong", legenda.longitude+","+legenda.latitude)
                                    .setNotificationConfig(new UploadNotificationConfig())
                                    .setMaxRetries(2)
                                    .startUpload();
                } catch (Exception exc) {
                    Log.e("AndroidUploadService", exc.getMessage(), exc);
                }
            }
        });
    }

    @Override
    public void onProgress(UploadInfo uploadInfo) {

    }

    @Override
    public void onError(UploadInfo uploadInfo, Exception exception) {
        Log.e("sigpodes", exception.toString());
    }

    @Override
    public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
        Log.e("sigpodes", serverResponse.toString());
    }

    @Override
    public void onCancelled(UploadInfo uploadInfo) {

    }
}
