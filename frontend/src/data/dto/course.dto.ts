export type CreateCourseRequestDto = {
  code: string;
  name: string;
  description?: string | null;
  ects: number;
  semester: number;
  mandatory: boolean;
  enrollmentLimit?: number | null;
  lecturerId?: number | null;
  studyProgramId?: number | null;
  academicYearId?: number | null;
  active: boolean;
  prerequisiteCourseIds?: number[];
};

export type CourseDto = {
  id: number;
  code: string;
  name: string;
  description: string | null;
  ects: number;
  semester: number;
  mandatory: boolean;
  active: boolean;
  enrollmentLimit: number | null;
  studyProgramId: number | null;
  academicYearId: number | null;
  lecturerId: number | null;
};

export type UpdateCourseRequestDto = {
  any: any;
};
