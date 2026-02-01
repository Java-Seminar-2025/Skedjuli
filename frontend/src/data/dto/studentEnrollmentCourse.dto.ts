export type StudentEnrollmentCourseDto = {
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

export type StudentEnrollmentCoursesResponseDto = StudentEnrollmentCourseDto[];
