package de.thws.fiw.bs.library.domain;

import java.time.LocalDate;

public class Ausleihe {
    long id;
    User user;
    Buch buch;
    LocalDate von;
    LocalDate bis;
}
