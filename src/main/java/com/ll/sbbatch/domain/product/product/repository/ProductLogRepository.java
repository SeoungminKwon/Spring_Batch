package com.ll.sbbatch.domain.product.product.repository;

import com.ll.sbbatch.domain.product.product.entity.ProductLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductLogRepository extends JpaRepository< ProductLog, Long > {
}
