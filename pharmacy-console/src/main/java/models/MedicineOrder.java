package models;

import javax.persistence.*;
import java.time.Instant;

/**
 * Модель таблицы базы данных "Заказ лекарства"
 */
@Table
@Entity(name = "medicine_order")
public class MedicineOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Medicine item;

    @Column(name = "count")
    private int count;

    /**
     * Общая стоимость всего заказа
     *
     * totalPrice = count * item.price
     */
    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "order_date")
    private Instant orderDate;

    public MedicineOrder() {

    }

    public MedicineOrder(Medicine item, int count) {
        this.item = item;
        this.count = count;
        this.totalPrice = item.getPrice() * count;
        this.orderDate = Instant.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Instant getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }
}
