//package borba.com.br.blindbeacon;
//
//import org.altbeacon.beacon.Beacon;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by andre on 13/03/2017.
// */
//
//
//public class _OLD_DestinosDB {
//
//    List<BeaconDestinoViewModel> pontosMapeados = new ArrayList<BeaconDestinoViewModel>();
//
//
//    public _OLD_DestinosDB(){
//        //Adiciona registros ao Fake DB
//        //PAI
//        //52414449-5553-4e45-5457-4f524b53434f
//
//
//        //NANDO
//        //2f234454-cf6d-4a0f-adf2-f4911ba9ffa6
//
//        //Dispositivo old
//        //699ebc80-e1f3-11e3-9a0f-0cf3ee3bc012
//
////        String uniqueId, String minorID, String majorID, String descricao,
////                String tipo, String categoria, String nome){
//
//
//        /*
//        * Novos cinza
//        * 003e8c80-ea01-4ebb-b888-78da19df9e55
//        *
//        * Major Minor: 893.2
//        * Major Minor: 893.88
//        * Major Minor: 893.148
//        * */
//
//        pontosMapeados.add(
//            new BeaconDestinoViewModel("52414449-5553-4e45-5457-4f524b53434f", "","","Ponto 1", "destino", "sala de aula","D06 109 Pai"));
//
//        pontosMapeados.add(
//                new BeaconDestinoViewModel("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6", "","","Ponto 2", "destino", "sala de aula","D06 100 Nando"));
//
//        pontosMapeados.add(
//                new BeaconDestinoViewModel("699ebc80-e1f3-11e3-9a0f-0cf3ee3bc012", "","","Ponto 3", "porta", "obstaculo", "porta"));
//    }
//
//    public BeaconDestinoViewModel getPontoByUUID(String uuid){
//
//        for (BeaconDestinoViewModel vm:pontosMapeados) {
//            if(vm.getUniqueID().equals(uuid))
//                return vm;
//        }
//
//        return null;
//    }
//
//    public List<BeaconDestinoViewModel> getPontoByUUIDArray(String[] uuid){
//
//        List<BeaconDestinoViewModel> pontosEncontrados = new ArrayList<BeaconDestinoViewModel>();
//
//        for (BeaconDestinoViewModel vm:pontosMapeados) {
//            for(int i = 0; i < uuid.length; i++){
//                if(vm.getUniqueID().equals(uuid[i]))
//                    pontosEncontrados.add(vm);
//            }
//        }
//
//        return pontosEncontrados;
//    }
//
//}
