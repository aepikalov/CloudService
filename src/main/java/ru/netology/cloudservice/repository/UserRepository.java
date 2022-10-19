package ru.netology.cloudservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.netology.cloudservice.entities.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {
}