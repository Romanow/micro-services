package ru.romanow.services.warehouse.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.romanow.services.warehouse.model.enums.SizeChart;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SizeChart size;

    @Column(name = "available_count", nullable = false)
    private int availableCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equal(model, item.model) &&
                size == item.size;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(model, size);
    }

    @Override
    public String toString() {
        return MoreObjects
                .toStringHelper(this)
                .add("model", model)
                .add("size", size)
                .toString();
    }
}
