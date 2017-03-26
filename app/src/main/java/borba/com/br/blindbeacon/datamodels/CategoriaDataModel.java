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
import borba.com.br.blindbeacon.enums.CategoriaEnum;
import borba.com.br.blindbeacon.models.CategoriaModel;

/**
 * Created by andre on 22/03/2017.
 */

public class CategoriaDataModel {
    // Declaracao da tabela de Predios
    public final String TABLE_NAME = "categoria";
    private final String CATEGORIA_ID = "Id";
    private final String CATEGORIA_NOME = "Nome";

    //Instacias da base
    private SQLiteDatabase database;
    private final DataBaseHandler dbHandler;

    public CategoriaDataModel(final Context context){
        dbHandler = new DataBaseHandler(context);
    }

    public void LoadWithFakeData(){
        Log.w("Database", "CategoriaDataModel - Start Fake Data");

        this.addCategoria(new CategoriaModel(CategoriaEnum.SALA_DE_AULA.getValue(), CategoriaEnum.SALA_DE_AULA.toString()));
        this.addCategoria(new CategoriaModel(CategoriaEnum.BANHEIRO.getValue(), CategoriaEnum.BANHEIRO.toString()));
        this.addCategoria(new CategoriaModel(CategoriaEnum.XEROX.getValue(), CategoriaEnum.XEROX.toString()));
        this.addCategoria(new CategoriaModel(CategoriaEnum.AUDITORIO.getValue(), CategoriaEnum.AUDITORIO.toString()));
        this.addCategoria(new CategoriaModel(CategoriaEnum.CAFE.getValue(), CategoriaEnum.CAFE.toString()));
        this.addCategoria(new CategoriaModel(CategoriaEnum.SECRETARIA.getValue(), CategoriaEnum.SECRETARIA.toString()));
        this.addCategoria(new CategoriaModel(CategoriaEnum.OUTRO.getValue(), CategoriaEnum.OUTRO.toString()));

        Log.w("Database", "CategoriaDataModel - End Fake Data");
    }

    private void closeDataBaseConnection() {
        if (database.isOpen()) {
            database.close();
        }
    }

    public String getCreateScript(){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + CATEGORIA_ID + " INTEGER PRIMARY KEY, " +
                CATEGORIA_NOME + " TEXT)" ;

        return CREATE_TABLE;
    }

    public void addCategoria(CategoriaModel model){
        final ContentValues values = new ContentValues();

        values.put(CATEGORIA_ID, model.getId());
        values.put(CATEGORIA_NOME, model.getNome());

        database = dbHandler.getWritableDatabase();

        try {
            database.insert(TABLE_NAME, null, values);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        closeDataBaseConnection();
    }

    public ArrayList<CategoriaModel> getAll(){
        final String query = "SELECT * FROM " + TABLE_NAME;
        database = dbHandler.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = database.rawQuery(query, null);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        final ArrayList<CategoriaModel> categoria = cursorToArrayList(cursor);

        closeDataBaseConnection();
        return categoria;
    }

    private ArrayList<CategoriaModel> cursorToArrayList(final Cursor cursor) {
        final ArrayList<CategoriaModel> categoriaList = new ArrayList<CategoriaModel>();

        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    final CategoriaModel categoria = cursorToData(cursor);

                    categoriaList.add(categoria);

                } while (cursor.moveToNext());
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return categoriaList;
    }

    private CategoriaModel cursorToData(Cursor cursor) {
        final CategoriaModel model = new CategoriaModel();

        model.setId(cursor.getInt(cursor
                .getColumnIndex(CATEGORIA_ID)));

        model.setNome(cursor.getString(cursor
                .getColumnIndex(CATEGORIA_NOME)));

        return model;
    }

    private JsonObject createJsonObject(CategoriaModel model) {
        Gson g = new Gson();
        return g.fromJson(g.toJson(model), JsonObject.class);
    }
}
