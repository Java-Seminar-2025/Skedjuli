export type UserRole = "STUDENT" | "PROFESSOR";

export type LoginRequestDto = {
  email: string;
  password: string;
};

export type Role = "PROFESSOR" | "STUDENT" | "ADMIN";

export type LoginResponseDto = {
  user: LoginUserDetailsDto;
  studentId: number | null;
  lecturerId: number | null;
};

export type LoginUserDetailsDto = {
  id: number;
  email: string;
  username: string;
  firstName: string;
  lastName: string;
  role: Role;
};

export type RegisterRequestDto = {
  email: string;
  password: string;

  firstName?: string;
  lastName?: string;
  confirmPassword?: string;

  role?: UserRole;
  studyProgramId?: number;

  department?: string;
  academicTitle?: string;
  officeLocation?: string;
  phoneNumber?: string;

  dateOfBirth?: string;
  enrollmentYear?: number;
  currentYear?: number;
};

export type RegisterResponseDto = {
  message?: string;
  token?: string;
};

export type StudyProgram = {
  id: number;
  name: string;
};
