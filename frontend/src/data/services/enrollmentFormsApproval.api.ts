import { httpPost } from "../http/httpClient";
import type {
  ApproveRejectEnrollmentFormRequestDto,
  ApproveRejectEnrollmentFormResponseDto,
} from "../dto/approveRejectEnrollmentForm.dto";

export async function approveEnrollmentForm(
  payload: ApproveRejectEnrollmentFormRequestDto
): Promise<ApproveRejectEnrollmentFormResponseDto> {
  await httpPost<ApproveRejectEnrollmentFormResponseDto, undefined>(
    "/api/enrollmentForms/lecturer/approve",
    undefined,
    {
      params: {
        approverUserId: payload.approverUserId,
        formId: payload.formId,
      },
    }
  );
}

export async function rejectEnrollmentForm(
  payload: ApproveRejectEnrollmentFormRequestDto
): Promise<ApproveRejectEnrollmentFormResponseDto> {
  await httpPost<ApproveRejectEnrollmentFormResponseDto, undefined>(
    "/api/enrollmentForms/lecturer/reject",
    undefined,
    {
      params: {
        approverUserId: payload.approverUserId,
        formId: payload.formId,
      },
    }
  );
}
