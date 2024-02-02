package model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonPropertyOrder({"id", "fullName", "code", "sign"})
public class CurrenciesModel {
    private int id;
    private String code;
    @JsonSetter("name")
    private String fullName;
    private String sign;

    public CurrenciesModel(String code, String fullName, String sign) {
        this.code = code;
        this.fullName = fullName;
        this.sign = sign;
    }
}
