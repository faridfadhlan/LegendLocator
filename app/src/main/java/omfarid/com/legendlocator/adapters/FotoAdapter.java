package omfarid.com.legendlocator.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import omfarid.com.legendlocator.R;
import omfarid.com.legendlocator.entities.PhotosChoosen;

/**
 * Created by farid on 12/6/2016.
 */

public class FotoAdapter extends RecyclerView.Adapter<FotoAdapter.MyViewHolder> {

    private List<PhotosChoosen> fotos;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView fotonya;

        public MyViewHolder(View view) {
            super(view);
            fotonya = (ImageView) view.findViewById(R.id.fotonya);
            //view.setOnCreateContextMenuListener(this);
        }
//
//        @Override
//        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//            menu.add("Lihat");
//            menu.add("Hapus");
//        }
    }


    public FotoAdapter(Context context, List<PhotosChoosen> fotos) {
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
        //Log.i("sigpodes", fotos.get(position).path);
        Glide.with(this.context)
                .load(Uri.fromFile(new File(fotos.get(position).path)))
                .override(400, 300)
                .centerCrop()
                .crossFade()
                .into(holder.fotonya);
    }

    @Override
    public int getItemCount() {
        return fotos.size();
    }
}