package digital.metro.pricing.calculator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class CalculatorController {

    private final BasketCalculatorService basketCalculatorService;

    @Autowired
    public CalculatorController(BasketCalculatorService basketCalculatorService) {
        this.basketCalculatorService = basketCalculatorService;
    }

    @PostMapping("/calculator/calculate-basket")
    public BasketCalculationResult calculateBasket(@RequestBody Basket basket) {
        return basketCalculatorService.calculateBasket(basket);
    }

    @GetMapping("/calculator/article/{articleId}")
    public BigDecimal getArticlePrice(@PathVariable String articleId) {
        return basketCalculatorService.calculateArticle(new BasketEntry(articleId, BigDecimal.ONE), null);
    }

    @GetMapping("/calculator/getArticlePriceForCustomer")
    public BigDecimal getArticlePriceForCustomer(@RequestParam String articleId, @RequestParam String customerId) {
        return basketCalculatorService.calculateArticle(new BasketEntry(articleId, BigDecimal.ONE), customerId);
    }
}
