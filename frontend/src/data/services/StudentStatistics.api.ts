import { httpGet } from "../http/httpClient";
import type { StudentStatisticsResponseDto } from "../dto/studentStatistics.dto";

export const StudentStatisticsApi = {
  get(studentId: number): Promise<StudentStatisticsResponseDto> {
    return httpGet<StudentStatisticsResponseDto>(
      `/api/students/${studentId}/analytics`
    );
  },
};
