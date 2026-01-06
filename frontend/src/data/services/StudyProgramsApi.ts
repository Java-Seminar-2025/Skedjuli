import { httpClient } from "../http/httpClient";
import { endpoints } from "../http/endpoints";
import type { StudyProgram } from "../dto/auth.dto";

export const StudyProgramApi = {
  async list(): Promise<StudyProgram[]> {
    const res = await httpClient.get<StudyProgram[]>(
      endpoints.STUDY_PROGRAMS_LIST
    );
    return res.data;
  },
};
