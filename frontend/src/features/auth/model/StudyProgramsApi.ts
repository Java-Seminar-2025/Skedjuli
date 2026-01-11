import { httpClient } from "../../../data/http/httpClient";
import { endpoints } from "../../../data/http/endpoints";
import type { StudyProgram } from "./auth.dto";
export const StudyProgramApi = {
  async list(): Promise<StudyProgram[]> {
    const res = await httpClient.get<StudyProgram[]>(
      endpoints.STUDY_PROGRAMS_LIST
    );
    return res.data;
  },
};
