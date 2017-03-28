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
import borba.com.br.blindbeacon.models.PredioModel;

/**
 * Created by André Borba on 21/02/2017.
 */
public class PredioAdapter extends ArrayAdapter<PredioModel> {

    private Context ctx;
    private int resourceID;
    private List<PredioModel> meusPredios;
    private ItemHolder itemHolder;


    public PredioAdapter(Context context, int resource, List<PredioModel> meusPredios) {
        super(context, resource, meusPredios);
        this.ctx = context;
        this.meusPredios = meusPredios;
        this.resourceID = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
            itemHolder = new ItemHolder();

            rowView = inflater.inflate(resourceID, parent, false);

            itemHolder.nome = (TextView) rowView.findViewById(R.id.tvNomePredio);

            rowView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) rowView.getTag();
        }

        itemHolder.nome.setText(String.valueOf(meusPredios.get(position).getNome()));
        itemHolder.idPredio =meusPredios.get(position).getId();

        return rowView;
    }


    private static class ItemHolder {
        TextView nome;
        int idPredio;
    }
}

