package borba.com.br.blindbeacon.datamodels;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;

import borba.com.br.blindbeacon.database.DataBaseAdapter;
import borba.com.br.blindbeacon.models.LogApp;

/**
 * Created by andre on 16/03/2017
 */
public class LogsPlataformaCAP {

    private Context ctx;
    private DataBaseAdapter dbAdapter;
//    private PlataformaAPI api;

//    public Logs(Context ctx, DataBaseAdapter dbAdapter, PlataformaAPI api) {
//        this.ctx = ctx;
//        this.dbAdapter = dbAdapter;
//        this.api = api;
//    }

    /**
     * Cria uma nova entrada de log model
     *
     * @param ex
     * @param url
     * @param message
     * @param details
     * @param tipo
     * @return
     */
    private LogApp newLog(Exception ex, String url, String message, String details, String tipo) {
        Writer writer = new StringWriter();
        if (ex != null) {
            ex.printStackTrace(new PrintWriter(writer));
        }

        LogApp logApp = new LogApp();
        //logApp.setData(Formatter.formatDateTime(new Date()));
        logApp.setMensagem(message);
        logApp.setExcecao(writer.toString());
        logApp.setDetalhes(details);
        logApp.setUrlRequisicao(url);
        //Origem
        //Plataforma
        logApp.setTipo(tipo);
        //logApp.setVersaoApp(DeviceConfiguration.getVersaoApp(ctx));

        return logApp;
    }

    /**
     * Transforma a entidade de logApp em um objeto json para envio para a API
     *
     * @param logApp
     * @return
     */
    private JsonObject createLogJsonObject(LogApp logApp) {
        Gson g = new Gson();
        return g.fromJson(g.toJson(logApp), JsonObject.class);
    }

    /**
     * Limpa a lista de logs da base local
     */
    public void clearLocalDatabase() {
        dbAdapter.removeLogs();
    }

    /**
     * Sincroniza os logs de exceção do sistema e limpa a base local
     *
     * @param showQuantity
     */
    public void syncLogs(boolean showQuantity) {
        int quantidadeDeLogs = dbAdapter.getLogCount();

        if (showQuantity) {
            Toast.makeText(ctx, "Quantidade de logs a serem sincronizados: " + quantidadeDeLogs, Toast.LENGTH_LONG).show();
        }

        if (quantidadeDeLogs > 0) {
            ArrayList<LogApp> logApps;// = new ArrayList<LogApp>();

            logApps = dbAdapter.getLogs();

            for (int i = 0; i < logApps.size(); i++) {
                //api.EnviarLog(this.createLogJsonObject(logApps.get(i)), logApps.get(i).getTipo());
            }

            this.clearLocalDatabase();
        }
    }

    /**
     * Cria um log e envia para o servidor
     *
     * @param ex
     * @param url
     * @param message
     * @param details
     * @param tipo
     */
    private void createServerLog(Exception ex, String url, String message, String details, String tipo) {
        LogApp logApp = this.newLog(ex, url, message, details, tipo);
        //api.EnviarLog(this.createLogJsonObject(logApp), logApp.getTipo());
    }

    /**
     * Cria um log e salva na base local
     *
     * @param ex
     * @param url
     * @param message
     * @param details
     * @param tipo
     */
    private void createLocalLog(Exception ex, String url, String message, String details, String tipo) {
        LogApp logApp = this.newLog(ex, url, message, details, tipo);
        dbAdapter.addLog(logApp);
    }

    /**
     * Gera uma nova instancia de log, e tem a inteligencia para enviar para o servidor ou armazenar local
     *
     * @param ex
     * @param url
     * @param message
     * @param details
     * @param tipo
     */
    public void generateLog(Exception ex, String url, String message, String details, String tipo) {
        try {
//            if (((MainActivity) ctx).isConnected())
//                this.createServerLog(ex, url, message, details, tipo);
//            else
//                this.createLocalLog(ex, url, message, details, tipo);
        }catch (ClassCastException exception){
            this.createLocalLog(ex, url, message, details, tipo);
        }
    }

    /**
     * Gera uma nova instancia de log,e salva na base local
     * utilizado quando nao houver comunicação com api
     *
     * @param ex
     * @param url
     * @param message
     * @param details
     * @param tipo
     */
    public void generateLogOffline(Exception ex, String url, String message, String details, String tipo) {
        this.createLocalLog(ex, url, message, details, tipo);
    }

}
