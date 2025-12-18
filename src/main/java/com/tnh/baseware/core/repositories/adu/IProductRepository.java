package com.tnh.baseware.core.repositories.adu;

import com.tnh.baseware.core.entities.adu.Product;
import com.tnh.baseware.core.repositories.IGenericRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IProductRepository extends IGenericRepository<Product, UUID> {
}
