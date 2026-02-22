package com.ffb.model.db.objects.image;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ffb.model.db.objects.food_court.FoodCourt;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "image", schema = "ffb")
public class Image extends PanacheEntityBase {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "image_uri", unique = true)
    private URI imageURI;

    @JsonIgnore
    @OneToMany(mappedBy = "image", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FoodCourt> foodCourt;

    protected Image() {}

    public Image(UUID id, URI imageURI) {
        this.id = id;
        this.imageURI = imageURI;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public URI getImageURI() {
        return imageURI;
    }

    public void setImageURI(URI imageURI) {
        this.imageURI = imageURI;
    }

    public List<FoodCourt> getFoodCourt() {
        return foodCourt;
    }

    public void setFoodCourt(List<FoodCourt> foodCourt) {
        this.foodCourt = foodCourt;
    }
}
