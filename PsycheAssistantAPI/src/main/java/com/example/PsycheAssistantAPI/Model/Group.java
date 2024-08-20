package com.example.PsycheAssistantAPI.Model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "groups")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * To avoid recursion when calling from front-end, we only use the ID as reference when de/serializing the models as JSON objects.
     */
    @OneToMany(mappedBy = "group")
    @JsonIdentityReference(alwaysAsId = true)
    @ToString.Exclude
    private List<User> users = new ArrayList<>();

    /**
     * Note that we aren't interested in activities no longer tied to a group to be present in the database
     * hence the Cascade and orphanRemoval.
     */
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIdentityReference(alwaysAsId = true)
    @ToString.Exclude
    private List<Activity> activities = new ArrayList<>();

    @OneToOne
    @JsonIdentityReference(alwaysAsId = true)
    @ToString.Exclude
    private User owner;

    private String code;

    public void addActivity(Activity activity) {
        activities.add(activity);
        activity.setGroup(this);
    }

    public void removeActivity(Activity activity) {
        activities.remove(activity);
        activity.setGroup(null);
    }


    public void addUser(User user) {
        users.add(user);
        user.setGroup(this);
    }

    public void removeUser(User user) {
        users.remove(user);
        user.setGroup(null);
    }
}
