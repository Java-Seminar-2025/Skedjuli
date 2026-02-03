import { httpClient } from "../http/httpClient";

function authHeaders() {
  const token = localStorage.getItem("token");
  return token ? { Authorization: `Bearer ${token}` } : {};
}

export type MeDto = {
  id?: number;
  email?: string;
  role?: string;
  lecturerId?: number;
  studyProgramId?: number;
  lecturer?: { id?: number };
  studyProgram?: { id?: number };
};

export async function getMe(): Promise<MeDto> {
  const urls = ["/api/auth/me", "/api/users/me", "/api/me"];

  for (const url of urls) {
    try {
      const res = await httpClient.get<MeDto>(url, { headers: authHeaders() });
      return res.data;
    } catch (e: any) {
      if (e?.response?.status === 404) continue;
      if (e?.response?.status === 401) break;
      if (e?.response?.status === 403) break;
      throw e;
    }
  }

  return {};
}
