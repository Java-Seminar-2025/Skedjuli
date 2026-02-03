import { httpPost } from "../http/httpClient";
import type {
  AddStudentToCourseRequestDto,
  AddStudentToCourseResponseDto,
} from "../dto/addStudentToCourse.dto";

export async function addStudentToCourse(
  payload: AddStudentToCourseRequestDto
): Promise<AddStudentToCourseResponseDto> {
  await httpPost<AddStudentToCourseResponseDto, AddStudentToCourseRequestDto>(
    "/api/courses/my-course/students/add",
    payload
  );
}
