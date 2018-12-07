package ru.romanow.services.warehouse.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.romanow.services.warehouse.model.enums.SizeChart;

import javax.persistence.*;
import java.util.UUID;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "items",
       indexes = @Index(name = "idx_items_item_id", columnList = "item_id", unique = true)
)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "item_id", unique = true, length = 40)
    private UUID itemId;

    @Column
    private String model;

    @Column
    @Enumerated(EnumType.STRING)
    private SizeChart size;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equal(itemId, item.itemId) &&
                Objects.equal(model, item.model);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(itemId, model);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("itemId", itemId)
                .add("model", model)
                .add("size", size)
                .toString();
    }
}
