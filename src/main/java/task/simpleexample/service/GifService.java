package task.simpleexample.service;

import feign.RequestLine;
import task.simpleexample.model.GifResource;

public interface GifService {
    @RequestLine("GET")
    GifResource get();
}
