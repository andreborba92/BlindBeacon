package borba.com.br.blindbeacon.models;

/**
 * Created by andre on 16/03/2017.
 */

/**
 * Representa uma model para log de erros do sistema
 * Created by diego.nunes on 18/11/2015.
 */
public class LogApp {

    public static final String WARNING = "WARNING";
    public static final String ERROR = "ERROR";
    public static final String INFORMATION = "INFORMATION";

    private String Data;
    private String Tipo;
    private String Mensagem;
    private String Excecao;
    private String Detalhes;
    private String UrlRequisicao;
    private String Origem = "ANDROID";
    private String PlataformaOperacional = "";

    private String VersaoApp;

    public LogApp(){}

    public String getOrigem() {
        return Origem;
    }

    public void setOrigem(String origem) {
        Origem = origem;
    }

    public String getPlataformaOperacional() {
        return PlataformaOperacional;
    }

    public void setPlataformaOperacional(String plataforma) {
        PlataformaOperacional = plataforma;
    }

    public String getVersaoApp() {
        return VersaoApp;
    }

    public void setVersaoApp(String versaoApp) {
        VersaoApp = versaoApp;
    }

    public String getUrlRequisicao() {
        return UrlRequisicao;
    }

    public void setUrlRequisicao(String urlRequisicao) {
        UrlRequisicao = urlRequisicao;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getExcecao() {
        return Excecao;
    }

    public void setExcecao(String excecao) {
        Excecao = excecao;
    }

    public String getDetalhes() {
        return Detalhes;
    }

    public void setDetalhes(String detalhes) {
        Detalhes = detalhes;
    }

    public String getMensagem() {
        return Mensagem;
    }

    public void setMensagem(String mensagem) {
        Mensagem = mensagem;
    }

    @Override
    public String toString() {
        return "Mensagem: " + this.getMensagem() + ", Detalhes: " + this.getDetalhes() + ", Data: " + getData();
    }


}
