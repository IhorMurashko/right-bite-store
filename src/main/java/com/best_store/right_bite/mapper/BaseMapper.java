package com.best_store.right_bite.mapper;

import java.util.List;
import java.util.Set;

public interface BaseMapper<E, D> {

    E toEntity(D d);

    List<E> toEntityList(List<D> ds);

    Set<E> toEntitySet(Set<D> ds);

    D toDTO(E e);

    List<D> toDTOList(List<E> e);

    Set<D> toDTOSet(Set<E> e);
}
