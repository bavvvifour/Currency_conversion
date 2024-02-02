package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonPropertyOrder({"id", "BaseCurrencyId", "TargetCurrencyId", "Rate"})
public class ExchangeRatesModel {
    private int id;
    private CurrenciesModel BaseCurrencyId;
    private CurrenciesModel TargetCurrencyId;
    private BigDecimal Rate;

    @JsonIgnore
    private BigDecimal amount;
    @JsonIgnore
    private BigDecimal convertedAmount;

    public ExchangeRatesModel(CurrenciesModel baseCurrencyId, CurrenciesModel targetCurrencyId, BigDecimal rate) {
        BaseCurrencyId = baseCurrencyId;
        TargetCurrencyId = targetCurrencyId;
        Rate = rate;
    }

    public ExchangeRatesModel(int id, CurrenciesModel baseCurrencyId, CurrenciesModel targetCurrencyId, BigDecimal rate) {
        this.id = id;
        BaseCurrencyId = baseCurrencyId;
        TargetCurrencyId = targetCurrencyId;
        Rate = rate;
    }
}
