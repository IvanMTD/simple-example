package task.simpleexample.service;

import feign.RequestLine;
import task.simpleexample.model.Currency;

public interface CurrencyService {
    @RequestLine("GET")
    Currency getCurrency();
}
