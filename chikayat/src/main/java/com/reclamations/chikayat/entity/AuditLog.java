package com.reclamations.chikayat.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Data
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String tableName;
    private String operation;
    private Integer recordId;
    private Integer userId;
    private LocalDateTime timestamp;
    @Lob
    private String oldData;
    @Lob
    private String newData;
}
