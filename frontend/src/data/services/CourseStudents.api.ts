import { httpGet } from "../http/httpClient";
import type {
  CourseStudentsRequestDto,
  CourseStudentsResponseDto,
} from "../dto/courseStudents.dto";

export async function getCourseStudents(
  payload: CourseStudentsRequestDto
): Promise<CourseStudentsResponseDto> {
  return httpGet<CourseStudentsResponseDto>(
    `/api/courses/${payload.courseId}/students`
  );
}
