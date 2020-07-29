package io.nuite.modules.system.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class GoodsShoesPlatformForm {
    private BigDecimal oemPrice;
    private BigDecimal wholesalerPrice;
    private BigDecimal storePrice;
    private BigDecimal salePrice;
    private Boolean onSale;
    private Date onSaleTime;
    private Date offSaleTime;
    private List<Integer> author;

    private List<GoodsShoesStockForm> stock;

    public BigDecimal getOemPrice() {
        return oemPrice;
    }

    public void setOemPrice(BigDecimal oemPrice) {
        this.oemPrice = oemPrice;
    }

    public BigDecimal getWholesalerPrice() {
        return wholesalerPrice;
    }

    public void setWholesalerPrice(BigDecimal wholesalerPrice) {
        this.wholesalerPrice = wholesalerPrice;
    }

    public BigDecimal getStorePrice() {
        return storePrice;
    }

    public void setStorePrice(BigDecimal storePrice) {
        this.storePrice = storePrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public List<Integer> getAuthor() {
        return author;
    }

    public void setAuthor(List<Integer> author) {
        this.author = author;
    }

    public List<GoodsShoesStockForm> getStock() {
        return stock;
    }

    public void setStock(List<GoodsShoesStockForm> stock) {
        this.stock = stock;
    }

    public Boolean getOnSale() {
        return onSale;
    }

    public void setOnSale(Boolean onSale) {
        this.onSale = onSale;
    }

	public Date getOnSaleTime() {
		return onSaleTime;
	}

	public void setOnSaleTime(Date onSaleTime) {
		this.onSaleTime = onSaleTime;
	}

	public Date getOffSaleTime() {
		return offSaleTime;
	}

	public void setOffSaleTime(Date offSaleTime) {
		this.offSaleTime = offSaleTime;
	}
	
}
