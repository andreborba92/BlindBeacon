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

import borba.com.br.blindbeacon.enums.CategoriaEnum;
import borba.com.br.blindbeacon.models.DestinoModel;

/**
 * Created by André Borba on 21/02/2017.
 */
public class DestinoAdapter extends ArrayAdapter<DestinoModel> {

    private Context ctx;
    private int resourceID;
    private List<DestinoModel> meusDestinos;
    private ItemHolder itemHolder;


    public DestinoAdapter(Context context, int resource, List<DestinoModel> meusDestinos) {
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

            itemHolder.nome = (TextView) rowView.findViewById(R.id.tvNomeDestino);
            itemHolder.categoria = (TextView) rowView.findViewById(R.id.tvCategoriaDestino);

            rowView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) rowView.getTag();
        }

        itemHolder.nome.setText(String.valueOf(meusDestinos.get(position).getNome()));
        itemHolder.categoria.setText(String.valueOf(CategoriaEnum.getCategoriaById(meusDestinos.get(position).getIdCategoria()).toString()));

        return rowView;
    }


    private static class ItemHolder {
        TextView nome, categoria;
    }
}

