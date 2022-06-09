package task.simpleexample.service;

import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;

public class GifServiceAPI {
    private final String token;

    public GifServiceAPI(String token){
        this.token = token;
    }

    public GifService prepareGif(int limit, String name){
        String path = "https://api.giphy.com/v1/gifs/search?api_key=" + token + "&q=" + name + "&limit=" + limit + "&offset=0&rating=g&lang=en";

        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .logger(new Slf4jLogger(GifService.class))
                .logLevel(Logger.Level.FULL)
                .target(GifService.class, path);
    }
}
