package models;

import java.util.Date;

public final class Update {
    private final Product product;
    private final String whatsNew;    
    private final Date postedDate;

    public Update(String whatsNew, Product product) {
        this(whatsNew, product, new Date());
    }

    public Update(String whatsNew, Product product, Date postedDate) {
        this.whatsNew = whatsNew;
        this.product = product;
        this.postedDate = postedDate;
    }

    public Product getProduct() {
        return product;
    }

    public String getWhatsNew() {
        return whatsNew;
    }

    public Date getPostedDate() {
        return postedDate;
    }

}
