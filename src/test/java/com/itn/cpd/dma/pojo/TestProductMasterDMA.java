package com.itn.cpd.dma.pojo;

import com.itn.cpd.dma.entities.ProductMasterDMA;
import com.itn.cpd.dma.repositories.ProductMasterDMARepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestProductMasterDMA {
    @Autowired
    ProductMasterDMARepository productMasterDMARepository;
    Faker faker = new Faker();
    @Test
    public void createDummyProducts(){
        for (int k=0; k<100; k++) {
            ProductMasterDMA product = createDummyProduct();
            productMasterDMARepository.save(product);
        }
    }

    private ProductMasterDMA createDummyProduct() {
        return ProductMasterDMA.builder()
                .productName(faker.harryPotter().character())
                .brand(faker.harryPotter().location())
                .brandId(faker.number().numberBetween(100, 999))
                .uomId(faker.idNumber().toString())
                .productNumber(faker.number().digits(8))
                .category(faker.book().genre())
                .gtin(faker.number().digits(14))
                .manufacturerNumber(faker.number().digits(16))
                .partnerId(faker.number().numberBetween(10, 200))
                .build();
    }
}
