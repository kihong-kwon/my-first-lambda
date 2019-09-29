package net.kkhstudy.myfirstlambda.Domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class PayloadResponse {
    private String message1;
    private String message2;

    public PayloadResponse(String message1, String message2) {
        this.message1 = message1;
        this.message2 = message2;
    }
}
