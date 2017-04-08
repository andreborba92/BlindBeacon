package borba.com.br.blindbeacon;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import borba.com.br.blindbeacon.enums.CategoriaEnum;
import borba.com.br.blindbeacon.viewmodels.RotaDestinoViewModel;

/**
 * Created by andre on 08/04/2017.
 */

public class RotaAdapter extends ArrayAdapter<RotaDestinoViewModel> {

private Context ctx;
private int resourceID;
private List<RotaDestinoViewModel> _rotas;
private ItemHolder itemHolder;


public RotaAdapter(Context context, int resource, List<RotaDestinoViewModel> minhasRotas) {
        super(context, resource, minhasRotas);
        this.ctx = context;
        this._rotas = minhasRotas;
        this.resourceID = resource;
        }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (rowView == null) {
        LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
        itemHolder = new ItemHolder();

        rowView = inflater.inflate(resourceID, parent, false);

        itemHolder.nomeDestinoRota = (TextView) rowView.findViewById(R.id.tvNomeDestinoRota);
        itemHolder.categoriaDestinoRota = (TextView) rowView.findViewById(R.id.tvCategoriaDestinoRota);
        itemHolder.distanciaDestinoRota = (TextView) rowView.findViewById(R.id.tvDistanciaDestinoRota);

        rowView.setTag(itemHolder);
        } else {
        itemHolder = (ItemHolder) rowView.getTag();
        }

        itemHolder.nomeDestinoRota.setText(String.valueOf(_rotas.get(position).getDestinoModel().getNome()));

        String categoria = String.valueOf(CategoriaEnum.getCategoriaById(_rotas.get(position).getDestinoModel().getIdCategoria()).toString());
        itemHolder.categoriaDestinoRota.setText(categoria);

        //Formatação da distância de double para duas casas decimais
//        DecimalFormat df = new DecimalFormat("#.##");
//
//        Double distancia = _rotas.get(position).getDistanciaDoPontoOrigem();
//        String distanciaFormatada = df.format(distancia) + " metros";

        itemHolder.distanciaDestinoRota.setText(_rotas.get(position).getDistanciaFormatada());

        return rowView;
     }

    private static class ItemHolder {
        TextView nomeDestinoRota, categoriaDestinoRota, distanciaDestinoRota;
    }
}
