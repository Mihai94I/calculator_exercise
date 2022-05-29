package digital.metro.pricing.calculator;

import org.springframework.stereotype.Component;

import java.math.*;
import java.util.Map;

import static java.util.Map.entry;

/**
 * A dummy implementation for testing purposes. In production, we would get real prices from a database.
 */
@Component
public class PriceRepository {

    // article prices were calculated and declared as dummy data based on the expected result of calculateBasket.sh
    /*
    11.97 e 90% din suma totala a 4 articole de tip article-1 => 13.3 => pretul la article-1 este aproximativ 3.325
    10.28 e 90% din suma totala a 3 articole de tip article-2 => 10.28 + 1.14 = 11.42 => pretul la article-2 este aproximativ 3.806666666666667
     */
    private static final Map<String, BigDecimal> prices = Map.ofEntries(
            entry("article-1", new BigDecimal("3.3263")),
            entry("article-2", new BigDecimal("3.8085"))
    );

    private static final Map<String, BigDecimal> customerDiscounts = Map.ofEntries(
            entry("customer-1", new BigDecimal("0.90")),
            entry("customer-2", new BigDecimal("0.85"))
    );

    public BigDecimal getPriceByArticleIdAndCustomerId(String articleId, String customerId) {
        BigDecimal articlePrice = getPriceByArticleId(articleId);
        BigDecimal customerDiscount = customerDiscounts.get(customerId);

        if (articlePrice != null && customerDiscount != null) {
            return articlePrice.multiply(customerDiscount).setScale(articlePrice.scale(), RoundingMode.HALF_UP);
        }

        return null;
    }

    public BigDecimal getPriceByArticleId(String articleId) {
        return prices.get(articleId);
    }
}
