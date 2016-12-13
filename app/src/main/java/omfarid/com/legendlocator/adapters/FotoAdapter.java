package omfarid.com.legendlocator.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import omfarid.com.legendlocator.R;
import omfarid.com.legendlocator.activities.FormLegendActivity;

/**
 * Created by farid on 12/6/2016.
 */

public class FotoAdapter extends RecyclerView.Adapter<FotoAdapter.MyViewHolder> {

    private List<String> fotos;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView fotonya;

        public MyViewHolder(View view) {
            super(view);
            fotonya = (ImageView) view.findViewById(R.id.fotonya);

        }
    }


    public FotoAdapter(Context context, List<String> fotos) {
        this.fotos = fotos;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.horizontal_item_view, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        if(fotos.get(position).equals("noimage")) {
            Picasso.with(context).load(R.mipmap.ic_launcher);
        }

        else {
            Picasso.with(context)
                    .load(Uri.fromFile(new File(fotos.get(position))))
                    .fit()
                    .centerInside()
                    .into(holder.fotonya, new Callback() {
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
    }

    @Override
    public int getItemCount() {
        return fotos.size();
    }
}