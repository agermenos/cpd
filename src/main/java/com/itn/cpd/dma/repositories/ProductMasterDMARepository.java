package com.itn.cpd.dma.repositories;

import com.itn.cpd.dma.entities.ProductMasterDMA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductMasterDMARepository extends JpaRepository<ProductMasterDMA, String> {
}
