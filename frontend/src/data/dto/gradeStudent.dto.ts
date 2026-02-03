export type GradeStudentRequestDto = {
  lecturerId: number;
  studentId: number;
  courseId: number;
  grade: number;
  completionDate: string;
};

export type CompletedCourseResponseDto = {
  id: number;
  studentId: number;
  courseId: number;
  grade: number;
  completionDate: string;
  academicYearId: number;
};

export type GradeStudentResponseDto = CompletedCourseResponseDto;
