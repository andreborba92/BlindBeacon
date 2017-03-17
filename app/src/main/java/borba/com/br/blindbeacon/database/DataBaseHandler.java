package borba.com.br.blindbeacon.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by andre on 16/03/2017.
 */

public class DataBaseHandler extends SQLiteOpenHelper {

    public DataBaseHandler(final Context context) {
        super(context, BLINDBEACON_DATABASE_NAME, null, BLINDBEACON_DATABASE_VERSION);
    }

    // Declaracao do banco de dados
    private static final String BLINDBEACON_DATABASE_NAME = "BLINDBEACON_DATABASE";
    private static final int BLINDBEACON_DATABASE_VERSION = 1;

    // Declaracao da tabela de logs
    public static final String LOG_TABLE_NAME = "logs";
    public static final String LOG_ID = "id_log";
    public static final String LOG_DATA = "data";
    public static final String LOG_MENSAGEM = "mensagem";
    public static final String LOG_TIPO = "tipo";
    public static final String LOG_EXCECAO = "excecao";
    public static final String LOG_DETALHES = "detalhes";
    public static final String LOG_URL_REQUSICAO = "url";
    public static final String LOG_ORIGEM = "origem";
    public static final String LOG_PLATAFORMA = "plataforma";
    public static final String LOG_VERSAO_APP = "versao";

    // Criando tabelas
    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_LOG_TABLE = "CREATE TABLE " + LOG_TABLE_NAME + "(" + LOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + LOG_DATA + " TEXT, " + LOG_MENSAGEM + " TEXT, " + LOG_TIPO
                + " TEXT, " + LOG_EXCECAO + " TEXT, "  + LOG_DETALHES + " TEXT, "  + LOG_URL_REQUSICAO + " TEXT, "  + LOG_ORIGEM + " TEXT, "  + LOG_PLATAFORMA + " TEXT, "  + LOG_VERSAO_APP + " TEXT)";

        db.execSQL(CREATE_LOG_TABLE);
        Log.v("Database", "Base criada com sucesso!");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

        // Dropa tabelas antigas, se necessario
        Log.v("Database", "Atualizando banco de dados.");
        db.execSQL("DROP TABLE IF EXISTS " + LOG_TABLE_NAME);

        // Recria as tabelas
        onCreate(db);
    }

    /**
     * Compacta a base local.
     */
    public void Vacuum() {
        SQLiteDatabase db = super.getWritableDatabase();
        db.execSQL("VACUUM");
        db.close();
    }

    /**
     * Trunca os dados de uma tabela, limpando seus registros.
     *
     * @param tableName
     * @return qtd
     */
    public int TruncateTable(String tableName) {
        SQLiteDatabase db = super.getWritableDatabase();
        try {
            db.beginTransaction();
            int r = db.delete(tableName, null, null);
            db.setTransactionSuccessful();
            return r;
        } catch (SQLException e) {
            throw e;
        } finally {
            db.endTransaction();
            if (db.isOpen())
                db.close();
        }
    }
}

