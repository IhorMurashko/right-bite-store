package com.best_store.right_bite.dto.orders;


import lombok.Builder;

/*Test dto*/
@Builder
public record OrderDTO(Long id, String numberOrder, Long id_customer) {
}
