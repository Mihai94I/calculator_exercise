package digital.metro.pricing.calculator.service;

import digital.metro.pricing.calculator.dto.*;

import java.math.BigDecimal;

public interface BasketCalculatorService {

    BasketCalculationResult calculateBasket(Basket basket);

    BigDecimal calculateArticle(BasketEntry basketEntry, String customerId);
}
