package task.simpleexample.model;

import java.util.Map;

public class Currency {
    private Map<String,String> rates;

    public Currency(){

    }

    public Map<String, String> getRates() {
        return rates;
    }

    public void setRates(Map<String, String> rates) {
        this.rates = rates;
    }

    public String getCurrencyPrice(String key){
        return rates.get(key);
    }
}
