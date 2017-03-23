package borba.com.br.blindbeacon.datamodels;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import borba.com.br.blindbeacon.database.DataBaseHandler;
import borba.com.br.blindbeacon.models.BeaconModel;

/**
 * Created by andre on 22/03/2017.
 */

public class BeaconDataModel {
    // Declaracao da tabela de Predios
    public final String TABLE_NAME = "beacon";
    private final String BEACON_UniqueId = "UniqueId";
    private final String BEACON_MajorId = "MajorId";
    private final String BEACON_MinorId = "MinorId";
    private final String BEACON_Descricao = "Descricao";
    private final String BEACON_IdDestino = "IdDestino";

    //Instacias da base
    private SQLiteDatabase database;
    private final DataBaseHandler dbHandler;

    public BeaconDataModel(final Context context){
        dbHandler = new DataBaseHandler(context);
    }

    private void closeDataBaseConnection() {
        if (database.isOpen()) {
            database.close();
        }
    }

    public String getCreateScript(){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + BEACON_UniqueId + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BEACON_MajorId + " TEXT, " + BEACON_MinorId + " TEXT, " + BEACON_Descricao + " TEXT, " +
                BEACON_IdDestino + " INT)";

        return CREATE_TABLE;
    }

    public void addDestino(BeaconModel model){
        final ContentValues values = new ContentValues();

        values.put(BEACON_UniqueId, model.getUniqueId());
        values.put(BEACON_MajorId, model.getMajorId());
        values.put(BEACON_MinorId, model.getMinorId());
        values.put(BEACON_Descricao, model.getDescricao());
        values.put(BEACON_IdDestino, model.getIdDestino());

        database = dbHandler.getWritableDatabase();

        try {
            database.insert(TABLE_NAME, null, values);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        closeDataBaseConnection();
    }

    public ArrayList<BeaconModel> getAll(){
        final String query = "SELECT * FROM " + TABLE_NAME;
        database = dbHandler.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = database.rawQuery(query, null);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        final ArrayList<BeaconModel> list = cursorToArrayList(cursor);

        closeDataBaseConnection();
        return list;
    }

    private ArrayList<BeaconModel> cursorToArrayList(final Cursor cursor) {
        final ArrayList<BeaconModel> list = new ArrayList<BeaconModel>();

        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    final BeaconModel dado = cursorToData(cursor);

                    list.add(dado);

                } while (cursor.moveToNext());
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return list;
    }

    private BeaconModel cursorToData(Cursor cursor) {
        final BeaconModel model = new BeaconModel();

        model.setIdDestino(cursor.getInt(cursor
                .getColumnIndex(BEACON_IdDestino)));

        model.setUniqueId(cursor.getString(cursor
                .getColumnIndex(BEACON_UniqueId)));

        model.setMajorId(cursor.getString(cursor
                .getColumnIndex(BEACON_MajorId)));

        model.setMinorId(cursor.getString(cursor
                .getColumnIndex(BEACON_MajorId)));

        model.setDescricao(cursor.getString(cursor
                .getColumnIndex(BEACON_Descricao)));

        return model;
    }

    private JsonObject createJsonObject(BeaconModel model) {
        Gson g = new Gson();
        return g.fromJson(g.toJson(model), JsonObject.class);
    }
}
