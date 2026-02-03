import { httpPost } from "../http/httpClient";
import type { LoginRequestDto, LoginResponseDto } from "../dto/auth.dto";

export async function login(payload: LoginRequestDto): Promise<LoginResponseDto> {
  return httpPost<LoginResponseDto>("/api/auth/login", payload);
}

export const LoginApi = { login };
