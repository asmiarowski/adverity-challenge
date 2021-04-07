package com.example.adveritychallenge.service.importer.lock;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ImportLockRepository extends CrudRepository<ImportLock, Integer> {

    Optional<ImportLock> findByImporterType(ImporterType importerType);
}
