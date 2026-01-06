import { httpPost } from "../http/httpClient";
import { endpoints } from "../http/endpoints";
import { LoginRequestDto, LoginResponseDto } from "../dto/auth.dto";

export const LoginApi = {
  login: (payload: LoginRequestDto) =>
    httpPost<LoginResponseDto, LoginRequestDto>(endpoints.auth.login, payload),
};
