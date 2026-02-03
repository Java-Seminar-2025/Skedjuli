import { httpClient } from "../http/httpClient";
import type {
  CourseDto,
  CreateCourseRequestDto,
  UpdateCourseRequestDto,
} from "../dto/course.dto";
// import type { StudentDto } from "../dto/student.dto";

export async function listCourses(): Promise<CourseDto[]> {
  const res = await httpClient.get<CourseDto[]>("/api/courses");
  return res.data;
}

export async function getCourse(id: number): Promise<CourseDto> {
  const res = await httpClient.get<CourseDto>(`/api/courses/${id}`);
  return res.data;
}

export async function createCourse(
  payload: CreateCourseRequestDto
): Promise<CourseDto> {
  const res = await httpClient.post<CourseDto>("/api/courses", payload);
  return res.data;
}

export async function updateCourse(
  id: number,
  payload: UpdateCourseRequestDto
): Promise<CourseDto> {
  const res = await httpClient.put<CourseDto>(`/api/courses/${id}`, payload);
  return res.data;
}

export async function deleteCourse(id: number): Promise<void> {
  await httpClient.delete(`/api/courses/${id}`);
}
