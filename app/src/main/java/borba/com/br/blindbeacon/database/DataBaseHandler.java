package borba.com.br.blindbeacon.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import borba.com.br.blindbeacon.datamodels.PredioDataModel;

/**
 * Created by andre on 16/03/2017.
 */

public class DataBaseHandler extends SQLiteOpenHelper {

    private Context context;

    public DataBaseHandler(final Context context) {
        super(context, BLINDBEACON_DATABASE_NAME, null, BLINDBEACON_DATABASE_VERSION);
        this.context = context;
    }

    // Declaracao do banco de dados
    private static final String BLINDBEACON_DATABASE_NAME = "BLINDBEACON_DATABASE";
    private static final int BLINDBEACON_DATABASE_VERSION = 1;

    // Criando tabelas
    @Override
    public void onCreate(SQLiteDatabase db) {

       String PredioCreateScript = new PredioDataModel(this.context).getCreateScript();
        db.execSQL(PredioCreateScript);


        Log.v("Database", "Base criada com sucesso!");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

        // Dropa tabelas antigas, se necessario
//        Log.v("Database", "Atualizando banco de dados.");
//        db.execSQL("DROP TABLE IF EXISTS " + LOG_TABLE_NAME);

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

