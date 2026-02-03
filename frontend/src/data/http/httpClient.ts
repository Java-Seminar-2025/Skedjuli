import axios, {
  type AxiosInstance,
  type AxiosRequestConfig,
  AxiosError,
} from "axios";

export const httpClient: AxiosInstance = axios.create({
  baseURL: "",
  timeout: 10000,
});

httpClient.interceptors.response.use(
  (response) => response,
  (error: AxiosError<any>) => {
    const status = error.response?.status;

    const messageFromBody =
      typeof error.response?.data?.message === "string"
        ? error.response?.data?.message
        : typeof error.response?.data === "string"
        ? error.response?.data
        : null;

    const message = messageFromBody ?? (error.message || "Request failed");

    return Promise.reject({ status, message, raw: error });
  }
);

export const httpGet = async <T>(
  url: string,
  config?: AxiosRequestConfig
): Promise<T> => {
  const res = await httpClient.get<T>(url, config);
  return res.data;
};

export const httpPost = async <TResponse, TBody = unknown>(
  url: string,
  body?: TBody,
  config?: AxiosRequestConfig
): Promise<TResponse> => {
  const res = await httpClient.post<TResponse>(url, body, config);
  return res.data;
};

export const httpPut = async <TResponse, TBody = unknown>(
  url: string,
  body?: TBody,
  config?: AxiosRequestConfig
): Promise<TResponse> => {
  const res = await httpClient.put<TResponse>(url, body, config);
  return res.data;
};

export const httpPatch = async <TResponse, TBody = unknown>(
  url: string,
  body?: TBody,
  config?: AxiosRequestConfig
): Promise<TResponse> => {
  const res = await httpClient.patch<TResponse>(url, body, config);
  return res.data;
};

export const httpDelete = async <TResponse = void>(
  url: string,
  config?: AxiosRequestConfig
): Promise<TResponse> => {
  const res = await httpClient.delete<TResponse>(url, config);
  return res.data;
};
