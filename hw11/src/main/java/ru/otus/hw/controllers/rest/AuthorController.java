package ru.otus.hw.controllers.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dtos.AuthorDTO;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorRepository authorRepository;

    @GetMapping("/api/v1/authors")
    public Flux<Author> getAuthors() {
        return authorRepository.findAll();
    }

    @GetMapping("/api/v1/authors/{id}")
    public Mono<ResponseEntity<Author>> getAuthor(@PathVariable("id") Long id) {
        return authorRepository.findById(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping("/api/v1/authors")
    public Mono<ResponseEntity<Author>> createAuthor(@RequestBody @Valid AuthorDTO authorDTO) {
        Author author = AuthorMapper.toNewEntity(authorDTO);
        return authorRepository.save(author)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/api/v1/authors/{id}")
    public Mono<ResponseEntity<Author>> updateAuthor(@PathVariable("id") Long id,
                                                     @RequestBody @Valid AuthorDTO authorDTO) {
        return authorRepository.existsById(id)
                .map(exists -> exists ? authorDTO : null)
                .map(AuthorMapper::toEntity)
                .flatMap(authorRepository::save)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/api/v1/authors/{id}")
    public Mono<ResponseEntity<Void>> deleteAuthor(@PathVariable("id") Long id) {
        return authorRepository.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

}
