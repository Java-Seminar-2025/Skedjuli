import { httpGet } from "../http/httpClient";
import type {
  LockedEnrollmentFormsRequestDto,
  LockedEnrollmentFormsResponseDto,
} from "../dto/lockedEnrollmentForms.dto";

export async function getLockedEnrollmentForms(
  payload: LockedEnrollmentFormsRequestDto
): Promise<LockedEnrollmentFormsResponseDto> {
  return httpGet<LockedEnrollmentFormsResponseDto>(
    "/api/enrollmentForms/lecturer/locked",
    { params: { lecturerId: payload.lecturerId } }
  );
}
