export type CourseStudentsRequestDto = {
  courseId: number;
};

export type CourseStudentUserDto = {
  id: number;
  email: string;
  username: string;
  firstName: string;
  lastName: string;
  role: string;
};

export type CourseStudentDto = {
  id: number;
  userId: number;
  user: CourseStudentUserDto;
  studyProgramId: number;
  enrollmentYear: number;
  currentYear: number;
  averageGrade: number | null;
  totalEctsEarned: number;
  isActive: boolean;
};

export type CourseStudentsResponseDto = CourseStudentDto[];
