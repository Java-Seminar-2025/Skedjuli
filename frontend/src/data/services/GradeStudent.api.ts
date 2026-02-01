import { httpPost } from "../http/httpClient";
import type {
  GradeStudentRequestDto,
  GradeStudentResponseDto,
} from "../dto/gradeStudent.dto";

export async function gradeStudent(
  payload: GradeStudentRequestDto
): Promise<GradeStudentResponseDto> {
  return httpPost<GradeStudentResponseDto, GradeStudentRequestDto>(
    "/api/courses/my-course/grade",
    payload
  );
}
