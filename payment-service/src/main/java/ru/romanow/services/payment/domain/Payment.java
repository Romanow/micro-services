package ru.romanow.services.payment.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.romanow.services.payment.model.enums.PaymentStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "payments",
       indexes = {
        @Index(name = "idx_payments_user_id", columnList = "user_id"),
        @Index(name = "idx_payments_order_id", columnList = "order_id", unique = true),
        @Index(name = "idx_payments_user_id_and_order_id", columnList = "user_id, order_id"),
       })
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", length = 40)
    private UUID userId;

    @Column(name = "order_id", unique = true, length = 40)
    private UUID orderId;

    @Column(name = "item_id", length = 40)
    private UUID itemId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equal(orderId, payment.orderId) &&
                Objects.equal(itemId, payment.itemId) &&
                status == payment.status;
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
