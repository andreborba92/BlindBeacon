package borba.com.br.blindbeacon.database;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.util.Log;

import org.altbeacon.beacon.BeaconData;

import java.util.ArrayList;

import borba.com.br.blindbeacon.datamodels.BeaconDataModel;
import borba.com.br.blindbeacon.datamodels.CategoriaDataModel;
import borba.com.br.blindbeacon.datamodels.DestinoDataModel;
import borba.com.br.blindbeacon.datamodels.PredioDataModel;
import borba.com.br.blindbeacon.datamodels.RelacionaentoBeaconsDataModel;
import borba.com.br.blindbeacon.datamodels.TipoDestinoDataModel;
import borba.com.br.blindbeacon.models.CategoriaModel;
import borba.com.br.blindbeacon.models.DestinoModel;
import borba.com.br.blindbeacon.models.PredioModel;
import borba.com.br.blindbeacon.models.TipoDestinoModel;

/**
 * Created by andre on 16/03/2017.
 */

public class DataBaseHandler extends SQLiteOpenHelper {

    private Context context;
    private Boolean _dataModelsInitialized = false;

    public DataBaseHandler(final Context context) {
        super(context, BLINDBEACON_DATABASE_NAME, null, BLINDBEACON_DATABASE_VERSION);
        this.context = context;
    }

    // Declaracao do banco de dados
    private static final String BLINDBEACON_DATABASE_NAME = "BLINDBEACON_DATABASE";
    private static final int BLINDBEACON_DATABASE_VERSION = 5;

    //DataModels
    private TipoDestinoDataModel _tipoDestinoDataModel;
    private CategoriaDataModel _categoriaDataModel;
    private PredioDataModel _predioDataModel;
    private DestinoDataModel _destinoDataModel;
    private BeaconDataModel _beaconDataModel;
    private RelacionaentoBeaconsDataModel _relacionaentoBeaconsDataModel;

    // Criando tabelas
    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.v("Database", "onCreate Start.");

        InitializeInstancesDataModels();

        String PredioCreateScript = _predioDataModel.getCreateScript();
        db.execSQL(PredioCreateScript);

        String TipoDestinoScript = _tipoDestinoDataModel.getCreateScript();
        db.execSQL(TipoDestinoScript);

        String CategoriaScript = _categoriaDataModel.getCreateScript();
        db.execSQL(CategoriaScript);

        String DestinoScript = _destinoDataModel.getCreateScript();
        db.execSQL(DestinoScript);

        String BeaconScript = _beaconDataModel.getCreateScript();
        db.execSQL(BeaconScript);

        String RelacionamentoBeaconScript = _relacionaentoBeaconsDataModel.getCreateScript();
        db.execSQL(RelacionamentoBeaconScript);

        Log.v("Database", "onCreate Finish.");
        Log.v("Database", "Base criada com sucesso!");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {

        // Dropa tabelas antigas, se necessario
        Log.v("Database", "onUpgrade Start.");

        InitializeInstancesDataModels();

        db.execSQL("DROP TABLE IF EXISTS " + _relacionaentoBeaconsDataModel.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + _beaconDataModel.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + _destinoDataModel.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + _tipoDestinoDataModel.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + _categoriaDataModel.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + _predioDataModel.TABLE_NAME);

        Log.v("Database", "onUpgrade Finish.");

        // Recria as tabelas
        onCreate(db);
    }

    public void LoadWithFakeData(){
        InitializeInstancesDataModels();

        Log.v("Database", "onLoadWithFakeData Start.");

        this._tipoDestinoDataModel.LoadWithFakeData();
        this._categoriaDataModel.LoadWithFakeData();
        this._predioDataModel.LoadWithFakeData();
        this._destinoDataModel.LoadWithFakeData();
        this._beaconDataModel.LoadWithFakeData();
        this._relacionaentoBeaconsDataModel.LoadWithFakeData();

        Log.v("Database", "onLoadWithFakeData Finish.");
    }

    public void TestFakeData(){
        InitializeInstancesDataModels();

        ArrayList<TipoDestinoModel> listTipoDestino = _tipoDestinoDataModel.getAll();
        ArrayList<CategoriaModel> listcategorias = _categoriaDataModel.getAll();
        ArrayList<PredioModel> listPredio = _predioDataModel.getAll();
        ArrayList<DestinoModel> listDestino = _destinoDataModel.getAll(1);

        Log.w("Database", "Quantidade Tipo Destino: " + listTipoDestino.size());
        Log.w("Database", "Quantidade Categoria: " + listcategorias.size());
        Log.w("Database", "Quantidade Predio: " + listPredio.size());
        Log.w("Database", "Quantidade Destino: " + listDestino.size());
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

    private void InitializeInstancesDataModels(){

        if(_dataModelsInitialized)
            return;

        this._tipoDestinoDataModel = new TipoDestinoDataModel(this.context);
        this._categoriaDataModel = new CategoriaDataModel(this.context);
        this._predioDataModel = new PredioDataModel(this.context);
        this._destinoDataModel = new DestinoDataModel(this.context);
        this._beaconDataModel = new BeaconDataModel(this.context);
        this._relacionaentoBeaconsDataModel = new RelacionaentoBeaconsDataModel(this.context);

        _dataModelsInitialized = true;
    }
}

