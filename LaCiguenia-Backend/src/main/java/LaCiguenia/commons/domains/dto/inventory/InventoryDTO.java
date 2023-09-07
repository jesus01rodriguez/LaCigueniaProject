package LaCiguenia.commons.domains.dto.inventory;

import LaCiguenia.commons.domains.entity.product.ProductEntity;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class InventoryDTO implements Serializable {
    private Integer inventoryId;
    private Integer inventoryAmount;
    private Date inventoryEntryDate;
    @JsonIdentityReference(alwaysAsId = true)
    private ProductEntity productEntity;
}
