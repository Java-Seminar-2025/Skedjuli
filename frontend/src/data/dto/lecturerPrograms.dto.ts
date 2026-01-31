export type LecturerStudyProgramRequestDto = {
  lecturerId: number;
};

export type LecturerStudyProgramsResponseDto = {
  id: number;
  code: string;
  name: string;
  description: string;
  durationYears: number;
  totalEcts: number;
  active: boolean;
};
