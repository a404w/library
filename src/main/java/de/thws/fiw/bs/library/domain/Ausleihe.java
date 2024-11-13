package de.thws.fiw.bs.library.domain;

import java.time.LocalDate;

import de.thws.fiw.bs.library.domain.Buch;
import de.thws.fiw.bs.library.domain.User;

public class Ausleihe {
    long id;
    User user;
    Buch buch;
    LocalDate von;
    LocalDate bis;
}
