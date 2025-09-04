package com.best_store.right_bite.service.inventory;

import java.util.Map;

//todo: sync method
public interface InventoryService {
    void deductInventory(Map<Long, Integer> productIdsWithQuantity);

    void releaseInventory(Map<Long, Integer> productIdsWithQuantity);
}
