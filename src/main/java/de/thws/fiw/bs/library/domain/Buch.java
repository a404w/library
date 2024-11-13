package de.thws.fiw.bs.library.domain;

import java.time.LocalDate;

public class Buch {
    long id;
    String titel;
    String genre;
    String beschreibung;
    int isbn;
    Author author;
    int seitenanzahl;
    int kosten;
    LocalDate veröffentlichungsdatum;
    Ausleihe ausgeliehen;
    Reservierung reservierung;
    double preis;
    int ausleiheVerlängert;
}
