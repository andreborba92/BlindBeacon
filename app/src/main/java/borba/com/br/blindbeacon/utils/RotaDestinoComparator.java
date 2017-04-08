package borba.com.br.blindbeacon.utils;

import java.util.Comparator;

import borba.com.br.blindbeacon.viewmodels.RotaDestinoViewModel;

/**
 * Created by andre on 08/04/2017.
 */

public class RotaDestinoComparator implements Comparator<RotaDestinoViewModel> {

    public int compare(RotaDestinoViewModel left, RotaDestinoViewModel right) {
        return left.compareTo(right);
    }

}