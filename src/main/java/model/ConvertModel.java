package model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonPropertyOrder({"BaseCurrencyId", "TargetCurrencyId", "Rate", "amount", "convertedAmount"})
public class ConvertModel {
    private CurrenciesModel BaseCurrencyId;
    private CurrenciesModel TargetCurrencyId;
    private BigDecimal Rate;

    private BigDecimal amount;
    private BigDecimal convertedAmount;
}
