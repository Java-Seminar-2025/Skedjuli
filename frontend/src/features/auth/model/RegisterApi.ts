import { httpClient } from "../../../data/http/httpClient";
import { endpoints } from "../../../data/http/endpoints";
import type { RegisterRequestDto, RegisterResponseDto } from "./auth.dto";

export async function registerApi(
  payload: RegisterRequestDto
): Promise<RegisterResponseDto> {
  const res = await httpClient.post(endpoints.auth.register, payload);
  return res.data as RegisterResponseDto;
}
