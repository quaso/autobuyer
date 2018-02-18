package mq.coin.autobuyer;

import coinbase.api.v2.CoinbaseApi;
import coinbase.api.v2.bean.CoinbaseAccount;
import coinbase.api.v2.bean.CoinbaseBuyResource;
import coinbase.api.v2.bean.CoinbasePaymentMethod;
import coinbase.api.v2.bean.CoinbaseUser;
import coinbase.api.v2.bean.operation.CoinbasePlaceBuyOrder;
import coinbase.api.v2.bean.response.ListCoinbaseAccount;
import coinbase.api.v2.bean.response.ListCoinbasePaymentMethod;
import coinbase.api.v2.exception.CoinbaseHttpException;
import coinbase.api.v2.service.CoinbaseAccountService;
import coinbase.api.v2.service.CoinbaseBuyService;
import coinbase.api.v2.service.CoinbasePaymentMethodService;
import coinbase.api.v2.service.CoinbaseUserService;
import coinbase.api.v2.service.auth.CoinbaseAuthenticationApiKey;
import coinbase.api.v2.service.auth.CoinbaseAuthenticationBearer;
import coinbase.api.v2.service.auth.JvmBasedTimestampProvider;
import coinbase.api.v2.utils.CoinbaseJsonObjectMapper;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class AutobuyerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutobuyerApplication.class, args);
	}

	@Autowired
	private PriceController priceController;

	@Value("${app.coin.api.key}")
	private String apiKey;

	@Value("${app.coin.api.secret}")
	private String apiSecret;

	private String aaa = "{\"pagination\":{\"ending_before\":null,\"starting_after\":null,\"limit\":25,\"order\":\"desc\",\"previous_uri\":null,\"next_uri\":null},\"data\":[{\"id\":\"85929ad8-ee01-54ff-95d6-d49b337056a3\",\"name\":\"BCH Wallet\",\"primary\":false,\"type\":\"wallet\",\"currency\":{\"code\":\"BCH\",\"name\":\"Bitcoin Cash\",\"color\":\"#8DC451\",\"exponent\":8,\"type\":\"crypto\",\"address_regex\":\"^([13][a-km-zA-HJ-NP-Z1-9]{25,34})|^((bitcoincash:)?(q|p)[a-z0-9]{41})|^((BITCOINCASH:)?(Q|P)[A-Z0-9]{41})$\"},\"balance\":{\"amount\":\"0.00000000\",\"currency\":\"BCH\"},\"created_at\":\"2017-12-15T21:30:43Z\",\"updated_at\":\"2017-12-17T22:30:07Z\",\"resource\":\"account\",\"resource_path\":\"/v2/accounts/85929ad8-ee01-54ff-95d6-d49b337056a3\"},{\"id\":\"36199720-961b-5559-b73c-f42aa536f47d\",\"name\":\"EUR Wallet\",\"primary\":false,\"type\":\"fiat\",\"currency\":{\"code\":\"EUR\",\"name\":\"Euro\",\"color\":\"#0066cf\",\"exponent\":2,\"type\":\"fiat\"},\"balance\":{\"amount\":\"0.00\",\"currency\":\"EUR\"},\"created_at\":\"2017-12-06T18:32:21Z\",\"updated_at\":\"2018-02-15T06:56:38Z\",\"resource\":\"account\",\"resource_path\":\"/v2/accounts/36199720-961b-5559-b73c-f42aa536f47d\"},{\"id\":\"40cf22d9-f719-50b9-a636-8ce97d8fee54\",\"name\":\"LTC Wallet\",\"primary\":false,\"type\":\"wallet\",\"currency\":{\"code\":\"LTC\",\"name\":\"Litecoin\",\"color\":\"#B5B5B5\",\"exponent\":8,\"type\":\"crypto\",\"address_regex\":\"^(L|M|3)[a-km-zA-HJ-NP-Z1-9]{25,34}$\"},\"balance\":{\"amount\":\"4.84357095\",\"currency\":\"LTC\"},\"created_at\":\"2017-12-06T09:41:12Z\",\"updated_at\":\"2018-02-15T06:56:38Z\",\"resource\":\"account\",\"resource_path\":\"/v2/accounts/40cf22d9-f719-50b9-a636-8ce97d8fee54\"},{\"id\":\"51f9466b-49c7-5475-9466-79dc8deda016\",\"name\":\"ETH Wallet\",\"primary\":false,\"type\":\"wallet\",\"currency\":{\"code\":\"ETH\",\"name\":\"Ethereum\",\"color\":\"#6F7CBA\",\"exponent\":8,\"type\":\"crypto\",\"address_regex\":\"^(?:0x)?[0-9a-fA-F]{40}$\"},\"balance\":{\"amount\":\"0.00000000\",\"currency\":\"ETH\"},\"created_at\":\"2017-12-06T09:41:12Z\",\"updated_at\":\"2017-12-06T09:41:12Z\",\"resource\":\"account\",\"resource_path\":\"/v2/accounts/51f9466b-49c7-5475-9466-79dc8deda016\"},{\"id\":\"2f3f3d7b-adf2-5359-a4a8-7de500ea0764\",\"name\":\"BTC Wallet\",\"primary\":true,\"type\":\"wallet\",\"currency\":{\"code\":\"BTC\",\"name\":\"Bitcoin\",\"color\":\"#FFB119\",\"exponent\":8,\"type\":\"crypto\",\"address_regex\":\"^[13][a-km-zA-HJ-NP-Z1-9]{25,34}$\"},\"balance\":{\"amount\":\"0.00000000\",\"currency\":\"BTC\"},\"created_at\":\"2017-12-06T09:41:12Z\",\"updated_at\":\"2018-02-02T11:47:52Z\",\"resource\":\"account\",\"resource_path\":\"/v2/accounts/2f3f3d7b-adf2-5359-a4a8-7de500ea0764\"}]}";

	@PostConstruct
	public void foo() throws CoinbaseHttpException, IOException {
		CoinbaseAuthenticationBearer auth = new CoinbaseAuthenticationApiKey(apiKey, apiSecret);
        JvmBasedTimestampProvider timeService = new JvmBasedTimestampProvider();

		CoinbaseApi coinbaseApi = new CoinbaseApi(timeService);

        CoinbaseUserService userService = coinbaseApi.getUserService();
		CoinbaseUser user = userService.showCurrent(auth);
		System.out.println(user);

		CoinbaseJsonObjectMapper objectMapper = new CoinbaseJsonObjectMapper();
		ListCoinbaseAccount listCoinbasePaymentMethod = objectMapper.fromString(aaa, ListCoinbaseAccount.class);

		CoinbasePaymentMethodService coinbasePaymentMethodService = coinbaseApi.getPaymentMethodService();
        ListCoinbasePaymentMethod paymentMethodList = coinbasePaymentMethodService.list(auth);

		CoinbasePaymentMethod fiatAccountPaymentMethod = paymentMethodList.getData().stream().filter(pm -> "fiat_account".equals(pm.getType())).findFirst().get();

		CoinbaseAccountService coinbaseAccountService = coinbaseApi.getAccountService();
        ListCoinbaseAccount accountList = coinbaseAccountService.list(auth);

		CoinbaseAccount eurAccount = accountList.getData().stream().filter(a -> "EUR".equals(a.getCurrency().getCode())).findFirst().get();


		CoinbasePlaceBuyOrder buyOrder = new CoinbasePlaceBuyOrder();
        buyOrder.setCommit(false);
        buyOrder.setAmount(new BigDecimal("100.0"));
        buyOrder.setQuote(false);
        buyOrder.setCurrency(fiatAccountPaymentMethod.getCurrency());
        buyOrder.setPaymentMethod(fiatAccountPaymentMethod.getId());

        CoinbaseBuyService buyService = coinbaseApi.getBuyService();
		CoinbaseBuyResource coinbaseBuyResource = buyService.placeBuyOrder(auth, eurAccount.getId(), buyOrder);
		System.out.println(coinbaseBuyResource);


/*
		System.out.println(priceController.userInfo());
		System.out.println(priceController.getCurrentExchangeRate());
		System.out.println(priceController.getCurrentPrice());
*/
		System.exit(0);
	}

	public static class Foo extends ArrayList<CoinbasePaymentMethod> {

		public Foo(){
			super();
		}

	}
}
