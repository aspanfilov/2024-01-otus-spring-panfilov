package ru.otus.hw.security;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.Set;

@Entity
@Table(name = "authority_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityGroup {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST},
            fetch = FetchType.EAGER)
    @JoinTable(name = "authority_groups_authorities",
            joinColumns = @JoinColumn(name = "authority_group_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id"))
    @Fetch(FetchMode.SUBSELECT)
    private Set<Authority> authorities;
}
