package com.itn.cpd.dma.pojo;

import com.itn.cpd.dma.entities.ProductMasterDMA;
import com.itn.cpd.dma.repositories.ProductMasterDMARepository;
import com.itn.cpd.dma.services.SearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestProductMasterDMA {
    @Autowired
    ProductMasterDMARepository productMasterDMARepository;
    @Autowired
    SearchService searchService;
    Faker faker = new Faker();
    @Test
    public void createDummyProducts(){
        for (int k=0; k<100; k++) {
            ProductMasterDMA product = createDummyProduct();
            productMasterDMARepository.save(product);
        }
    }

    @Test
    public void createDummyFromFile(){
        String fileName = "src/test/resources/ItemMaster-DMA-50000-Items.csv";
        //String fileName = "src/test/resources/ItemMaster-non-DMA-Items.csv";
        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(fileName)).skip(1)) {
            stream.forEach(line -> {
                ProductMasterDMA product = createProduct(line);
                //System.out.println(product.toString());
                productMasterDMARepository.save(product);
                try {
                    searchService.addIndex(product);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ProductMasterDMA createProduct(String line) {
        String[] properties = line.split(",");
        return ProductMasterDMA.builder()
                .altIdInt(lValue(properties[0]))
                .partnerId(lValue(properties[1]))
                .productNumber(strValue(properties[2]))
                .breakLevel(lValue(properties[3]))
                .productName(strValue(properties[4]))
                .uomId(strValue(properties[5]))
                .pack((strValue(properties[6])))
                .upc((strValue(properties[7])))
                .gtin((strValue(properties[8])))
                .manufacturerNumber((strValue(properties[9])))
                .brandId(lValue(properties[10]))
                .category((strValue(properties[11])))
                .statusId(lValue(properties[12]))
                .dateCreated(getDate(properties[13]))
                .dateModified(getDate(properties[14]))
                .logonId(lValue(properties[15]))
                .build();

    }
    
    private String strValue (String text){
        if (text!=null && !text.equals("NULL")) return text;
        else return null;
    }
    
    private Long lValue (String text) {
        try {
            if (text != null && !text.equals("NULL")) return Long.valueOf(text);
        }
        catch (NumberFormatException nfe) {
            System.out.println("ERROR WITH: " + text);
        }
        return null;
    }

    private Date getDate(String property) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(property);
        }
        catch (ParseException pe) {
            return null;
        }
    }

    private ProductMasterDMA createDummyProduct() {
        return ProductMasterDMA.builder()
                .productName(faker.harryPotter().character())
                .brandId(Long.valueOf(faker.number().numberBetween(100, 999)))
                .uomId(faker.idNumber().toString())
                .productNumber(faker.number().digits(8))
                .category(faker.book().genre())
                .gtin(faker.number().digits(14))
                .manufacturerNumber(faker.number().digits(16))
                .partnerId(Long.valueOf(faker.number().numberBetween(10, 200)))
                .build();
    }
}
