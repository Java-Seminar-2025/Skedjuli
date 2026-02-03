export function toIntOrNull(v: string): number | null {
  const t = v.trim();
  if (!t) return null;
  const n = Number(t);
  return Number.isInteger(n) ? n : null;
}

export function todayIso(): string {
  const d = new Date();
  const y = d.getFullYear();
  const m = String(d.getMonth() + 1).padStart(2, "0");
  const day = String(d.getDate()).padStart(2, "0");
  return `${y}-${m}-${day}`;
}

type HasUserBasics = {
  user: {
    firstName: string;
    lastName: string;
    email: string;
    username: string;
  };
};

export function matchesStudent<T extends HasUserBasics>(s: T, query: string): boolean {
  const t = query.trim().toLowerCase();
  if (!t) return true;
  const full = `${s.user.firstName} ${s.user.lastName}`.toLowerCase();
  return (
    full.includes(t) ||
    s.user.email.toLowerCase().includes(t) ||
    s.user.username.toLowerCase().includes(t)
  );
}

export function isAlreadyEnrolled(
  enrolledStudentIds: number[],
  studentId: number
): boolean {
  return enrolledStudentIds.includes(studentId);
}
