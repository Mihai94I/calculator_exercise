package digital.metro.pricing.calculator;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Component;

import java.math.*;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BasketCalculatorService {

    private final PriceRepository priceRepository;

    @Value("${basket.default.price.scale}")
    private Integer defaultPriceScale;

    @Autowired
    public BasketCalculatorService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public BasketCalculationResult calculateBasket(Basket basket) {
        Map<String, BigDecimal> pricedArticles = basket.getEntries().stream()
                .collect(Collectors.toMap(
                        BasketEntry::getArticleId,
                        entry -> calculateArticle(entry, basket.getCustomerId()))
                );

        BigDecimal totalAmount = pricedArticles.values().stream()
                .reduce(BigDecimal.ONE, BigDecimal::add)
                .setScale(defaultPriceScale, RoundingMode.HALF_UP);

        pricedArticles.replaceAll((key, value) -> value.setScale(defaultPriceScale, RoundingMode.HALF_UP));

        return new BasketCalculationResult(basket.getCustomerId(), pricedArticles, totalAmount);
    }

    public BigDecimal calculateArticle(BasketEntry basketEntry, String customerId) {
        String articleId = basketEntry.getArticleId();
        BigDecimal articleQuantity = basketEntry.getQuantity();

        if (customerId != null) {
            BigDecimal customerPrice = priceRepository.getPriceByArticleIdAndCustomerId(articleId, customerId);
            if (customerPrice != null) {
                return customerPrice.multiply(articleQuantity);
            }
        }
        return priceRepository.getPriceByArticleId(articleId).multiply(articleQuantity);
    }
}
