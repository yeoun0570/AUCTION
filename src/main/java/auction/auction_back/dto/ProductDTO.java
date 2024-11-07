package auction.auction_back.dto;

import auction.auction_back.domain.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    private Long productId;

    private String sellerId;

    private String productName;

    private int price;

    private ProductStatus status;
}
