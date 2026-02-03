// src/data/services/coursesMineApi.ts

import { httpClient } from "../http/httpClient";
import type {
  CoursesMineRequestDto,
  CoursesMineResponseDto,
} from "../dto/lecturerCourses.dto";

export async function getMyCourses(
  payload: CoursesMineRequestDto
): Promise<CoursesMineResponseDto> {
  const res = await httpClient.get<CoursesMineResponseDto>(
    "/api/courses/mine",
    {
      params: { lecturerId: payload.lecturerId },
    }
  );
  return res.data;
}
