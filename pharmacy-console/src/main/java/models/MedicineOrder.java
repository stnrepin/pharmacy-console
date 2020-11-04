package models;

import javax.persistence.*;

public class MedicineOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Medicine item;
    private int count;
    @Column(name = "total_price")
    private int totalPrice;

    public Medicine getItem() {
        return this.item;
    }

    public void setItem(Medicine item) {
        this.item = item;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
