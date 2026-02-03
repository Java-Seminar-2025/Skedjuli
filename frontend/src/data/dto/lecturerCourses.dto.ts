export type CoursesMineRequestDto = {
  lecturerId: number;
};

export type CoursesMineItemDto = {
  id: number;
  code: string;
  name: string;
  description: string | null;
  ects: number;
  mandatory: boolean;
  enrollmentLimit: number;
  semester: number;
  active: boolean | null;
  lecturerId: number;
  studyProgramId: number;
  academicYearId: number;
};

export type CoursesMineResponseDto = CoursesMineItemDto[];
