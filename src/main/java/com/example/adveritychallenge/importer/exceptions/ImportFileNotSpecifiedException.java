package com.example.adveritychallenge.importer.exceptions;

public class ImportFileNotSpecifiedException extends RuntimeException {
    public ImportFileNotSpecifiedException() {
        super("File to be imported was not specified in configuration properties.");
    }
}
