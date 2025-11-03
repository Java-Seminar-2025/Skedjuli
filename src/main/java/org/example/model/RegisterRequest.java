package org.example.model;

public class RegisterRequest {
    // zajednicka tablica u bazi
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public String role;         // student /professor
    public String dateOfBirth;  //yyyy-MM-dd  moze biti prazno

    // student tablica u bazi
    public Integer studyProgramId;
    public Integer enrollmentYear;
    public Integer currentYear;

    // profesor tablica u bazi
    public String department;
    public String academicTitle;
    public String officeLocation;
    public String phoneNumber;
}
