package uz.nexgroup.nexcrm.model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;


@Entity
@Data
@Table(name = "role", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "account_id"})})
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "json")
    private String permissionsJson;

    @Transient
    private Permission permissions;

    @Column(name = "is_admin")
    private boolean isAdmin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users;

    @PostLoad
    private void loadPermissions() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.permissions = objectMapper.readValue(this.permissionsJson, new TypeReference<Permission>() {});
    }

    @PrePersist
    @PreUpdate
    private void savePermissions() throws IOException, SQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.permissionsJson = objectMapper.writeValueAsString(this.permissions);
    }
}
