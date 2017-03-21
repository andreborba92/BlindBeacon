package borba.com.br.blindbeacon.datamodels;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import borba.com.br.blindbeacon.database.DataBaseHandler;
import borba.com.br.blindbeacon.models.LogApp;
import borba.com.br.blindbeacon.models.PredioModel;

/**
 * Created by andre on 20/03/2017.
 */

public class PredioDataModel {
    // Declaracao da tabela de Predios
    public final String TABLE_NAME = "predio";
    private final String PREDIO_ID = "Id";
    private final String PREDIO_NOME = "Nome";
    private final String PREDIO_DESCRICAO = "Descricao";

    //Instacias da base
    private SQLiteDatabase database;
    private final DataBaseHandler dbHandler;

    public PredioDataModel(final Context context){
        dbHandler = new DataBaseHandler(context);
    }

    private void closeDataBaseConnection() {
        if (database.isOpen()) {
            database.close();
        }
    }

    public String getCreateScript(){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + PREDIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PREDIO_NOME + " TEXT, " + PREDIO_DESCRICAO + " TEXT)" ;

        return CREATE_TABLE;
    }

    public void addPredio(PredioModel predio){
        final ContentValues values = new ContentValues();

        values.put(PREDIO_ID, predio.getId());
        values.put(PREDIO_NOME, predio.getNome());
        values.put(PREDIO_DESCRICAO, predio.getDescricao());

        database = dbHandler.getWritableDatabase();

        try {
            database.insert(TABLE_NAME, null, values);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        closeDataBaseConnection();
    }

    public ArrayList<PredioModel> getAllPredios(){
        final String query = "SELECT * FROM " + TABLE_NAME;
        database = dbHandler.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = database.rawQuery(query, null);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        final ArrayList<PredioModel> predios = cursorToArrayList(cursor);

        closeDataBaseConnection();
        return predios;
    }

    private ArrayList<PredioModel> cursorToArrayList(final Cursor cursor) {
        final ArrayList<PredioModel> predioList = new ArrayList<PredioModel>();

        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    final PredioModel predio = cursorToData(cursor);

                    predioList.add(predio);

                } while (cursor.moveToNext());
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return predioList;
    }

    private PredioModel cursorToData(Cursor cursor) {
        final PredioModel predio = new PredioModel();

        predio.setId(cursor.getInt(cursor
                .getColumnIndex(PREDIO_ID)));

        predio.setNome(cursor.getString(cursor
                .getColumnIndex(PREDIO_NOME)));

        predio.setDescricao(cursor.getString(cursor
                .getColumnIndex(PREDIO_DESCRICAO)));

        return predio;
    }

    private JsonObject createJsonObject(PredioModel predio) {
        Gson g = new Gson();
        return g.fromJson(g.toJson(predio), JsonObject.class);
    }

}
