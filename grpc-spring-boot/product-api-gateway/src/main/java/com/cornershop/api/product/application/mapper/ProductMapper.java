package com.cornershop.api.product.application.mapper;

import com.cornershop.api.product.application.model.Product;
import com.cornershop.product.Resources;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public abstract class ProductMapper {

    public static final ProductMapper MAPPER =
            Mappers.getMapper(ProductMapper.class);

    public abstract Product map(Resources.GetProductResponse getProductResponse);
}
