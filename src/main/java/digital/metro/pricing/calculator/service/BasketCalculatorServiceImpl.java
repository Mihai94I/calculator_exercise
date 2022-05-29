package digital.metro.pricing.calculator.service;

import digital.metro.pricing.calculator.dto.*;
import digital.metro.pricing.calculator.repository.PriceRepository;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import java.math.*;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BasketCalculatorServiceImpl implements BasketCalculatorService {

    private final PriceRepository priceRepository;

    @Value("${basket.default.price.scale}")
    private Integer defaultPriceScale;

    @Autowired
    public BasketCalculatorServiceImpl(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
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

    @Override
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
