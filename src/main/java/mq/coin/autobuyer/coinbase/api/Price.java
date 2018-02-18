package mq.coin.autobuyer.coinbase.api;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
public class Price {

    private PriceData data;

    @Data
    @ToString
    public class PriceData {
        private String base;
        private String currency;
        private double amount;

    }
}
