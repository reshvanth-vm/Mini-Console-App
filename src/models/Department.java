package models;

import models.Department;

public final class Department {
    private final int id;
    private int managerId;
    private String name;
    private Product product;

    public Department(int id, String name, Product product, int managerId) {
        this.id = id;
        this.name = name;
        this.product = product;
        this.managerId = managerId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getManagerId() {
        return managerId;
    }

    public Product getProduct() {
        return product;
    }

}
