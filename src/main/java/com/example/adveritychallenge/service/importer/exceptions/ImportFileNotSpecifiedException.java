package com.example.adveritychallenge.service.importer.exceptions;

public class ImportFileNotSpecifiedException extends RuntimeException {
    public ImportFileNotSpecifiedException() {
        super("File to be imported was not specified in configuration properties.");
    }
}
