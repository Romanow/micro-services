package ru.romanow.services.order.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.romanow.services.order.model.enums.PaymentStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "orders",
       indexes = {
               @Index(name = "idx_orders_user_id", columnList = "user_id"),
               @Index(name = "idx_orders_order_id", columnList = "order_id", unique = true),
               @Index(name = "idx_orders_user_id_and_order_id", columnList = "user_id, order_id"),
       })
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false, length = 40)
    private UUID userId;

    @Column(name = "order_id", nullable = false, unique = true, length = 40)
    private UUID orderId;

    @Column(name = "item_id", nullable = false, length = 40)
    private UUID itemId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equal(orderId, order.orderId) &&
                Objects.equal(itemId, order.itemId) &&
                status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderId, itemId, status);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("userId", userId)
                          .add("orderId", orderId)
                          .add("itemId", itemId)
                          .add("orderDate", orderDate)
                          .add("status", status)
                          .toString();
    }
}
