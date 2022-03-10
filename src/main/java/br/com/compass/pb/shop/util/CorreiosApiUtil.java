package br.com.compass.pb.shop.util;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CorreiosApiUtil {

    private static final String P_CODE_ORIGIN = "29302030";


    public static BigDecimal getShippingFee(String pCodeDestination) {
        try {
            String uri = "http://ws.correios.com.br/calculador/CalcPrecoPrazo.aspx?nCdEmpresa=&sDsSenha=" +
                    "&sCepOrigem=" + P_CODE_ORIGIN + "&sCepDestino=" + pCodeDestination +
                    "&nVlPeso=1&nCdFormato=1&nVlComprimento=20&nVlAltura=20&nVlLargura=20&sCdMaoPropria=n" +
                    "&nVlValorDeclarado=0&sCdAvisoRecebimento=n&nCdServico=04510&nVlDiametro=0" +
                    "&StrRetorno=xml&nIndicaCalculo=3";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(uri)).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(response.body())));
            doc.getDocumentElement().normalize();

            String fee = doc.getElementsByTagName("Valor").item(0).getTextContent()
                    .replace(",", ".");

            return new BigDecimal(fee);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
