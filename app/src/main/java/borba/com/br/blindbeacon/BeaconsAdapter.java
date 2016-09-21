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

            itemHolder.id1 = (TextView) rowView.findViewById(R.id.tvID1);
            itemHolder.id2 = (TextView) rowView.findViewById(R.id.tvID2);
            itemHolder.id3 = (TextView) rowView.findViewById(R.id.tvID3);

            rowView.setTag(itemHolder);
        } else {
            itemHolder = (ItemHolder) rowView.getTag();
        }

        //ToDo: Adicionar Id3 (minor), manter RSS e colocar distancia
        itemHolder.id1.setText(String.valueOf(myBeacons.get(position).getId1()));
        itemHolder.id2.setText(String.valueOf(myBeacons.get(position).getId2()));
        itemHolder.id3.setText(String.valueOf(myBeacons.get(position).getRssi()));


        return rowView;
    }


    private static class ItemHolder {
        TextView id1, id2, id3;
    }
}

