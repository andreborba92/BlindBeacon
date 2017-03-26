package borba.com.br.blindbeacon.enums;

/**
 * Created by andre on 25/03/2017.
 */

public enum CategoriaEnum { //sala de aula, banheiro, xerox, auditório, café, secretaria
    SALA_DE_AULA ("SALA_DE_AULA", 1),
    BANHEIRO("BANHEIRO",2),
    XEROX("XEROX",3),
    AUDITORIO("AUDITORIO",4),
    CAFE("CAFE",5),
    SECRETARIA("SECRETARIA",6),
    OUTRO("OUTRO",7);

    private String stringValue;
    private int intValue;

    private CategoriaEnum(String toString, int value) {
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
