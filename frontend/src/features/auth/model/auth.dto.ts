export type UserRole = "STUDENT" | "PROFESSOR" | "ADMIN";

export type RegisterRequestDto = {
  // required
  firstName: string;
  lastName: string;
  email: string;
  role: UserRole;
  password: string;
  confirmPassword: string;
  studyProgramId: number;

  // optional
  department?: string;
  academicTitle?: string;
  officeLocation?: string;
  phoneNumber?: string;
  enrollmentYear?: number;
  currentYear?: number;

  /**
   * LocalDate frmat: "YYYY-MM-DD"
   * npr. "2004-05-10"
   */
  dateOfBirth?: string;
};

export type RegisterResponseDto = {
  token: string; // JWT
  email: string;
};

export type StudyProgram = {
  id: number;
  name: string;
};

//Login

export type LoginRequestDto = {
  email: string;
  password: string;
};
export type LoginResponseDto = {
  token: string;
  email: string;
};
