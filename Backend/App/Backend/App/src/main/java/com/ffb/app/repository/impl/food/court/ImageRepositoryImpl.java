package com.ffb.app.repository.impl.food.court;

import com.ffb.app.repository.api.food.court.ImageRepository;
import com.ffb.model.db.objects.image.Image;
import jakarta.enterprise.context.ApplicationScoped;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class ImageRepositoryImpl implements ImageRepository {

    @Override
    public Optional<Image> getImageByUri(URI uri) {
        return Optional.empty();
    }

    @Override
    public Optional<Image> getImageByID(UUID id) {
        return Optional.empty();
    }
}
