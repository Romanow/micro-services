package ru.romanow.services.warranty.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.romanow.services.warranty.modal.enums.WarrantyStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "warranty",
       indexes = @Index(name = "idx_warranty_item_id", columnList = "item_id", unique = true)
)
public class Warranty {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "item_id", unique = true, length = 40)
    private UUID itemId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "warranty_date")
    private LocalDateTime warrantyDate;

    @Column
    @Enumerated(EnumType.STRING)
    private WarrantyStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Warranty warranty = (Warranty) o;
        return Objects.equal(itemId, warranty.itemId) &&
                status == warranty.status;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(itemId, status);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("itemId", itemId)
                .add("warrantyDate", warrantyDate)
                .add("status", status)
                .toString();
    }
}
