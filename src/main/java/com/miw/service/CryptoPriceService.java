package com.miw.service;

import com.google.gson.*;
import com.miw.database.JdbcCryptoDao;
import com.miw.database.JdbcTokenDao;
import com.miw.service.authentication.RegistrationService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CryptoPriceService {

    private final Logger logger = LoggerFactory.getLogger(RegistrationService.class);
    private final String apiKey = "89b44b8d-1f46-4e3c-9b3b-d4c9a84d80d6"; // dit is ook top secret! :)
    private final String uri = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";

    private JdbcCryptoDao jdbcCryptoDao;

    @Autowired
    public CryptoPriceService(JdbcCryptoDao jdbcCryptoDao) {
        this.jdbcCryptoDao = jdbcCryptoDao;
    }

    // kleine initial delay van 10 min, anders doet ie meteen een api-call elke keer dat een van ons een paar minuten lang de
    // applicatie aan moet zetten om iets te testen oid. de delay kan weggehaald worden in de versie die wordt gedeployed!
    @Scheduled(fixedRate = (60 * 60 * 1000), initialDelay = 10 * 60 * 1000)
    public void updatePrices() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("start","1"));
        params.add(new BasicNameValuePair("limit","20"));
        params.add(new BasicNameValuePair("convert","EUR"));

        try {
            parseAndSave(makeAPICall(uri, params));
            logger.info("CryptoPrices updated.");
        } catch (IOException e) {
            System.out.println("Error: cannot access content - " + e.toString());
        } catch (URISyntaxException e) {
            System.out.println("Error: Invalid URL " + e.toString());
        }
    }

    public void parseAndSave(String responseContent) throws IOException {
        JsonObject convertedObject = new Gson().fromJson(responseContent, JsonObject.class);
        JsonArray cryptos = convertedObject.getAsJsonArray("data");
        for (JsonElement crypto : cryptos) {
            jdbcCryptoDao.saveCryptoPriceBySymbol(crypto.getAsJsonObject().get("symbol").toString(),
                    crypto.getAsJsonObject().get("quote").getAsJsonObject().get("EUR").getAsJsonObject().get("price").getAsDouble());
            logger.info(crypto.getAsJsonObject().get("symbol").toString() + " logged");
        }
    }

    public String makeAPICall(String uri, List<NameValuePair> params) throws URISyntaxException, IOException {
        String responseContent = "";
        URIBuilder query = new URIBuilder(uri);
        query.addParameters(params);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(query.build());
        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.addHeader("X-CMC_PRO_API_KEY", apiKey);

        CloseableHttpResponse response = client.execute(request);
        try {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            responseContent = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
        return responseContent;
    }
}