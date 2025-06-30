package com.praktica.HelpDesk.mapper.base;

import java.util.List;

public interface Mappable <E,D>{
    E toEntity(D d);
    D toDto(E e);

    List<E> toEntities(Iterable<D> dtos);
    List<D> toDtos(Iterable<E> ents);
}
