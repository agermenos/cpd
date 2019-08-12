package com.itn.cpd.dma.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
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
    @Column (name = "i_alt_id")
    private Long altIdInt;
    @Column(name = "product_name")
    private String productName;
    @JsonIgnore
    @Column(name = "break_level")
    private Long breakLevel;
    @Column(name="pack")
    private String pack;
    @Column (name ="unit_of_measure_id")
    private String uomId;
    @Column (name = "manufacturer_number")
    private String manufacturerNumber;
    @Column (name="upc")
    private String upc;
    @Column (name = "gtin")
    private String gtin;
    @Column (name = "category")
    private String category;
    @JsonIgnore
    @Column (name = "notes")
    private String notes;
    @Column (name = "partner_id")
    private Long partnerId;
    @Column (name = "product_number")
    private String productNumber;
    @Column (name = "brand_id")
    private Long brandId;
    @Column (name = "status_id")
    private Long statusId;
    @Column (name="created")
    private Date dateCreated;
    @Column (name = "modified")
    private Date dateModified;
    @JsonIgnore
    @Column (name = "logon_id")
    private Long logonId;
    @JsonIgnore
    @Column (name = "s_alt_id")
    private String altIdStr;

    //@PrePersist
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
