package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddProduct {
    @JsonProperty("product_id")
    private int id;
    private int quantity;
}
