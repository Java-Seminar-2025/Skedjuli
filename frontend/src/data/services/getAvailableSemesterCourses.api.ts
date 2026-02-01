import { httpGet } from "../http/httpClient";
import type {
  AvailableSemesterCoursesRequestDto,
  AvailableSemesterCoursesResponseDto,
} from "../dto/availableSemesterCourses.dto";

export async function getAvailableSemesterCourses(
  payload: AvailableSemesterCoursesRequestDto
): Promise<AvailableSemesterCoursesResponseDto> {
  return httpGet<AvailableSemesterCoursesResponseDto>(
    "/api/students/semester/courses/available",
    { params: { studentId: payload.studentId } }
  );
}
