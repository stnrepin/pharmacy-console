package models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Модель таблицы БД "Заболевание"
 */
@Entity
@Table(name = "disease")
public class Disease {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToMany(cascade =
            {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.REFRESH,
                    CascadeType.PERSIST
            },
            mappedBy = "targetDiseases")
    private List<Medicine> targetMedicines;

    public Disease() {
        this("");
    }

    public Disease(String name) {
        this.name = name;
        targetMedicines = new ArrayList<>();
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

    public List<Medicine> getTargetMedicines() {
        return targetMedicines;
    }

    public void setTargetMedicines(List<Medicine> targetMedicines) {
        this.targetMedicines = targetMedicines;
    }
}
