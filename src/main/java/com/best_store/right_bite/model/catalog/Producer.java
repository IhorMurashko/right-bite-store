package com.best_store.right_bite.model.catalog;

import jakarta.persistence.Entity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producer extends Base {

    private String producerName;
}
