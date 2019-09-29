package net.kkhstudy.myfirstlambda.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemoRequest {
    private String name;
    private String description;
}
