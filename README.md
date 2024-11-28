# library
Projekt für BackendSystems

# Struktur:
src/main/java/de/thws/fiw/bs/library
│
├── domain                  
│   ├── model             
│   │   ├── Book.java            // Buchart mit Titel, Genre, ISBN, Autoren
│   │   ├── BookCopy.java        // Einzelnes Exemplar eines Buches
│   │   ├── Author.java          // Autorenmodell
│   │   ├── User.java            // Nutzer mit Ausleihen und Reservierungen
│   │   └── Reservation.java     // Modell für Reservierungen
│   │
│   ├── service              
│   │   ├── BorrowService.java   // Logik für Ausleihen von Büchern
│   │   ├── ReturnService.java   // Logik für Rückgaben
│   │   ├── ReservationService.java // Logik für Reservierungen
│   │   └── BookSearchService.java  // Logik für Suche und Filterung
│   │
│   └── ports                
│       ├── BookRepository.java      // Schnittstelle für Bücher
│       ├── BookCopyRepository.java  // Schnittstelle für Exemplare
│       ├── AuthorRepository.java    // Schnittstelle für Autoren
│       ├── UserRepository.java      // Schnittstelle für Nutzer
│       └── ReservationRepository.java // Schnittstelle für Reservierungen
│
├── api                     
│   ├── controller          
│   │   ├── BookController.java       // REST-Endpoints für Bücher
│   │   ├── AuthorController.java     // Endpoints für Autoren
│   │   ├── UserController.java       // Endpoints für Nutzer
│   │   └── ReservationController.java // Endpoints für Reservierungen
│   │
│   ├── dto                 
│   │   ├── BookDTO.java           // Data Transfer Object für Bücher
│   │   ├── BookCopyDTO.java       // DTO für Buch-Exemplare
│   │   ├── AuthorDTO.java         // DTO für Autoren
│   │   ├── UserDTO.java           // DTO für Nutzer
│   │   └── ReservationDTO.java    // DTO für Reservierungen
│   │
│   └── exception            
│       ├── BookNotFoundException.java
│       ├── AuthorNotFoundException.java
│       ├── ReservationNotFoundException.java
│       └── UserNotFoundException.java
│
├── persistence             
│   ├── repository          
│   │   ├── JpaBookRepository.java
│   │   ├── JpaBookCopyRepository.java
│   │   ├── JpaAuthorRepository.java
│   │   ├── JpaUserRepository.java
│   │   └── JpaReservationRepository.java
│   │
│   ├── entity              
│   │   ├── BookEntity.java
│   │   ├── BookCopyEntity.java
│   │   ├── AuthorEntity.java
│   │   ├── UserEntity.java
│   │   └── ReservationEntity.java
│   │
│   └── mapper              
│       ├── BookMapper.java
│       ├── BookCopyMapper.java
│       ├── AuthorMapper.java
│       ├── UserMapper.java
│       └── ReservationMapper.java
│
├── config                  
│   └── AppConfig.java
│
└── Application.java