export type AvailableSemesterCoursesRequestDto = {
  studentId: number;
};

export type AvailableCourseDto = {
  id: number;
  code: string;
  name: string;
  description: string | null;
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

export type CourseAvailabilityStatusDto =
  | "AVAILABLE"
  | "UNAVAILABLE"
  | "ENROLLED"
  | "COMPLETED"
  | string;

export type AvailableCourseItemDto = {
  course: AvailableCourseDto;
  status: CourseAvailabilityStatusDto;
};

export type AvailableSemesterCoursesGroupDto = {
  semester: number;
  courses: AvailableCourseItemDto[];
};

export type AvailableSemesterCoursesResponseDto =
  AvailableSemesterCoursesGroupDto[];
