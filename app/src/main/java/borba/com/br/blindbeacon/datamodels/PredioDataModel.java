package borba.com.br.blindbeacon.datamodels;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

    public void LoadWithFakeData(){
        Log.w("Database", "PredioDataModel - Start Fake Data");

        PredioModel p1 = new PredioModel();
        p1.setId(1);
        p1.setNome("Centro D 06");
        p1.setDescricao("Prédio de Exatas");

        PredioModel p2 = new PredioModel();
        p1.setId(2);
        p2.setNome("Centro D 07");
        p2.setDescricao("Prédio de Exatas");

        PredioModel p3 = new PredioModel();
        p1.setId(3);
        p3.setNome("Centro D 08");
        p3.setDescricao("Prédio de Exatas");

        this.addPredio(p1);
        this.addPredio(p2);
        this.addPredio(p3);

        Log.w("Database", "PredioDataModel - End Fake Data");
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

    public void addPredio(PredioModel model){
        final ContentValues values = new ContentValues();

        //values.put(PREDIO_ID, model.getId());
        values.put(PREDIO_NOME, model.getNome());
        values.put(PREDIO_DESCRICAO, model.getDescricao());

        database = dbHandler.getWritableDatabase();

        try {
            database.insert(TABLE_NAME, null, values);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        closeDataBaseConnection();
    }

    public ArrayList<PredioModel> getAll(){
        final String query = "SELECT * FROM " + TABLE_NAME;
        database = dbHandler.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = database.rawQuery(query, null);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        final ArrayList<PredioModel> list = cursorToArrayList(cursor);

        closeDataBaseConnection();
        return list;
    }

    private ArrayList<PredioModel> cursorToArrayList(final Cursor cursor) {
        final ArrayList<PredioModel> list = new ArrayList<PredioModel>();

        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    final PredioModel dado = cursorToData(cursor);

                    list.add(dado);

                } while (cursor.moveToNext());
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return list;
    }

    private PredioModel cursorToData(Cursor cursor) {
        final PredioModel model = new PredioModel();

        model.setId(cursor.getInt(cursor
                .getColumnIndex(PREDIO_ID)));

        model.setNome(cursor.getString(cursor
                .getColumnIndex(PREDIO_NOME)));

        model.setDescricao(cursor.getString(cursor
                .getColumnIndex(PREDIO_DESCRICAO)));

        return model;
    }

    private JsonObject createJsonObject(PredioModel model) {
        Gson g = new Gson();
        return g.fromJson(g.toJson(model), JsonObject.class);
    }

}
