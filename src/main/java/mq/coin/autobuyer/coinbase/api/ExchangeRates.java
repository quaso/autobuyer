package mq.coin.autobuyer.coinbase.api;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
public class ExchangeRates {
    private ExchangeRatesData data;

    @Data
    @ToString
    public static class ExchangeRatesData {
        private String currency;
        private Map<String, Double> rates;

    }}
