import { httpPost } from "../../../data/http/httpClient";
import { endpoints } from "../../../data/http/endpoints";
import { LoginRequestDto, LoginResponseDto } from "./auth.dto";

export const LoginApi = {
  login: (payload: LoginRequestDto) =>
    httpPost<LoginResponseDto, LoginRequestDto>(endpoints.auth.login, payload),
};
