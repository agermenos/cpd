package com.itn.cpd.dma.entities;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity (name = "product_master_dma")
@Data
@Builder
public class ProductMasterDMA {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(name = "product_name")
    private String productName;
    @Column(name="pack")
    private String pack;
    @Column(name= "size")
    private Integer size;
    @Column (name ="unit_of_measure_id")
    private String uomId;
    @Column (name = "brand")
    private String brand;
    @Column (name = "brand_owner")
    private String brandOwner;
    @Column (name = "manufacturer_number")
    private String manufacturerNumber;
    @Column (name="upc")
    private String upc;
    @Column (name = "gtin")
    private String gtin;
    @Column (name = "category")
    private String category;
    @Column (name = "notes")
    private String notes;
    @Column (name = "partner_id")
    private Integer partnerId;
    @Column (name = "product_number")
    private String productNumber;
    @Column (name = "brand_id")
    private Integer brandId;
    @Column (name = "status_id")
    private Integer statusId;
    @Column (name="created")
    private Date dateCreated;
    @Column (name = "modified")
    private Date dateModified;
    @Column (name = "logon_id")
    private Integer logonId;
    @Column (name = "i_alt_id")
    private Integer altIdInt;
    @Column (name = "s_alt_id")
    private String altIdStr;

    @PrePersist
    public void prePersist(){
        if (id==null) {
            dateCreated = new Date();
            dateModified = dateCreated;
        }
        else {
            dateModified = new Date();
        }
    }
}
