package borba.com.br.blindbeacon;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by André Borba on 21/02/2017.
 */
public class DestinoAdapter extends ArrayAdapter<Destino> {

    private Context ctx;
    private int resourceID;
    private List<Destino> meusDestinos;
    private ItemHolder itemHolder;


    public DestinoAdapter(Context context, int resource, List<Destino> meusDestinos) {
        super(context, resource, meusDestinos);
        this.ctx = context;
        this.meusDestinos = meusDestinos;
        this.resourceID = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
            itemHolder = new ItemHolder();

            rowView = inflater.inflate(resourceID, parent, false);

            Log.w("TAG_BORBA", "ENTREI 1");

            itemHolder.nome = (TextView) rowView.findViewById(R.id.tvNomeDestino);
            itemHolder.categoria = (TextView) rowView.findViewById(R.id.tvCategoriaDestino);
            itemHolder.distancia = (TextView) rowView.findViewById(R.id.tvDistanciaDestino);

            rowView.setTag(itemHolder);
        } else {
            Log.w("TAG_BORBA", "ENTREI 2");
            itemHolder = (ItemHolder) rowView.getTag();
        }

        Log.w("TAG_BORBA", itemHolder.toString());

        itemHolder.nome.setText(String.valueOf(meusDestinos.get(position).getNome()));
        itemHolder.categoria.setText(String.valueOf(meusDestinos.get(position).getCategoria()));
        itemHolder.distancia.setText(String.valueOf(meusDestinos.get(position).getDistancia()));

        return rowView;
    }


    private static class ItemHolder {
        TextView nome, categoria, distancia;
    }
}

