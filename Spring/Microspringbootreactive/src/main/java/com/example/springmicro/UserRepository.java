package com.example.springmicro;

import reactor.core.publisher.Mono;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

    Mono<User> findByName(String name);
}
