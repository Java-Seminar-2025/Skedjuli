export type Role = "PROFESSOR" | "STUDENT" | "ADMIN";

export type StoredLoginResponse = {
  user: {
    id: number;
    email: string;
    username: string;
    firstName: string;
    lastName: string;
    role: Role | string;
  };
  studentId: number | null;
  lecturerId: number | null;
};

const KEY = "user";

function isFiniteNumber(x: unknown): x is number {
  return typeof x === "number" && Number.isFinite(x);
}

function toNumberOrNull(x: unknown): number | null {
  if (x == null) return null;
  const n = typeof x === "number" ? x : Number(x);
  return Number.isFinite(n) ? n : null;
}

export function setStoredUser(data: StoredLoginResponse): void {
  localStorage.setItem(KEY, JSON.stringify(data));
}

export function getStoredUser(): StoredLoginResponse | null {
  const raw = localStorage.getItem(KEY);
  if (!raw) return null;

  try {
    const x = JSON.parse(raw) as any;

    if (!x || typeof x !== "object") return null;
    if (!x.user || typeof x.user !== "object") return null;
    if (!isFiniteNumber(Number(x.user.id))) return null;

    return {
      user: {
        id: Number(x.user.id),
        email: String(x.user.email ?? ""),
        username: String(x.user.username ?? ""),
        firstName: String(x.user.firstName ?? ""),
        lastName: String(x.user.lastName ?? ""),
        role: (x.user.role ?? "") as string,
      },
      studentId: toNumberOrNull(x.studentId),
      lecturerId: toNumberOrNull(x.lecturerId),
    };
  } catch {
    return null;
  }
}

export function clearStoredUser(): void {
  localStorage.removeItem(KEY);
}

export function getUserDetails() {
  return getStoredUser()?.user ?? null;
}

export function getUserId(): number | null {
  return getStoredUser()?.user?.id ?? null;
}

export function getLecturerId(): number | null {
  return getStoredUser()?.lecturerId ?? null;
}

export function getStudentId(): number | null {
  return getStoredUser()?.studentId ?? null;
}
