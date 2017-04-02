package borba.com.br.blindbeacon.viewmodels;

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;

import borba.com.br.blindbeacon.models.DestinoModel;

/**
 * Created by andre on 02/04/2017.
 */

public class BeaconDestinoViewModel implements Comparable<BeaconDestinoViewModel> {
    private Beacon _beacon;
    private DestinoModel _destinoModel;

    public Beacon getBeacon() {
        return _beacon;
    }

    public DestinoModel getDestinoModel() {
        return _destinoModel;
    }

    public BeaconDestinoViewModel(Beacon beacon, DestinoModel destino){
        this._beacon = beacon;
        this._destinoModel = destino;
    }

    @Override
    public int compareTo(BeaconDestinoViewModel another) {
        if(this.getDestinoModel().getIdDestino() < another.getDestinoModel().getIdDestino())
            return -1;
        else if(this.getDestinoModel().getIdDestino() > another.getDestinoModel().getIdDestino())
            return 1;
        else
            return 0;
    }
}
