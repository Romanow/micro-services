package ru.romanow.services.warehouse.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "order_item", indexes = {
        @Index(name = "idx_order_item_item_id", columnList = "item_id"),
        @Index(name = "idx_order_item_uid", columnList = "uid", unique = true)
})
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_id", nullable = false, length = 40)
    private UUID orderId;

    @Column(nullable = false, length = 40, unique = true)
    private UUID uid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", foreignKey = @ForeignKey(name = "fk_order_item_item_id"))
    private Item item;

    @Column
    private boolean canceled;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equal(orderId, orderItem.orderId) &&
                Objects.equal(uid, orderItem.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderId, uid);
    }

    @Override
    public String toString() {
        return MoreObjects
                .toStringHelper(this)
                .add("orderId", orderId)
                .add("uid", uid)
                .add("canceled", canceled)
                .toString();
    }
}
