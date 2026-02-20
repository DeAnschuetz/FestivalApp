package com.ffb.app.repository.api.food.court;

import com.ffb.model.db.objects.image.Image;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

public interface ImageRepository extends PanacheRepositoryBase<Image, UUID>{


    Optional<Image> getImageByUri(URI uri);

    Optional<Image> getImageByID(UUID id);

}
