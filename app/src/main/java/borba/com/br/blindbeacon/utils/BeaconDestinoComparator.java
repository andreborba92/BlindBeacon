package borba.com.br.blindbeacon.utils;

import java.util.Comparator;

import borba.com.br.blindbeacon.viewmodels.BeaconDestinoViewModel;

/**
 * Created by andre on 02/04/2017.
 */

public class BeaconDestinoComparator  implements Comparator<BeaconDestinoViewModel> {

        public int compare(BeaconDestinoViewModel left, BeaconDestinoViewModel right) {
            return left.compareTo(right);
        }

}
