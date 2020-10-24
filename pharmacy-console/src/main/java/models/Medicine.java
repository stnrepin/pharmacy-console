package models;

import java.util.HashSet;
import java.util.Set;

public class Medicine {
    private int id;
    private String name;
    private int price;
    private int count;
    private Set<Disease> targetDiseases = new HashSet<>();

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

    public Set<Disease> getTargetDiseases() {
        return targetDiseases;
    }

    public void setTargetDiseases(Set<Disease> targetDiseases) {
        this.targetDiseases = targetDiseases;
    }
}
