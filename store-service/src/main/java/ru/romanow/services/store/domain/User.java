package ru.romanow.services.store.domain;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Data
@Accessors(chain = true)
@Entity
@Table(name = "users",
        indexes = {
                @Index(name = "idx_user_name", columnList = "name", unique = true),
                @Index(name = "idx_user_uid", columnList = "uid", unique = true),
        })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, length = 40, unique = true)
    private UUID uid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equal(name, user.name) &&
                Objects.equal(uid, user.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, uid);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("uid", uid)
                .toString();
    }
}
