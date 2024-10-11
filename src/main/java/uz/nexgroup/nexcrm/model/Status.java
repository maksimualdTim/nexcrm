package uz.nexgroup.nexcrm.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String name;

    private int order;

    @ManyToOne
    @JoinColumn(name = "pipeline_id", nullable = false) // pipeline_id как внешний ключ
    private Pipeline pipeline;
}
