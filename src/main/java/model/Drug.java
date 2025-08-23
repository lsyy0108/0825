package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Drug {
    private int id;
    private String name;
    private String category;
    private String unit;
    private int stock;
    private BigDecimal price;
    private LocalDate expirationDate;
    private String drugCode;

    public Drug() {}

    public Drug(int id, String name, String category, String unit, int stock, BigDecimal price, LocalDate expirationDate, String drugCode) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.stock = stock;
        this.price = price;
        this.expirationDate = expirationDate;
        this.drugCode = drugCode;
        
        
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public LocalDate getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDate expirationDate) { this.expirationDate = expirationDate; }

    public String getDrugCode() { return drugCode; }
    public void setDrugCode(String drugCode) { this.drugCode = drugCode; }
    
    public String toString() {
        return name + " (" + drugCode + ")";}
    
}

