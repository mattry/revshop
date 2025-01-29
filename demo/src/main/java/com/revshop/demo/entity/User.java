package com.revshop.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchConnectionDetails;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "role"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Buyer.class, name = "BUYER"),
    @JsonSubTypes.Type(value = Seller.class, name = "SELLER")
})
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String username;

    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Role role = Role.BUYER;

    public enum Role {
        BUYER,
        SELLER
    }

    public User(){}

    public User(Long userId, String email, String firstName, String lastName, String password, Role role, String username) {
        this.id = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.role = role;
        this.username = username;
    }

}
