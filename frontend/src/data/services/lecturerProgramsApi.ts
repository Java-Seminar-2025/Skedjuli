import { httpClient } from "../http/httpClient";
import type {
  LecturerStudyProgramRequestDto,
  LecturerStudyProgramsResponseDto,
} from "../dto/lecturerPrograms.dto";

export async function lecturerProgramsList(
  payload: LecturerStudyProgramRequestDto
): Promise<LecturerStudyProgramsResponseDto> {
  const res = await httpClient.get<LecturerStudyProgramsResponseDto>(
    "/api/studyPrograms/by-lecturer",
    {
      params: { lecturerId: payload.lecturerId },
    }
  );

  return res.data;
}
