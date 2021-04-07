package com.example.adveritychallenge.service.importer.lock;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class ImportLock {

    public ImportLock(ImporterType importerType) {
        this.importerType = importerType;
    }

    /**
     * The id property is not necessary, importerType could be the primary key here, however id was added deliberately
     * because primary key value should not have a reason to change due to potential issues in database, while name of
     * the importer may change depending on its internal logic and we want to keep that flexibility.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private ImporterType importerType;

    private boolean locked;
}
