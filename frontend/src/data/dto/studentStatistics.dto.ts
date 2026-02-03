export type StudentStatisticsResponseDto = {
  studentId: number;

  averageThisAcademicYear: number;
  averageOverall: number;

  cohortAverageThisAcademicYear: number;
  cohortAverageOverall: number;

  deltaVsCohortThisAcademicYear: number;
  deltaVsCohortOverall: number;

  gradedCourses: GradedCourseDto[];
  notPassedCourses: NotPassedCourseDto[];
};

export type GradedCourseDto = {
  courseId: number;
  courseCode: string;
  courseName: string;
  grade: number;
  academicYearId: number;
  academicYearCode: string;
};

export type NotPassedCourseDto = {
  id: number;
  code: string;
  name: string;
  description?: string | null;
  ects: number;
  mandatory: boolean;
  enrollmentLimit: number;
  lecturerId: number | null;
  studyProgramId: number;
  academicYearId: number;
  semester: number;
  active: boolean;
  prerequisiteIds: number[];
};
