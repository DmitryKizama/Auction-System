package com.stkizema.auction.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        indexes = {
                @Index(value = "idProduct", unique = true)
        })
public class Product {

    @Id
    private Long idProduct;

    @NotNull
    @Property(nameInDb = "NameP")
    private String name;

    @NotNull
    @Property(nameInDb = "StartPriceP")
    private double startPrice;

    @Property(nameInDb = "ImageP")
    private String imageUrl;

    @NotNull
    @Property(nameInDb = "CreatorIdP")
    private Long creatorId;

    @Property(nameInDb = "WinnerId")
    private Long winnerId;

    @NotNull
    @Property(nameInDb = "IsAuctionedP")
    private boolean isAuctioned;

    @NotNull
    @Property(nameInDb = "IsWonP")
    private boolean isWon;

@Generated(hash = 258836703)
public Product(Long idProduct, @NotNull String name, double startPrice,
        String imageUrl, @NotNull Long creatorId, Long winnerId,
        boolean isAuctioned, boolean isWon) {
    this.idProduct = idProduct;
    this.name = name;
    this.startPrice = startPrice;
    this.imageUrl = imageUrl;
    this.creatorId = creatorId;
    this.winnerId = winnerId;
    this.isAuctioned = isAuctioned;
    this.isWon = isWon;
}

@Generated(hash = 1890278724)
public Product() {
}

public Long getIdProduct() {
    return this.idProduct;
}

public void setIdProduct(Long idProduct) {
    this.idProduct = idProduct;
}

public String getName() {
    return this.name;
}

public void setName(String name) {
    this.name = name;
}

public double getStartPrice() {
    return this.startPrice;
}

public void setStartPrice(double startPrice) {
    this.startPrice = startPrice;
}

public String getImageUrl() {
    return this.imageUrl;
}

public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
}

public Long getCreatorId() {
    return this.creatorId;
}

public void setCreatorId(Long creatorId) {
    this.creatorId = creatorId;
}

public boolean getIsAuctioned() {
    return this.isAuctioned;
}

public void setIsAuctioned(boolean isAuctioned) {
    this.isAuctioned = isAuctioned;
}

public boolean getIsWon() {
    return this.isWon;
}

public void setIsWon(boolean isWon) {
    this.isWon = isWon;
}

public Long getWinnerId() {
    return this.winnerId;
}

public void setWinnerId(Long winnerId) {
    this.winnerId = winnerId;
}

}
