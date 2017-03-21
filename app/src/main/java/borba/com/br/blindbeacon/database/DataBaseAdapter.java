package borba.com.br.blindbeacon.database;

/**
 * Created by andre on 16/03/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import borba.com.br.blindbeacon.models.LogApp;

public class DataBaseAdapter {
    private final String logTable = "";
    private SQLiteDatabase database;
    private final DataBaseHandler dbHandler;

    public DataBaseAdapter(final Context context) {
        dbHandler = new DataBaseHandler(context);
    }

    private void closeDataBaseConnection() {
        if (database.isOpen()) {
            database.close();
        }
    }

    /**
     * Remove todos os logs da base de dados local
     */
    public void removeLogs(){
        int c = dbHandler.TruncateTable(logTable);
        dbHandler.Vacuum();
    }

    /**
     * Busca todos os logs armazenados em banco.
     * @return
     */
    public ArrayList<LogApp> getLogs() {
        final String query = "SELECT * FROM " + logTable;
        database = dbHandler.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = database.rawQuery(query, null);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        final ArrayList<LogApp> logApps = cursorToArrayListLogs(cursor);

        closeDataBaseConnection();
        return logApps;
    }

    private ArrayList<LogApp> cursorToArrayListLogs(final Cursor cursor) {
        final ArrayList<LogApp> logAppList = new ArrayList<LogApp>();

        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    final LogApp logs = cursorToLog(cursor);

                    logAppList.add(logs);

                } while (cursor.moveToNext());
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return logAppList;
    }

    /**
     * Adiciona um item do cursor do banco para virar um log
     * @param cursor
     * @return
     */
    private LogApp cursorToLog(Cursor cursor) {
        final LogApp logApp = new LogApp();

//        logApp.setData(cursor.getString(cursor
//                .getColumnIndex(DataBaseHandler.LOG_DATA)));
//
//        logApp.setMensagem(cursor.getString(cursor
//                .getColumnIndex(DataBaseHandler.LOG_MENSAGEM)));
//
//        logApp.setDetalhes(cursor.getString(cursor
//                .getColumnIndex(DataBaseHandler.LOG_DETALHES)));
//
//        logApp.setExcecao(cursor.getString(cursor
//                .getColumnIndex(DataBaseHandler.LOG_EXCECAO)));
//
//        logApp.setOrigem(cursor.getString(cursor
//                .getColumnIndex(DataBaseHandler.LOG_ORIGEM)));
//
//        logApp.setPlataformaOperacional(cursor.getString(cursor
//                .getColumnIndex(DataBaseHandler.LOG_PLATAFORMA)));
//
//        logApp.setTipo(cursor.getString(cursor
//                .getColumnIndex(DataBaseHandler.LOG_TIPO)));
//
//        logApp.setUrlRequisicao(cursor.getString(cursor
//                .getColumnIndex(DataBaseHandler.LOG_URL_REQUSICAO)));
//
//        logApp.setVersaoApp(cursor.getString(cursor
//                .getColumnIndex(DataBaseHandler.LOG_VERSAO_APP)));

        return logApp;
    }

    /**
     * Adiciona um logApp na base local
     * @param logApp
     */
    public void addLog(final LogApp logApp) {
        final ContentValues values = new ContentValues();
//
//        values.put(DataBaseHandler.LOG_DATA, logApp.getData());
//        values.put(DataBaseHandler.LOG_DETALHES, logApp.getDetalhes());
//        values.put(DataBaseHandler.LOG_EXCECAO, logApp.getExcecao());
//        values.put(DataBaseHandler.LOG_MENSAGEM, logApp.getMensagem());
//        values.put(DataBaseHandler.LOG_ORIGEM, logApp.getOrigem());
//        values.put(DataBaseHandler.LOG_TIPO, logApp.getTipo());
//        values.put(DataBaseHandler.LOG_URL_REQUSICAO, logApp.getUrlRequisicao());
//        values.put(DataBaseHandler.LOG_VERSAO_APP, logApp.getVersaoApp());
//        values.put(DataBaseHandler.LOG_PLATAFORMA, logApp.getPlataformaOperacional());

        database = dbHandler.getWritableDatabase();

        try {
            database.insert(logTable, null, values);
        } catch (final Exception e) {
            e.printStackTrace();
        }

        closeDataBaseConnection();
    }

    /**
     * Busca a quantidade de logs armazenados localmente
     * @return
     */
    public int getLogCount() {
        final String countQuery = "SELECT * FROM " + logTable;
        database = dbHandler.getReadableDatabase();
        final Cursor cursor = database.rawQuery(countQuery, null);
        int count = 0;

        try {
            if (cursor.moveToFirst()) {
                count = cursor.getCount();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            closeDataBaseConnection();
        }
        return count;
    }
}
