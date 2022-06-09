package task.simpleexample.controller;

import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import task.simpleexample.service.CurrencyService;
import task.simpleexample.service.GifService;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Controller
public class MainController {

    private final String TOKEN_CURRENCY = System.getenv("TOKEN_CURRENCY");
    private final String TOKEN_GIF = System.getenv("TOKEN_GIF");

    private final CurrencyService currencyToDay;
    private final CurrencyService currencyLastDay;

    private final GifService rich;
    private final GifService broke;

    private String url;
    private String info;

    @Autowired
    public MainController(){

        url = "";
        info = "";

        Calendar lastDayDate = new GregorianCalendar();
        lastDayDate.add(Calendar.DATE,-1);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String pathToDay = "https://openexchangerates.org/api/latest.json?app_id=" + TOKEN_CURRENCY;
        String pathLastDay = "https://openexchangerates.org/api/historical/" + df.format(lastDayDate.getTime()) + ".json?app_id=" + TOKEN_CURRENCY;
        currencyToDay = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(CurrencyService.class))
                .logLevel(Logger.Level.FULL)
                .target(CurrencyService.class, pathToDay);
        currencyLastDay = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(CurrencyService.class))
                .logLevel(Logger.Level.FULL)
                .target(CurrencyService.class, pathLastDay);

        String richPath = "https://api.giphy.com/v1/gifs/search?api_key=" + TOKEN_GIF + "&q=rich&limit=25&offset=0&rating=g&lang=en";
        String brokePath = "https://api.giphy.com/v1/gifs/search?api_key=" + TOKEN_GIF + "&q=broke&limit=25&offset=0&rating=g&lang=en";

        rich = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(GifService.class))
                .logLevel(Logger.Level.FULL)
                .target(GifService.class, richPath);
        broke = Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(GifService.class))
                .logLevel(Logger.Level.FULL)
                .target(GifService.class, brokePath);
    }

    @GetMapping("/")
    public String getHome(Model model){
        model.addAttribute("url",url);
        model.addAttribute("info",info);
        return "home";
    }

    @GetMapping("/check")
    public String check(Model model, @RequestParam String symbol){
        if(currencyLastDay.getCurrency().getCurrencyPrice(symbol) != null){
            BigDecimal current = new BigDecimal(currencyToDay.getCurrency().getCurrencyPrice(symbol));
            BigDecimal last = new BigDecimal(currencyLastDay.getCurrency().getCurrencyPrice(symbol));
            if(current.compareTo(last) <= 0){
                url = rich.get().getRandomData().getImages().getOriginal().getUrl();
                info = "Курс " + symbol + " подрос!";
            }else{
                url = broke.get().getRandomData().getImages().getOriginal().getUrl();
                info = "Вчера курс " + symbol + " был лучше ...";
            }
        }else{
            url = "";
            info = "Не верный буквенный код валюты!";
        }
        return "redirect:/";
    }
}
