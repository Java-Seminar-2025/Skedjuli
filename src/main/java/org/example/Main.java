package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//todo
//landing page
//login/register screen


//dostupni predmeti studentu (ako je npr 1. god da prikaze sve dostupne, ako je 2. prikaz polozenih/ sto moze upisat)
///Logika za godinu studia *ko sta je na nasen faksu*
//- 1. godina → svi obvezni predmeti 1. godine 60 ects ovaj dio je rijesen, treba popravit da vridi za 2. i trecu godinu
//- 2. godina → provjera položenih predmeta 1. godine + obvezni 2. godine 58-62
//- 3. godina → preostali obvezni + izborni predmeti 58-62 bez zavrsnog do 80 za zavrsnin

// upis predmeta api (radi upis predmeta na prvu godinu i vraca upisane predmete sa svih godina)
//upis predmeta page


//profile screen
//prikaz logiranog studenta/profesora
/// student:
//- Osobni podaci + godina studija
//- Ukupni ostvareni ECTS
//- neki pie chart ects ili predmeta ko na studomatu
//- Povijest upisa po godinama
//
//// Za profesora:
///
//- Lista predmeta koje predaje
//- Broj upisanih studenata po predmetu + pise jel ponavlja ili ne?
//- Link do upisane liste studenata

/*
   lukina lista
i doc na kraj dto folderu
e da glavna stvar je sta se jos userima na registeru nije dodijelia oni username o kojem smo pricali pa bi i to tribalo pogledat
popravit selection api jer je completion api radi pa bi selection triba napravit parcijalni upis sta ne radi
imas academic year api
tribalo bi napravit unos predmeta api jos
map struct za mappere - risen userMapper
*/



@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}