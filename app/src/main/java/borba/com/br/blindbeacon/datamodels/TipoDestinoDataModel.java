package borba.com.br.blindbeacon.datamodels;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import borba.com.br.blindbeacon.database.DataBaseHandler;
import borba.com.br.blindbeacon.models.TipoDestinoModel;

/**
 * Created by andre on 22/03/2017.
 */

public class TipoDestinoDataModel {
    // Declaracao da tabela de Predios
    public final String TABLE_NAME = "tipoDestino";
    private final String TIPODESTINO_ID = "Id";
    private final String TIPODESTINO_NOME = "Nome";

    //Instacias da base
    private SQLiteDatabase database;
    private final DataBaseHandler dbHandler;

    public TipoDestinoDataModel(final Context context){
        dbHandler = new DataBaseHandler(context);
    }

    private void closeDataBaseConnection() {
        if (database.isOpen()) {
            database.close();
        }
    }

    public String getCreateScript(){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + TIPODESTINO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TIPODESTINO_NOME + " TEXT)" ;

        return CREATE_TABLE;
    }

    public void addTipoDestino(TipoDestinoModel model){
        final ContentValues values = new ContentValues();

        values.put(TIPODESTINO_ID, model.getId());
        values.put(TIPODESTINO_NOME, model.getNome());

        database = dbHandler.getWritableDatabase();

        try {
            database.insert(TABLE_NAME, null, values);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        closeDataBaseConnection();
    }

    public ArrayList<TipoDestinoModel> getAll(){
        final String query = "SELECT * FROM " + TABLE_NAME;
        database = dbHandler.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = database.rawQuery(query, null);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        final ArrayList<TipoDestinoModel> list = cursorToArrayList(cursor);

        closeDataBaseConnection();
        return list;
    }

    private ArrayList<TipoDestinoModel> cursorToArrayList(final Cursor cursor) {
        final ArrayList<TipoDestinoModel> list = new ArrayList<TipoDestinoModel>();

        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    final TipoDestinoModel dado = cursorToData(cursor);

                    list.add(dado);

                } while (cursor.moveToNext());
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return list;
    }

    private TipoDestinoModel cursorToData(Cursor cursor) {
        final TipoDestinoModel model = new TipoDestinoModel();

        model.setId(cursor.getInt(cursor
                .getColumnIndex(TIPODESTINO_ID)));

        model.setNome(cursor.getString(cursor
                .getColumnIndex(TIPODESTINO_NOME)));

        return model;
    }

    private JsonObject createJsonObject(TipoDestinoModel model) {
        Gson g = new Gson();
        return g.fromJson(g.toJson(model), JsonObject.class);
    }
}
