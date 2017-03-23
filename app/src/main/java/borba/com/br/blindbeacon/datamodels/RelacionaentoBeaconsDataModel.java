package borba.com.br.blindbeacon.datamodels;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import borba.com.br.blindbeacon.database.DataBaseHandler;
import borba.com.br.blindbeacon.models.RelacionamentoBeaconsModel;

/**
 * Created by andre on 22/03/2017.
 */

public class RelacionaentoBeaconsDataModel {
    // Declaracao da tabela de Predios
    public final String TABLE_NAME = "relacionamentoBeacon";
    private final String RELACIONAMENTOBEACON_ID = "IdRelacionamentoBeacon";
    private final String RELACIONAMENTOBEACON_UniqueId = "UniqueId";
    private final String RELACIONAMENTOBEACON_UniqueIdRelacionamento = "MajorId";
    private final String RELACIONAMENTOBEACON_Orientacao = "MinorId";
    private final String RELACIONAMENTOBEACON_Distancia = "Descricao";

    //Instacias da base
    private SQLiteDatabase database;
    private final DataBaseHandler dbHandler;

    public RelacionaentoBeaconsDataModel(final Context context){
        dbHandler = new DataBaseHandler(context);
    }

    private void closeDataBaseConnection() {
        if (database.isOpen()) {
            database.close();
        }
    }

    public String getCreateScript(){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + RELACIONAMENTOBEACON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RELACIONAMENTOBEACON_UniqueId + " TEXT, " + RELACIONAMENTOBEACON_UniqueIdRelacionamento + " TEXT, " + RELACIONAMENTOBEACON_Orientacao + " TEXT, " +
                RELACIONAMENTOBEACON_Distancia + " NUMERIC)";

        return CREATE_TABLE;
    }

    public void addDestino(RelacionamentoBeaconsModel model){
        final ContentValues values = new ContentValues();

        values.put(RELACIONAMENTOBEACON_ID, model.getIdRelacionamentoBeacon());
        values.put(RELACIONAMENTOBEACON_UniqueId, model.getUniqueId());
        values.put(RELACIONAMENTOBEACON_UniqueIdRelacionamento, model.getUniqueIdRelacionamento());
        values.put(RELACIONAMENTOBEACON_Orientacao, model.getOrientacao());
        values.put(RELACIONAMENTOBEACON_Distancia, model.getDistancia());

        database = dbHandler.getWritableDatabase();

        try {
            database.insert(TABLE_NAME, null, values);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        closeDataBaseConnection();
    }

    public ArrayList<RelacionamentoBeaconsModel> getAll(){
        final String query = "SELECT * FROM " + TABLE_NAME;
        database = dbHandler.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = database.rawQuery(query, null);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        final ArrayList<RelacionamentoBeaconsModel> list = cursorToArrayList(cursor);

        closeDataBaseConnection();
        return list;
    }

    private ArrayList<RelacionamentoBeaconsModel> cursorToArrayList(final Cursor cursor) {
        final ArrayList<RelacionamentoBeaconsModel> list = new ArrayList<RelacionamentoBeaconsModel>();

        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    final RelacionamentoBeaconsModel dado = cursorToData(cursor);

                    list.add(dado);

                } while (cursor.moveToNext());
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return list;
    }

    private RelacionamentoBeaconsModel cursorToData(Cursor cursor) {
        final RelacionamentoBeaconsModel model = new RelacionamentoBeaconsModel();

        model.setIdRelacionamentoBeacon(cursor.getInt(cursor
                .getColumnIndex(RELACIONAMENTOBEACON_ID)));

        model.setUniqueId(cursor.getString(cursor
                .getColumnIndex(RELACIONAMENTOBEACON_UniqueId)));

        model.setUniqueIdRelacionamento(cursor.getString(cursor
                .getColumnIndex(RELACIONAMENTOBEACON_UniqueIdRelacionamento)));

        model.setOrientacao(cursor.getString(cursor
                .getColumnIndex(RELACIONAMENTOBEACON_Orientacao)));

        model.setDistancia(cursor.getDouble(cursor
                .getColumnIndex(RELACIONAMENTOBEACON_Distancia)));

        return model;
    }

    private JsonObject createJsonObject(RelacionamentoBeaconsModel model) {
        Gson g = new Gson();
        return g.fromJson(g.toJson(model), JsonObject.class);
    }
}
