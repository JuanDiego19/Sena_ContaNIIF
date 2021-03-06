package club.contaniif.contaniff.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import java.util.ArrayList;

import club.contaniif.contaniff.R;
import club.contaniif.contaniff.entidades.PreguntasVo;
import club.contaniif.contaniff.entidades.VolleySingleton;

public class AdapterDrag extends RecyclerView.Adapter<AdapterDrag.AdapterHolder> implements View.OnClickListener, View.OnDragListener {

    private final ArrayList<PreguntasVo> lista;
    private View.OnClickListener listener;
    private final Context context;
    private View.OnDragListener onDragListener;

    public AdapterDrag(ArrayList<PreguntasVo> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterDrag.AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modelo_contenido, null, false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
        view.setOnDragListener(this);
        view.setOnClickListener(this);
        return new AdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHolder holder, int position) {
        if (lista.get(position).getImg() != null ) {
            cargarImagenWebService(lista.get(position).getImg(), holder);
        } else {
           holder.imagen.setImageResource(R.drawable.punteadoc);
        }
        holder.palabra.setText(lista.get(position).getPalabra());
    }

    private void cargarImagenWebService(String rutaImagen, final AdapterHolder holder) {
        String ip = context.getString(R.string.imgPreguntas);
        String urlImagen = "http://" + ip + rutaImagen;
        ImageRequest imageRequest = new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.imagen.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error al cargar la imagen" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(imageRequest);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }


    public void setOnDragListener(View.OnDragListener onDragListener) {
        this.onDragListener = onDragListener;
    }


    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        if (onDragListener != null) {
            onDragListener.onDrag(view, dragEvent);
        }
        return true;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public class AdapterHolder extends RecyclerView.ViewHolder {
        final TextView palabra;
        final ImageView imagen;

        AdapterHolder(View itemView) {
            super(itemView);
            palabra = itemView.findViewById(R.id.preguntasRespuestaRelacion);
            imagen = itemView.findViewById(R.id.imagenesRespuestaRelacion);
        }
    }
}
