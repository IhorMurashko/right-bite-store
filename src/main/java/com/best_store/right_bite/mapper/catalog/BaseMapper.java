package com.best_store.right_bite.mapper.catalog;

public interface BaseMapper<F, T> {
    T map(F obj);
}
