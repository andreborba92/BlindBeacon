package borba.com.br.blindbeacon.utils;

import org.altbeacon.beacon.Beacon;

import java.util.Comparator;

/**
 * Created by andre on 19/04/2017.
 */

public class BeaconNativeComparator implements Comparator<Beacon> {

        public int compare(Beacon left, Beacon right) {
            //return left.compareTo(right);
            if(left.getDistance() < right.getDistance())
                return -1;
            else if(left.getDistance() > right.getDistance())
                return 1;
            else
                return 0;
        }

}
