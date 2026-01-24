# 🎓 Sustav za upravljanje studentskim upisima

## 👩‍🎓 Student

### ✅ Implementirano
- Registracija / Prijava (API + UI)
- Pregled osobnih podataka
- Kreiranje upisnog lista (enrollment form)
- Spremanje i ažuriranje odabira kolegija
- Pregled upisanih kolegija za tekuću godinu
- Pregled položenih kolegija
- Pregled dostupnih kolegija za upis, uključujući:
  - ECTS bodove
  - tip kolegija (izborni / obavezni)
  - preduvjete za upis
  - limit upisa
  - predavača _(dostupno preko ID-a)_
- PDF export upisnog lista
- Pregled prethodnih upisnih listova  

### 🚧 U razvoju (WIP)

### 📝 Zadaci
- /

---

## 👨‍🏫 Lecturer (Predavač)

### ✅ Implementirano
- Dodavanje novih kolegija
- Uređivanje kolegija:
  - preduvjeti
  - ECTS bodovi
  - tip kolegija (izborni / obavezni)
  - limit upisa
- Pregled i upravljanje vlastitim kolegijima
- Upravljanje preduvjetima kolegija

### 🚧 U razvoju (WIP)
- Zaključavanje upisnog lista (approve workflow je djelomično implementiran)

### 📝 Zadaci
- Pregled liste upisanih studenata po kolegiju  
  _(nakon zaključavanja upisnog lista)_

---

## 👩‍💼 Administrator

### ✅ Implementirano
- Puni pristup sustavu (funkcionalno):
  - korisnici
  - studenti
  - predavači
  - studijski programi
  - akademske godine
  - kolegiji
- Upravljanje akademskim godinama (uključujući aktivnu godinu)
- Upravljanje studijskim programima

### 🚧 U razvoju (WIP)
- Role-based autorizacija 

### 📝 Zadaci
- Ručno dodavanje studenata na kolegije _(nije implementirano)_

---

## 🧩 Dodatne funkcionalnosti

### ✅ Implementirano
- Automatska provjera preduvjeta  
  _(direktni i tranzitivni preduvjeti, uz provjeru položenih kolegija)_
- PDF export upisnih listova

### 🚧 U razvoju (WIP)
- Zaključavanje upisa (potpuno korištenje `approvedBy`, `approvedAt`, `isLocked`)

### 📝 Zadaci
- Notifikacije za rokove upisa
- Analitika uspješnosti studenata
