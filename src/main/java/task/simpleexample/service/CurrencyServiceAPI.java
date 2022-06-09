package task.simpleexample.service;

import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CurrencyServiceAPI {

    private final String baseServiceUrl = "https://openexchangerates.org/api/";
    private final String token;

    public CurrencyServiceAPI(String token){
        this.token = token;
    }

    public CurrencyService getCurrencyToDay(){
        String path = baseServiceUrl + "latest.json?app_id=" + token;
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(CurrencyService.class))
                .logLevel(Logger.Level.FULL)
                .target(CurrencyService.class, path);
    }

    public CurrencyService getCurrencyLastDay(){
        Calendar lastDayDate = new GregorianCalendar();
        lastDayDate.add(Calendar.DATE,-1);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String path = baseServiceUrl + "historical/" + df.format(lastDayDate.getTime()) + ".json?app_id=" + token;
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(CurrencyService.class))
                .logLevel(Logger.Level.FULL)
                .target(CurrencyService.class, path);
    }
}
