import { useMemo, useState } from "react";
import type { CreateCourseRequest } from "../../data/dto/course.dto";

type FixedIds = Pick<CreateCourseRequest, "lecturerId" | "studyProgramId" | "academicYearId">;
type FormValues = Omit<CreateCourseRequest, "lecturerId" | "studyProgramId" | "academicYearId">;

type Props = {
  fixed: FixedIds;
  initial?: Partial<FormValues>;
  submitLabel: string;
  onSubmit: (payload: CreateCourseRequest) => Promise<void> | void;
};

function toNumber(v: string) {
  const n = Number(v);
  return Number.isFinite(n) ? n : 0;
}

function getErrorMessage(err: unknown) {
  const e = err as any;
  return (
    e?.response?.data?.message ||
    e?.response?.data?.error ||
    (typeof e?.response?.data === "string" ? e.response.data : null) ||
    e?.message ||
    "Request failed"
  );
}

function genCode() {
  const n = Math.floor((Date.now() / 1000) % 1000000);
  return `CS${n}`;
}

export default function CourseForm({ fixed, initial, submitLabel, onSubmit }: Props) {
  const init = useMemo<FormValues>(() => {
    return {
      code: initial?.code ?? genCode(),
      name: initial?.name ?? "",
      description: initial?.description ?? "",
      ects: initial?.ects ?? 0,
      mandatory: initial?.mandatory ?? true,
      enrollmentLimit: initial?.enrollmentLimit ?? 0,
      semester: initial?.semester ?? 1,
    };
  }, [initial]);

  const [form, setForm] = useState<FormValues>(init);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);

  function set<K extends keyof FormValues>(key: K, value: FormValues[K]) {
    setForm((p) => ({ ...p, [key]: value }));
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError(null);
    setSaving(true);
    try {
      await onSubmit({ ...form, ...fixed });
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setSaving(false);
    }
  }

  return (
    <form className="space-y-4" onSubmit={handleSubmit}>
      {error && (
        <div className="rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800">
          {error}
        </div>
      )}

      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Code</label>
          <div className="flex gap-2">
            <input
              className="w-full rounded-lg border px-3 py-2 outline-none focus:ring-2 focus:ring-gray-200"
              value={form.code}
              onChange={(e) => set("code", e.target.value)}
              required
            />
            <button
              type="button"
              className="px-3 py-2 rounded-lg border bg-white hover:bg-gray-50"
              onClick={() => set("code", genCode())}
              disabled={saving}
            >
              New
            </button>
          </div>
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Name</label>
          <input
            className="w-full rounded-lg border px-3 py-2 outline-none focus:ring-2 focus:ring-gray-200"
            value={form.name}
            onChange={(e) => set("name", e.target.value)}
            required
          />
        </div>
      </div>

      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">Description</label>
        <textarea
          className="w-full rounded-lg border px-3 py-2 outline-none focus:ring-2 focus:ring-gray-200"
          value={form.description}
          onChange={(e) => set("description", e.target.value)}
          rows={4}
        />
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">ECTS</label>
          <input
            type="number"
            className="w-full rounded-lg border px-3 py-2 outline-none focus:ring-2 focus:ring-gray-200"
            value={form.ects}
            onChange={(e) => set("ects", toNumber(e.target.value))}
            min={0}
            required
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Enrollment limit</label>
          <input
            type="number"
            className="w-full rounded-lg border px-3 py-2 outline-none focus:ring-2 focus:ring-gray-200"
            value={form.enrollmentLimit}
            onChange={(e) => set("enrollmentLimit", toNumber(e.target.value))}
            min={0}
            required
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">Semester</label>
          <input
            type="number"
            className="w-full rounded-lg border px-3 py-2 outline-none focus:ring-2 focus:ring-gray-200"
            value={form.semester}
            onChange={(e) => set("semester", toNumber(e.target.value))}
            min={1}
            required
          />
        </div>
      </div>

      <div className="flex items-center gap-2">
        <input
          id="mandatory"
          type="checkbox"
          checked={form.mandatory}
          onChange={(e) => set("mandatory", e.target.checked)}
        />
        <label htmlFor="mandatory" className="text-sm text-gray-700">
          Mandatory
        </label>
      </div>

      <div className="pt-2 flex gap-3">
        <button
          type="submit"
          disabled={saving}
          className="px-4 py-2 rounded-lg border bg-gray-900 text-white hover:bg-gray-800 disabled:opacity-60"
        >
          {saving ? "Saving..." : submitLabel}
        </button>

        <button
          type="button"
          className="px-4 py-2 rounded-lg border bg-white hover:bg-gray-50"
          onClick={() => setForm(init)}
          disabled={saving}
        >
          Reset
        </button>
      </div>
    </form>
  );
}
