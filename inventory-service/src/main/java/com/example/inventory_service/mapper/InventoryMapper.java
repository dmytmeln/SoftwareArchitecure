package com.example.inventory_service.mapper;

import com.example.inventory_service.domain.Product;
import com.example.inventory_service.dto.InventoryDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    InventoryDto toDto(Product product);

    List<InventoryDto> toDto(List<Product> products);

}
