package com.stkizema.auction.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        indexes = {
                @Index(value = "idBid", unique = true)
        })
public class Bid {

    @Id
    private Long idBid;

    @NotNull
    @Property(nameInDb = "Price")
    private double price;

    @NotNull
    @Property(nameInDb = "ProductIdB")
    private Long productId;

    @NotNull
    @Property(nameInDb = "CreatorIdB")
    private Long creatorId;

@Generated(hash = 2031116917)
public Bid(Long idBid, double price, @NotNull Long productId,
        @NotNull Long creatorId) {
    this.idBid = idBid;
    this.price = price;
    this.productId = productId;
    this.creatorId = creatorId;
}

@Generated(hash = 962914990)
public Bid() {
}

public Long getIdBid() {
    return this.idBid;
}

public void setIdBid(Long idBid) {
    this.idBid = idBid;
}

public double getPrice() {
    return this.price;
}

public void setPrice(double price) {
    this.price = price;
}

public Long getProductId() {
    return this.productId;
}

public void setProductId(Long productId) {
    this.productId = productId;
}

public Long getCreatorId() {
    return this.creatorId;
}

public void setCreatorId(Long creatorId) {
    this.creatorId = creatorId;
}

}
