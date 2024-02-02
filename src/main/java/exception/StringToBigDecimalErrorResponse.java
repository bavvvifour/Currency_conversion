package exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StringToBigDecimalErrorResponse {
    private int code;
    private String message;
}
