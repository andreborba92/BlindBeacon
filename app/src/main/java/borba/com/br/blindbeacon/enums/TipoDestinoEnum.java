package borba.com.br.blindbeacon.enums;

/**
 * Created by andre on 25/03/2017.
 */

public enum TipoDestinoEnum { //destino, conexão, obstáculo
    DESTINO ("DESTINO", 1),
    CONEXAO("CONEXAO",2),
    OBSTACULO("OBSTACULO",3);

    private String stringValue;
    private int intValue;

    private TipoDestinoEnum(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    public int getValue(){
        return intValue;
    }
}
