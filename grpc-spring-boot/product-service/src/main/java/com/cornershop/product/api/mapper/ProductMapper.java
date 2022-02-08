package com.cornershop.product.api.mapper;

import com.cornershop.product.Resources;
import com.cornershop.product.application.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ProductMapper {

    public static final ProductMapper MAPPER =
            Mappers.getMapper(ProductMapper.class);

    public abstract Product map(Resources.CreateProductRequest createProductRequest);
}
