export type LockedEnrollmentFormsRequestDto = {
  lecturerId: number;
};

export type LockedEnrollmentFormItemDto = {
  courseCode: string;
  courseName: string;
  ects: number;
  status: number;
};

export type LockedEnrollmentFormDto = {
  formId: number;
  studentFirstName: string;
  studentLastName: string;
  studentUsername: string;
  academicYearId: number;
  semester: number;
  submittedAt: string;
  items: LockedEnrollmentFormItemDto[];
};

export type LockedEnrollmentFormsResponseDto = LockedEnrollmentFormDto[];
