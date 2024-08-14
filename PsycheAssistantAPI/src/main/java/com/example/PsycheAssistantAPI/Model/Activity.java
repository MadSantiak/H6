package com.example.PsycheAssistantAPI.Model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "activities")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    private int energyCost = 0;

    /**
     * To avoid recursion when calling from front-end, we only use the ID as reference when de/serializing the models as JSON objects.
     */
    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonIdentityReference(alwaysAsId = true)
    private Group group = null;

    @OneToOne
    @JsonIdentityReference(alwaysAsId = true)
    private User owner;

    @OneToOne
    @JsonIdentityReference(alwaysAsId = true)
    private User handler;

    private LocalDateTime deadline;
    private LocalDateTime handledOn = null;
}