export type StudentsListRequestDto = {
  page?: number;
  size?: number;
  sort?: string;
};

export type StudentUserDto = {
  id: number;
  email: string;
  username: string;
  firstName: string;
  lastName: string;
  role: string;
};

export type StudentDto = {
  id: number;
  userId: number;
  user: StudentUserDto;
  studyProgramId: number;
  enrollmentYear: number;
  currentYear: number;
  averageGrade: number | null;
  totalEctsEarned: number;
  isActive: boolean;
};

export type SortDto = {
  sorted: boolean;
  unsorted: boolean;
  empty: boolean;
};

export type PageableDto = {
  pageNumber: number;
  pageSize: number;
  sort: SortDto;
  offset: number;
  paged: boolean;
  unpaged: boolean;
};

export type StudentsListResponseDto = {
  totalPages: number;
  totalElements: number;
  pageable: PageableDto;
  first: boolean;
  last: boolean;
  numberOfElements: number;
  size: number;
  content: StudentDto[];
  number: number;
  sort: SortDto;
  empty: boolean;
};
