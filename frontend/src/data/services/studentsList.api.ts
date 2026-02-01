import { httpGet } from "../http/httpClient";
import type {
  StudentsListRequestDto,
  StudentsListResponseDto,
} from "../dto/studentsList.dto";

export async function getStudents(
  payload: StudentsListRequestDto = {}
): Promise<StudentsListResponseDto> {
  const params: Record<string, any> = {};
  if (payload.page !== undefined) params.page = payload.page;
  if (payload.size !== undefined) params.size = payload.size;
  if (payload.sort !== undefined) params.sort = payload.sort;

  return httpGet<StudentsListResponseDto>("/api/students", { params });
}
