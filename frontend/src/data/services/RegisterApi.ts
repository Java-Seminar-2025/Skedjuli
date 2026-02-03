import { httpClient } from "../http/httpClient";
import { endpoints } from "../http/endpoints";
import type { RegisterRequestDto, RegisterResponseDto } from "../dto/auth.dto";

export async function registerApi(
  payload: RegisterRequestDto
): Promise<RegisterResponseDto> {
  const res = await httpClient.post(endpoints.auth.register, payload);
  return res.data as RegisterResponseDto;
}
