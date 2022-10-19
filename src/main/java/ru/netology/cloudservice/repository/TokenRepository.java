package ru.netology.cloudservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.netology.cloudservice.entities.TokenEntity;

@Repository
public interface TokenRepository extends CrudRepository<TokenEntity, String> {
}