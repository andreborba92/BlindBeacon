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
import borba.com.br.blindbeacon.models.RotaModel;

/**
 * Created by andre on 07/04/2017.
 */

public class RotaDataModel{

    // Declaracao da tabela de Rotas
    public final String TABLE_NAME = "Rota";
    private final String ROTA_IdRota = "IdRota";
    private final String ROTA_IdPredio = "IdPredio";
    private final String ROTA_IdDestino = "IdDestino";
    private final String ROTA_Ordem = "Ordem";
    private final String ROTA_OrientacaoFrente = "OrientacaoFrente";
    private final String ROTA_OrientacaoTras = "OrientacaoTras";
    private final String ROTA_Metragem = "Metragem";

    //Instacias da base
    private SQLiteDatabase database;
    private final DataBaseHandler dbHandler;

    public RotaDataModel(final Context context){
        dbHandler = new DataBaseHandler(context);
    }

    public void LoadWithFakeData(){
        Log.w("Database", "RotaDataModel - Start Fake Data");

        //public RotaModel(int IdPredio, int IdDestino, int Ordem, String OrientacaoF, String OrientacaoT, double Metragem){
        this.addRota(new RotaModel(1, 1, 1, "Siga em frente. Próximo ponto: {NOME}", "Você chegou ao início do prédio", 0));
        this.addRota(new RotaModel(1, 2, 2, "Siga em frente. Próximo ponto: {NOME}", "Siga em frente. Próximo ponto: {NOME}", 6.15));
        this.addRota(new RotaModel(1, 3, 3, "Cuidado que terá um degrau em aproximadamente 6 metros", "Siga em frente. Próximo ponto: {NOME}", 11.7));
        this.addRota(new RotaModel(1, 7, 4, "", "", 17.1)); //Obstáculo
        this.addRota(new RotaModel(1, 4, 5, "Siga em frente. Próximo ponto: {NOME}", "Cuidado que terá um degrau em aproximadamente 6 metros", 22.65));
        this.addRota(new RotaModel(1, 5, 6, "Siga em frente. Próximo ponto: {NOME}", "Siga em frente. Próximo ponto: {NOME}", 28.05));
        this.addRota(new RotaModel(1, 6, 7, "Você chegou ao final do prédio", "Siga em frente. Próximo ponto: {NOME}", 33.45));

        Log.w("Database", "RotaDataModel - End Fake Data");
    }

    public void LoadWithFakeDataUnisinos(){
        Log.w("Database", "RotaDataModel - Start Fake Data Unisinos");

        //public RotaModel(int IdPredio, int IdDestino, int Ordem, String OrientacaoF, String OrientacaoT, double Metragem){
        this.addRota(new RotaModel(1, 1, 1, "Cuidado que terá uma porta em aproximadamente X metros", "Você chegou ao final do prédio", 0));
        this.addRota(new RotaModel(1, 2, 2, "", "", 8)); //Obstáculo
        this.addRota(new RotaModel(1, 3, 3, "Siga em frente. Próximo ponto: {NOME}", "Cuidado que terá uma porta em aproximadamente X metros", 15.77));
        this.addRota(new RotaModel(1, 4, 4, "Siga em frente. Próximo ponto: {NOME}", "Siga em frente. Próximo ponto: {NOME}", 26.85));
        this.addRota(new RotaModel(1, 5, 5, "Siga em frente. Próximo ponto: {NOME}", "Siga em frente. Próximo ponto: {NOME}", 37.84));
        this.addRota(new RotaModel(1, 6, 6, "Você chegou ao final do prédio", "Siga em frente. Próximo ponto: {NOME}", 49.2));

        Log.w("Database", "RotaDataModel - End Fake Data Unisinos");
    }

    private void closeDataBaseConnection() {
        if (database.isOpen()) {
            database.close();
        }
    }

    public String getCreateScript(){
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + ROTA_IdRota + " INTEGER PRIMARY KEY, " +
                ROTA_IdPredio + " INT, " + ROTA_IdDestino + " INT, " + ROTA_Ordem + " INT, " +
                ROTA_OrientacaoFrente + " TEXT, " + ROTA_OrientacaoTras + " TEXT, " +
                ROTA_Metragem + " REAL)";

        return CREATE_TABLE;
    }

    public void addRota(RotaModel model){
        final ContentValues values = new ContentValues();

        values.put(ROTA_IdPredio, model.getIdPredio());
        values.put(ROTA_IdDestino, model.getIdDestino());
        values.put(ROTA_Ordem, model.getOrdem());
        values.put(ROTA_OrientacaoFrente, model.getOrientacaoFrente());
        values.put(ROTA_OrientacaoTras, model.getOrientacaoTras());
        values.put(ROTA_Metragem, model.getMetragem());

        database = dbHandler.getWritableDatabase();

        try {
            database.insert(TABLE_NAME, null, values);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        closeDataBaseConnection();
    }

    public ArrayList<RotaModel> getAll(){
        final String query = "SELECT * FROM " + TABLE_NAME;
        database = dbHandler.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = database.rawQuery(query, null);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        final ArrayList<RotaModel> list = cursorToArrayList(cursor);

        closeDataBaseConnection();
        return list;
    }

    public ArrayList<RotaModel> getAllByPredioId(int idPredio){
        final String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ROTA_IdPredio + " = " + idPredio +
                " ORDER BY " + ROTA_Ordem;
        database = dbHandler.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = database.rawQuery(query, null);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        final ArrayList<RotaModel> list = cursorToArrayList(cursor);

        closeDataBaseConnection();
        return list;
    }

    private ArrayList<RotaModel> cursorToArrayList(final Cursor cursor) {
        final ArrayList<RotaModel> list = new ArrayList<RotaModel>();

        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    final RotaModel dado = cursorToData(cursor);

                    list.add(dado);

                } while (cursor.moveToNext());
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return list;
    }

    private RotaModel cursorToData(Cursor cursor) {
        final RotaModel model = new RotaModel();

        model.setIdRota(cursor.getInt(cursor
                .getColumnIndex(ROTA_IdRota)));

        model.setIdPredio(cursor.getInt(cursor
                .getColumnIndex(ROTA_IdPredio)));

        model.setIdDestino(cursor.getInt(cursor
                .getColumnIndex(ROTA_IdDestino)));

        model.setOrdem(cursor.getInt(cursor
                .getColumnIndex(ROTA_Ordem)));

        model.setOrientacaoFrente(cursor.getString(cursor
                .getColumnIndex(ROTA_OrientacaoFrente)));

        model.setOrientacaoTras(cursor.getString(cursor
                .getColumnIndex(ROTA_OrientacaoTras)));

        model.setMetragem(cursor.getDouble(cursor
                .getColumnIndex(ROTA_Metragem)));

        return model;
    }

    private JsonObject createJsonObject(RotaModel model) {
        Gson g = new Gson();
        return g.fromJson(g.toJson(model), JsonObject.class);
    }

}
