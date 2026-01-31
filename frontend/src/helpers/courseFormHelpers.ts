export type StudyProgramDto = {
  id: number;
  code: string;
  name: string;
  description?: string | null;
  durationYears?: number;
  totalEcts?: number;
  active?: boolean;
};

export function getErrorMessage(err: unknown) {
  const e = err as any;
  return (
    e?.response?.data?.message ||
    e?.response?.data?.error ||
    (typeof e?.response?.data === "string" ? e.response.data : null) ||
    e?.message ||
    "Create course failed"
  );
}

export function genCode() {
  const n = Math.floor((Date.now() / 1000) % 1000000);
  return `CS${n}`;
}

export function toNumber(v: string) {
  const n = Number(v);
  return Number.isFinite(n) ? n : 0;
}

export function parseIdsCsv(v: string): number[] {
  const t = v.trim();
  if (!t) return [];
  return t
    .split(",")
    .map((x) => Number(x.trim()))
    .filter((n) => Number.isFinite(n));
}

export function toNumberRequired(v: string): number | null {
  const t = v.trim();
  if (!t) return null;
  const n = Number(t);
  return Number.isFinite(n) ? n : null;
}

export function toNumberOrNull(v: string): number | null {
  const t = v.trim();
  if (!t) return null;
  const n = Number(t);
  return Number.isFinite(n) ? n : null;
}
