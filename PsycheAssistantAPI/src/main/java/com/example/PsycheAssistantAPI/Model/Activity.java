package com.example.PsycheAssistantAPI.Model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
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
    @ToString.Exclude
    private Group group = null;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    @ToString.Exclude
    private User owner;

    @OneToOne
    @JsonIdentityReference(alwaysAsId = true)
    private User handledBy = null;

    private LocalDate deadline;
    private LocalDate handledOn = null;
    private boolean completed = false;

    public Activity(User owner, String description, int energyCost, LocalDate deadline) {
        this.owner = owner;
        this.description = description;
        this.energyCost = energyCost;
        this.deadline = deadline;

        if (owner.getGroup() != null) {
            this.group = owner.getGroup();
        }
    }
}