import { httpGet } from "../http/httpClient";
import { StudentEnrollmentCoursesResponseDto } from "../dto/studentEnrollmentCourse.dto";

export const StudentEnrollmentCoursesApi = {
  list(studentId: number): Promise<StudentEnrollmentCoursesResponseDto> {
    return httpGet<StudentEnrollmentCoursesResponseDto>(
      `/api/enrollment/student/${studentId}/courses`
    );
  },
};
