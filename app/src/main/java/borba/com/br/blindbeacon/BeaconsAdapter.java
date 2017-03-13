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

/**
 * Created by tailan.trucolo on 15/09/2016.
 */
public class BeaconsAdapter extends ArrayAdapter<Beacon> {

    private Context ctx;
    private int resourceID;
    private List<Beacon> myBeacons;
    private ItemHolder itemHolder;


    public BeaconsAdapter(Context context, int resource, List<Beacon> myBeacons) {
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

            itemHolder.uniqueID = (TextView) rowView.findViewById(R.id.tvUID);
            itemHolder.majorID = (TextView) rowView.findViewById(R.id.tvMajorID);
            itemHolder.minorID = (TextView) rowView.findViewById(R.id.tvMinorId);
            itemHolder.rssi = (TextView) rowView.findViewById(R.id.tvRSSI);
            itemHolder.distance = (TextView) rowView.findViewById(R.id.tvDistance);

            rowView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) rowView.getTag();
        }

        itemHolder.uniqueID.setText(String.valueOf(myBeacons.get(position).getId1()));
        itemHolder.majorID.setText(String.valueOf(myBeacons.get(position).getId2()));
        itemHolder.minorID.setText(String.valueOf(myBeacons.get(position).getId3()));
        itemHolder.rssi.setText(String.valueOf(myBeacons.get(position).getRssi()));

        //Formatação da distância de double para duas casas decimais
        DecimalFormat df = new DecimalFormat("#.##");
        String distanciaFormatada = df.format(myBeacons.get(position).getDistance()) + " metros";

        //itemHolder.distance.setText(String.valueOf(myBeacons.get(position).getDistance()));
        itemHolder.distance.setText(distanciaFormatada);

        return rowView;
    }


    private static class ItemHolder {
        TextView uniqueID, majorID, minorID, rssi, distance;
    }
}

