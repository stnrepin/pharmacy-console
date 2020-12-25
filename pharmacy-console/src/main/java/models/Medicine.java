package models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Модель таблицы БД "Лекарство"
 */
@Entity
@Table(name = "medicine")
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @Column(name = "count")
    private int count;

    @ManyToMany(cascade =
            {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            })
    @JoinTable(name = "medicine_disease",
            joinColumns = @JoinColumn(name = "med_id"),
            inverseJoinColumns = @JoinColumn(name = "dis_id")
    )
    private List<Disease> targetDiseases;

    public Medicine() {
        this("", 0, 0);
    }

    public Medicine(String name, int price, int count) {
        this(name, price, count, new ArrayList<>());
    }

    public Medicine(String name, int price, int count, List<Disease> targetDiseases) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.targetDiseases = targetDiseases;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void addTargetDisease(Disease target) {
        this.targetDiseases.add(target);
    }

    public List<Disease> getTargetDiseases() {
        return targetDiseases;
    }

    public void setTargetDiseases(List<Disease> targetDiseases) {
        this.targetDiseases = targetDiseases;
    }
}
