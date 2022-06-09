package task.simpleexample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import task.simpleexample.service.CurrencyService;
import task.simpleexample.service.CurrencyServiceAPI;
import task.simpleexample.service.GifServiceAPI;

import java.math.BigDecimal;

@Controller
public class MainController {
    private final CurrencyServiceAPI currencyService;

    private final GifServiceAPI gifService;

    private String url;
    private String info;

    @Autowired
    public MainController(){
        url = "";
        info = "";
        currencyService = new CurrencyServiceAPI("54054d0cf7bd4a11ae7fc00aa562072a");
        gifService = new GifServiceAPI("837CDpMy4Bn8ETlTkKNw8gj4CrPrMEYl");
    }

    @GetMapping("/")
    public String getHome(Model model){
        model.addAttribute("url",url);
        model.addAttribute("info",info);
        return "home";
    }

    @GetMapping("/check")
    public String check(@RequestParam String symbol){
        int limit = 25;
        CurrencyService toDay = currencyService.getCurrencyToDay();
        CurrencyService lastDay = currencyService.getCurrencyLastDay();
        if(toDay.getCurrency().getCurrencyPrice(symbol) != null){
            BigDecimal current = new BigDecimal(toDay.getCurrency().getCurrencyPrice(symbol));
            BigDecimal last = new BigDecimal(lastDay.getCurrency().getCurrencyPrice(symbol));
            if(current.compareTo(last) <= 0){
                url = gifService.prepareGif(limit,"rich").get().getRandomData().getImages().getOriginal().getUrl();
                info = "Курс " + symbol + " подрос!";
            }else{
                url = gifService.prepareGif(limit,"broke").get().getRandomData().getImages().getOriginal().getUrl();
                info = "Вчера курс " + symbol + " был лучше ...";
            }
        }else{
            url = "";
            info = "Не верный буквенный код валюты!";
        }
        return "redirect:/";
    }
}
