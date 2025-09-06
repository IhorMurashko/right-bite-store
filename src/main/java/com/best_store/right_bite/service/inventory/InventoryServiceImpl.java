package com.best_store.right_bite.service.inventory;

import com.best_store.right_bite.exception.ExceptionMessageProvider;
import com.best_store.right_bite.exception.inventory.InsufficientInventoryException;
import com.best_store.right_bite.model.catalog.Product;
import com.best_store.right_bite.repository.catalog.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class InventoryServiceImpl implements InventoryService {

    private final ProductRepository productRepository;

    @Override
    public void deductInventory(Map<Long, Integer> productIdsWithQuantity) {
        if (extracted(productIdsWithQuantity)) return;
        List<Product> productList = productRepository.findAllByIdIn(productIdsWithQuantity.keySet());
        productList.
                stream()
                .filter(Objects::nonNull)
                .forEach(product -> {
                    int balance = product.getQuantityInStock() - productIdsWithQuantity.get(product.getId());
                    log.debug("Deduct balance of product {} in stock is {}", product.getId(), balance);
                    if (balance < 0) {
                        log.error("Product with id {} has negative quantity", product.getId());
                        throw new InsufficientInventoryException(String.format(
                                ExceptionMessageProvider.NOT_ENOUGH_QUANTITY_IN_STOCK, product.getId()));
                    } else {
                        product.setQuantityInStock(balance);
                    }
                });
        productRepository.saveAll(productList);
        log.info("Successfully deducted inventory for {} products", productList.size());

    }

    @Override
    public void releaseInventory(Map<Long, Integer> productIdsWithQuantity) {
        if (extracted(productIdsWithQuantity)) return;
        List<Product> productList = productRepository.findAllByIdIn(productIdsWithQuantity.keySet());
        productList
                .stream()
                .filter(Objects::nonNull)
                .forEach(product -> {
                    int balance = product.getQuantityInStock() + productIdsWithQuantity.get(product.getId());
                    log.debug("Release balance of product {} in stock is {}", product.getId(), balance);
                    product.setQuantityInStock(balance);
                });
        productRepository.saveAll(productList);
    }

    private boolean extracted(Map<Long, Integer> productIdsWithQuantity) {
        if (productIdsWithQuantity.isEmpty()) {
            log.warn("Product ids with quantity is empty");
            return true;
        }
        return false;
    }
}
