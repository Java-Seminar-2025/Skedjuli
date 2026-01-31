import { httpClient } from "../http/httpClient";
import type {
  DeleteCourseRequestDto,
  DeleteCourseResponseDto,
} from "../dto/deleteCourse.dto";

export async function deleteCourse(
  payload: DeleteCourseRequestDto
): Promise<DeleteCourseResponseDto> {
  await httpClient.delete(`/api/courses/${payload.courseId}`, {
    params: { lecturerId: payload.lecturerId },
  });
}
