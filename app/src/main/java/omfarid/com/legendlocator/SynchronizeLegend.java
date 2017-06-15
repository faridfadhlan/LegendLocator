package omfarid.com.legendlocator;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.orm.SugarRecord;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;

import omfarid.com.legendlocator.activities.MainActivity;
import omfarid.com.legendlocator.fragments.legends.LegendSavedFragment;
import omfarid.com.legendlocator.models.Legenda;

/**
 * Created by farid on 1/4/2017.
 */

public class SynchronizeLegend {
    List<Legenda> legendas;
    private Context context;
    String serverUrlString = "http://kalbar.bps.go.id/sigpodes/legenda/upload";

    public SynchronizeLegend(Context context) {
        this.context = context;
    }

    public void setLegendas(List<Legenda> legendas) {
        this.legendas = legendas;
    }

    private UploadNotificationConfig getNotificationConfig(String filename) {
        return new UploadNotificationConfig()
//                .setIcon(R.drawable.ic_upload)
//                .setCompletedIcon(R.drawable.ic_upload_success)
//                .setErrorIcon(R.drawable.ic_upload_error)
                .setTitle(filename)
                .setInProgressMessage("Uploading")
                .setCompletedMessage("Berhasil diupload")
                .setErrorMessage("Gagal diupload")
                .setClickIntent(new Intent(this.context, MainActivity.class))
                .setClearOnAction(true)
                .setRingToneEnabled(true);
    }

//    private void addUploadToList(String uploadID, String filename) {
//        View uploadProgressView = getLayoutInflater().inflate(R.layout.view_upload_progress, null);
//        UploadProgressViewHolder viewHolder = new UploadProgressViewHolder(uploadProgressView, filename);
//        viewHolder.uploadId = uploadID;
//        container.addView(viewHolder.itemView, 0);
//        uploadProgressHolders.put(uploadID, viewHolder);
//    }

    private void showToast(String message) {
        Toast.makeText(this.context, message, Toast.LENGTH_LONG).show();
    }

    public void start() {
        final HashMap<String, Legenda> map = new HashMap<>();
        for (int i=0;i<legendas.size();i++) {

            try {
                //final String filename = getFilename(fileToUploadPath);

                String uploadId = new MultipartUploadRequest(this.context, serverUrlString)
                        .addFileToUpload(legendas.get(i).getFotos().get(0).paththum, "foto")
                        .addParameter("namafile",
                                legendas.get(i).kodeprop+
                                legendas.get(i).kodekab+
                                legendas.get(i).kodekec+
                                legendas.get(i).kodedesa+
                                "_"+
                                legendas.get(i).nama.trim().replace(" ", "_")+
                                ".jpg"
                        )
                        .addParameter("kode_prop", legendas.get(i).kodeprop)
                        .addParameter("kode_kabkota", legendas.get(i).kodekab)
                        .addParameter("kode_kec", legendas.get(i).kodekec)
                        .addParameter("kode_desa", legendas.get(i).kodedesa)
                        .addParameter("id_jenis", String.valueOf(Integer.parseInt(legendas.get(i).kodekategori)))
                        .addParameter("nama", legendas.get(i).nama.trim())
                        .addParameter("deskripsi", legendas.get(i).deskripsi)
                        .addParameter("latlong", legendas.get(i).longitude+","+legendas.get(i).latitude)
                        .setNotificationConfig(getNotificationConfig(legendas.get(i).nama.trim()))
                        .setMaxRetries(3)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(UploadInfo uploadInfo) {
                                // your code here
                            }

                            @Override
                            public void onError(UploadInfo uploadInfo, Exception exception) {
                                showToast(map.get(uploadInfo.getUploadId()).nama+":"+exception.toString());
                            }

                            @Override
                            public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {
                                Legenda legenda = map.get(uploadInfo.getUploadId());
                                legenda.kodestatus = "2";
                                SugarRecord.save(legenda);


                                //LegendSavedFragment f = (LegendSavedFragment) fm.findFragmentByTag();
                            }

                            @Override
                            public void onCancelled(UploadInfo uploadInfo) {
                                // your code here
                            }
                        })
                        .startUpload();

                map.put(uploadId, legendas.get(i));

                //addUploadToList(uploadID, filename);

                // these are the different exceptions that may be thrown
            } catch (FileNotFoundException exc) {
                //showToast(exc.getMessage());
            } catch (IllegalArgumentException exc) {
                showToast("Missing some arguments. " + exc.getMessage());
            } catch (MalformedURLException exc) {
                showToast(exc.getMessage());
            }
        }
    }
}
