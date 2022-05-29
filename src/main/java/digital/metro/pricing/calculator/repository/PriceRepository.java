package digital.metro.pricing.calculator.repository;

import java.math.BigDecimal;

public interface PriceRepository {

    BigDecimal getPriceByArticleIdAndCustomerId(String articleId, String customerId);

    BigDecimal getPriceByArticleId(String articleId);
}
