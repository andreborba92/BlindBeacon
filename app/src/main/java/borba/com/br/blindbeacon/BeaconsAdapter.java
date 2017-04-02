package borba.com.br.blindbeacon;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;

import borba.com.br.blindbeacon.viewmodels.BeaconDestinoViewModel;

/**
 * Created by tailan.trucolo on 15/09/2016.
 */
public class BeaconsAdapter extends ArrayAdapter<BeaconDestinoViewModel> {

    private Context ctx;
    private int resourceID;
    private List<BeaconDestinoViewModel> myBeacons;
    private ItemHolder itemHolder;


    public BeaconsAdapter(Context context, int resource, List<BeaconDestinoViewModel> myBeacons) {
        super(context, resource, myBeacons);
        this.ctx = context;
        this.myBeacons = myBeacons;
        this.resourceID = resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        if (rowView == null) {
            LayoutInflater inflater = ((Activity) ctx).getLayoutInflater();
            itemHolder = new ItemHolder();

            rowView = inflater.inflate(resourceID, parent, false);

            itemHolder.nomeBeacon = (TextView) rowView.findViewById(R.id.tvNomeBeacon);
            itemHolder.uniqueID = (TextView) rowView.findViewById(R.id.tvUID);
            itemHolder.majorMinorId = (TextView) rowView.findViewById(R.id.tvMajorMinor);
            itemHolder.rssi_distance = (TextView) rowView.findViewById(R.id.tvRSSIDistance);

            rowView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) rowView.getTag();
        }

        itemHolder.nomeBeacon.setText(String.valueOf(myBeacons.get(position).getDestinoModel().getNome()));
        itemHolder.uniqueID.setText(String.valueOf(myBeacons.get(position).getBeacon().getId1()));
        itemHolder.majorMinorId.setText(String.valueOf(myBeacons.get(position).getBeacon().getId2()) + " / " +
                String.valueOf(myBeacons.get(position).getBeacon().getId3()));

        //Formatação da distância de double para duas casas decimais
        DecimalFormat df = new DecimalFormat("#.##");
        String distanciaFormatada = df.format(myBeacons.get(position).getBeacon().getDistance()) + " metros";

        //itemHolder.distance.setText(String.valueOf(myBeacons.get(position).getDistance()));
        itemHolder.rssi_distance.setText(String.valueOf(myBeacons.get(position).getBeacon().getRssi())
        + " / " + distanciaFormatada);

        return rowView;
    }


    private static class ItemHolder {
        TextView nomeBeacon, uniqueID, majorMinorId, rssi_distance;
    }
}

