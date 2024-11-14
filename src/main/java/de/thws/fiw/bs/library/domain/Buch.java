package de.thws.fiw.bs.library.domain;

import java.time.LocalDate;
import java.util.List;

public class Buch {
    long id;
    String titel;
    String genre;
    String beschreibung;
    int isbn;
    List<Author> authoren;
    int seitenanzahl;
    int kosten;
    LocalDate veröffentlichungsdatum;
    Ausleihe ausgeliehen;
    Reservierung reservierung;
    double preis;
    int ausleiheVerlängert;
}
