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
import ru.otus.hw.dtos.GenreDTO;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.repositories.GenreRepository;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreRepository genreRepository;

    @GetMapping("/api/v1/genres")
    public Flux<GenreDTO> getGenres() {
        return genreRepository.findAll()
                .map(GenreMapper::toDto);
    }

    @GetMapping("/api/v1/genres/{id}")
    public Mono<ResponseEntity<GenreDTO>> getGenre(@PathVariable("id") Long id) {
        return genreRepository.findById(id)
                .map(GenreMapper::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping("/api/v1/genres")
    public Mono<ResponseEntity<GenreDTO>> createGenre(@RequestBody @Valid Mono<GenreDTO> genreDTO) {
        return genreDTO
                .map(GenreMapper::toNewEntity)
                .flatMap(genreRepository::save)
                .map(GenreMapper::toDto)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/api/v1/genres/{id}")
    public Mono<ResponseEntity<GenreDTO>> updateGenre(@PathVariable("id") Long id,
                                                      @RequestBody @Valid Mono<GenreDTO> genreDTO) {
        return genreRepository.findById(id)
                .flatMap(existingGenre -> genreDTO
                        .map(GenreMapper::toEntity)
                        .flatMap(genreRepository::save)
                        .map(GenreMapper::toDto)
                        .map(ResponseEntity::ok))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/api/v1/genres/{id}")
    public Mono<ResponseEntity<Void>> deleteGenre(@PathVariable("id") Long id) {
        return genreRepository.deleteById(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

}
