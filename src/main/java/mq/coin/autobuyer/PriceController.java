package mq.coin.autobuyer;

import mq.coin.autobuyer.coinbase.api.ExchangeRates;
import mq.coin.autobuyer.coinbase.api.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PriceController {

    @Autowired
    private RestTemplate restTemplate;

    public double getCurrentExchangeRate(){
/*
        ExchangeRates exchangeRates = new ExchangeRates();
        ExchangeRates.ExchangeRatesData data = new ExchangeRates.ExchangeRatesData();
        data.setCurrency("aaa");
        exchangeRates.setData(data);
        HttpEntity<ExchangeRates> request = new HttpEntity<>(exchangeRates);
        restTemplate.postForEntity("https://api.coinbase.com/v2/exchange-rates?currency=LTC", request, ExchangeRates.class);
*/
        return restTemplate.getForObject("https://api.coinbase.com/v2/exchange-rates?currency=LTC", ExchangeRates.class).getData().getRates().get("EUR");
    }

    public double getCurrentPrice(){
        return restTemplate.getForObject("https://api.coinbase.com/v2/prices/LTC-EUR/buy", Price.class).getData().getAmount();
    }

    public ResponseEntity<String> userInfo(){
        return restTemplate.getForEntity("https://api.coinbase.com/v2/user", String.class);
    }
}