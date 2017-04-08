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
import borba.com.br.blindbeacon.enums.TipoDestinoEnum;
import borba.com.br.blindbeacon.models.DestinoModel;

/**
 * Created by andre on 22/03/2017.
 */

public class DestinoDataModel {
    // Declaracao da tabela de Predios
    public final String TABLE_NAME = "destino";
    private final String DESTINO_ID = "IdDestino";
    private final String DESTINO_IdPredio = "IdPredio";
    private final String DESTINO_IdTipoDestino = "IdTipoDestino";
    private final String DESTINO_IdCategoria = "IdCategoria";
    private final String DESTINO_NOME = "Nome";
    private final String DESTINO_DESCRICAO = "Descricao";
    private final String DESTINO_UniqueId = "UniqueId";
    private final String DESTINO_MajorId = "MajorId";
    private final String DESTINO_MinorId = "MinorId";

    //Instacias da base
    private SQLiteDatabase database;
    private final DataBaseHandler dbHandler;

    public DestinoDataModel(final Context context){
        dbHandler = new DataBaseHandler(context);
    }

    public void LoadWithFakeData(){
        Log.w("Database", "DestinoDataModel - Start Fake Data");

        //public DestinoModel( IdPredio, IdTipoDestino, IdCategoria, Nome, UniqueId, MajorId, MinorId,  Descricao ){
        this.addDestino(new DestinoModel(1, 1, TipoDestinoEnum.DESTINO.getValue(), CategoriaEnum.SALA_DE_AULA.getValue(),
                "Ponto 1", "003e8c80-ea01-4ebb-b888-78da19df9e55", "893", "2", "Beacon Cinza 2"));

        this.addDestino(new DestinoModel(2, 1, TipoDestinoEnum.DESTINO.getValue(), CategoriaEnum.SALA_DE_AULA.getValue(),
                "Ponto 2", "003e8c80-ea01-4ebb-b888-78da19df9e55", "893", "88", "Beacon Cinza 88"));

        this.addDestino(new DestinoModel(3, 1, TipoDestinoEnum.DESTINO.getValue(), CategoriaEnum.SALA_DE_AULA.getValue(),
                "Ponto 3", "5AFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF", "704", "705", "Cel Nando Antigo"));

        this.addDestino(new DestinoModel(4, 1, TipoDestinoEnum.DESTINO.getValue(), CategoriaEnum.BANHEIRO.getValue(),
                "Ponto 4", "003e8c80-ea01-4ebb-b888-78da19df9e55", "893", "148", "Beacon Cinza 148"));

        this.addDestino(new DestinoModel(5, 1, TipoDestinoEnum.DESTINO.getValue(), CategoriaEnum.SALA_DE_AULA.getValue(),
                "Ponto 5", "52414449-5553-4e45-5457-4f524b53434f", "6", "5", "Cel Nando"));

        this.addDestino(new DestinoModel(6, 1, TipoDestinoEnum.DESTINO.getValue(), CategoriaEnum.XEROX.getValue(),
                "Ponto 6", "2f234454-cf6d-4a0f-adf2-f4911ba9ffa6", "55", "44", "Cel Pai"));

        this.addDestino(new DestinoModel(7, 1, TipoDestinoEnum.OBSTACULO.getValue(), CategoriaEnum.OUTRO.getValue(),
                "Degrau", "699ebc80-e1f3-11e3-9a0f-0cf3ee3bc012", "1", "20681", "beacon branco"));

        //-----------------------------------------------------------------------------
        //Sem Dispositivos até o momento

//        this.addDestino(new DestinoModel(8, 1, TipoDestinoEnum.DESTINO.getValue(), CategoriaEnum.SALA_DE_AULA.getValue(),
//                "Sala 120", "11111", "11", "11", "descrição"));
//
//        this.addDestino(new DestinoModel(9, 1, TipoDestinoEnum.DESTINO.getValue(), CategoriaEnum.SALA_DE_AULA.getValue(),
//                "Sala 199", "22222", "22", "22", "descrição"));
//
//        this.addDestino(new DestinoModel(10, 1, TipoDestinoEnum.DESTINO.getValue(), CategoriaEnum.SECRETARIA.getValue(),
//                "Secretaria Geral","33333", "33", "33",  "descrição"));
//
//        this.addDestino(new DestinoModel(11, 1, TipoDestinoEnum.OBSTACULO.getValue(), CategoriaEnum.OUTRO.getValue(),
//                "Degrau", "44444", "44", "44", "descrição"));
//
//        this.addDestino(new DestinoModel(12, 1, TipoDestinoEnum.CONEXAO.getValue(), CategoriaEnum.OUTRO.getValue(),
//                "Corredor", "55555", "55", "55", "descrição"));
//
//        this.addDestino(new DestinoModel(13, 1, TipoDestinoEnum.CONEXAO.getValue(), CategoriaEnum.OUTRO.getValue(),
//                "Corredor", "66666", "66", "66", "descrição"));

        /*
        //Disp branco
        this.addBeacon(new aBeaconModel("699ebc80-e1f3-11e3-9a0f-0cf3ee3bc012","1","20681",1,""));

        //Disp cinzas
        this.addBeacon(new aBeaconModel("003e8c80-ea01-4ebb-b888-78da19df9e55","893","2",1,""));
        this.addBeacon(new aBeaconModel("003e8c80-ea01-4ebb-b888-78da19df9e55","893","88",1,""));
        this.addBeacon(new aBeaconModel("003e8c80-ea01-4ebb-b888-78da19df9e55","893","148",1,""));

        //Cel Nando
        this.addBeacon(new aBeaconModel("52414449-5553-4e45-5457-4f524b53434f","6","5",1,""));

        //Cel Pai
        this.addBeacon(new aBeaconModel("2f234454-cf6d-4a0f-adf2-f4911ba9ffa6","55","44",1,""));
        */

        Log.w("Database", "DestinoDataModel - End Fake Data");
    }

    private void closeDataBaseConnection() {
        if (database.isOpen()) {
            database.close();
        }
    }

    public String getCreateScript(){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + DESTINO_ID + " INTEGER PRIMARY KEY, " +
                DESTINO_NOME + " TEXT, " + DESTINO_DESCRICAO + " TEXT, " + DESTINO_IdPredio + " INT, " +
                DESTINO_UniqueId + " TEXT, " + DESTINO_MajorId + " TEXT, " + DESTINO_MinorId + " TEXT, " +
                DESTINO_IdTipoDestino + " INT, " + DESTINO_IdCategoria + " INT)";

        return CREATE_TABLE;
    }

    public void addDestino(DestinoModel model){
        final ContentValues values = new ContentValues();

        values.put(DESTINO_ID, model.getIdDestino());
        values.put(DESTINO_NOME, model.getNome());
        values.put(DESTINO_DESCRICAO, model.getDescricao());
        values.put(DESTINO_IdPredio, model.getIdPredio());
        values.put(DESTINO_IdTipoDestino, model.getIdTipoDestino());
        values.put(DESTINO_IdCategoria, model.getIdCategoria());
        values.put(DESTINO_UniqueId, model.getUniqueId());
        values.put(DESTINO_MajorId, model.getMajorId());
        values.put(DESTINO_MinorId, model.getMinorId());

        database = dbHandler.getWritableDatabase();

        try {
            database.insert(TABLE_NAME, null, values);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        closeDataBaseConnection();
    }

    public ArrayList<DestinoModel> getAll(int idPredio){
        final String query = "SELECT * FROM " + TABLE_NAME;
        database = dbHandler.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = database.rawQuery(query, null);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        final ArrayList<DestinoModel> list = cursorToArrayList(cursor, idPredio);

        closeDataBaseConnection();
        return list;
    }

    public DestinoModel getByBeacon(ArrayList<DestinoModel> destinosList, String UniqueId, String MajorId, String MinorId){
        for (DestinoModel vm:destinosList) {
            if(vm.getUniqueId().equals(UniqueId) && vm.getMajorId().equals(MajorId) && vm.getMinorId().equals(MinorId))
                return vm;
        }

        return null;
    }

    public ArrayList<DestinoModel> getAll_ApenasDestinos(int idPredio){
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + DESTINO_IdTipoDestino + " = " +
                TipoDestinoEnum.DESTINO.getValue();
        database = dbHandler.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = database.rawQuery(query, null);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        final ArrayList<DestinoModel> list = cursorToArrayList(cursor, idPredio);

        closeDataBaseConnection();
        return list;
    }

    private ArrayList<DestinoModel> cursorToArrayList(final Cursor cursor, int idPredio) {
        final ArrayList<DestinoModel> list = new ArrayList<DestinoModel>();

        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    final DestinoModel dado = cursorToData(cursor);

                    if(dado.getIdPredio() != idPredio)
                        continue;

                    list.add(dado);

                } while (cursor.moveToNext());
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return list;
    }

    private DestinoModel cursorToData(Cursor cursor) {
        final DestinoModel model = new DestinoModel();

        model.setIdDestino(cursor.getInt(cursor
                .getColumnIndex(DESTINO_ID)));

        model.setNome(cursor.getString(cursor
                .getColumnIndex(DESTINO_NOME)));

        model.setDescricao(cursor.getString(cursor
                .getColumnIndex(DESTINO_DESCRICAO)));

        model.setIdPredio(cursor.getInt(cursor
                .getColumnIndex(DESTINO_IdPredio)));

        model.setIdTipoDestino(cursor.getInt(cursor
                .getColumnIndex(DESTINO_IdTipoDestino)));

        model.setIdCategoria(cursor.getInt(cursor
                .getColumnIndex(DESTINO_IdCategoria)));

        model.setUniqueId(cursor.getString(cursor
                .getColumnIndex(DESTINO_UniqueId)));

        model.setMajorId(cursor.getString(cursor
                .getColumnIndex(DESTINO_MajorId)));

        model.setMinorId(cursor.getString(cursor
                .getColumnIndex(DESTINO_MinorId)));

        return model;
    }

    private JsonObject createJsonObject(DestinoModel model) {
        Gson g = new Gson();
        return g.fromJson(g.toJson(model), JsonObject.class);
    }
}
