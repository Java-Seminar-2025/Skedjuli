import { useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

import InputField from "../../components/InputField";
import type { CreateCourseRequestDto } from "../../data/dto/course.dto";
import { createCourse } from "../../data/services/CoursesApi";

function getErrorMessage(err: unknown) {
  const e = err as any;
  return (
    e?.response?.data?.message ||
    e?.response?.data?.error ||
    (typeof e?.response?.data === "string" ? e.response.data : null) ||
    e?.message ||
    "Create course failed"
  );
}

function parseIdsCsv(v: string): number[] | undefined {
  const t = v.trim();
  if (!t) return undefined;

  const ids = t
    .split(",")
    .map((x) => x.trim())
    .filter(Boolean)
    .map((x) => Number(x))
    .filter((n) => Number.isFinite(n));

  return ids.length ? ids : undefined;
}

function toNumberRequired(v: string): number | null {
  const t = v.trim();
  if (!t) return null;
  const n = Number(t);
  return Number.isFinite(n) ? n : null;
}

function toNumberOrNull(v: string): number | null {
  const t = v.trim();
  if (!t) return null;
  const n = Number(t);
  return Number.isFinite(n) ? n : null;
}

export default function CreateNewCourse() {
  const navigate = useNavigate();

  const lecturerIdFromLogin = useMemo(() => {
    const rawUserId = localStorage.getItem("userId");
    if (rawUserId) {
      const n = Number(rawUserId);
      return Number.isFinite(n) ? n : null;
    }

    const rawUser = localStorage.getItem("user");
    if (!rawUser) return null;

    try {
      const u = JSON.parse(rawUser);
      const n = Number(u?.id);
      return Number.isFinite(n) ? n : null;
    } catch {
      return null;
    }
  }, []);

  const [code, setCode] = useState("");
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");

  const [ects, setEcts] = useState("0");
  const [semester, setSemester] = useState("1");
  const [enrollmentLimit, setEnrollmentLimit] = useState("");

  const [studyProgramId, setStudyProgramId] = useState("");
  const [academicYearId, setAcademicYearId] = useState("");

  const [mandatory, setMandatory] = useState(true);
  const [active, setActive] = useState(true);

  const [prereqCsv, setPrereqCsv] = useState("");

  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError(null);

    const ectsNum = toNumberRequired(ects);
    const semNum = toNumberRequired(semester);
    const spIdNum = toNumberRequired(studyProgramId);
    const ayIdNum = toNumberRequired(academicYearId);

    if (!code.trim()) return setError("Code is required");
    if (!name.trim()) return setError("Name is required");
    if (!lecturerIdFromLogin)
      return setError("LecturerId missing (login required)");
    if (!ectsNum || ectsNum <= 0)
      return setError("ECTS must be a positive number");
    if (!semNum || semNum <= 0)
      return setError("Semester must be a positive number");
    if (!spIdNum) return setError("StudyProgramId is required");
    if (!ayIdNum) return setError("AcademicYearId is required");

    const enrollmentLimitNum = toNumberOrNull(enrollmentLimit);

    const payload: CreateCourseRequestDto = {
      code: code.trim(),
      name: name.trim(),
      description: description.trim() ? description.trim() : null,
      ects: ectsNum,
      semester: semNum,
      mandatory,
      enrollmentLimit: enrollmentLimitNum,
      lecturerId: lecturerIdFromLogin,
      studyProgramId: spIdNum,
      academicYearId: ayIdNum,
      active,
      prerequisiteCourseIds: parseIdsCsv(prereqCsv),
    };

    setSaving(true);
    try {
      await createCourse(payload);
      navigate("/home");
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-end p-6">
      <div className="w-[80%] min-h-screen bg-white border rounded-2xl p-6 shadow-sm">
        <div className="flex items-center justify-between">
          <h1 className="font-bold text-xl">Create new course</h1>

          <button
            type="button"
            onClick={() => navigate(-1)}
            className="px-4 py-2 rounded-lg border bg-white hover:bg-gray-50"
          >
            Back
          </button>
        </div>

        {error && (
          <div className="mt-4 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="mt-6 space-y-6">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <InputField
              label="Code"
              value={code}
              onValueChange={setCode}
              type="text"
            />
            <InputField
              label="Name"
              value={name}
              onValueChange={setName}
              type="text"
            />
          </div>

          <div className="space-y-1">
            <label className="text-sm font-medium">Description</label>
            <textarea
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              className="w-full min-h-[110px] rounded-lg border px-3 py-2"
              placeholder="Optional..."
            />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <InputField
              label="ECTS"
              value={ects}
              onValueChange={setEcts}
              type="number"
            />
            <InputField
              label="Semester"
              value={semester}
              onValueChange={setSemester}
              type="number"
            />
            <InputField
              label="Enrollment limit (optional)"
              value={enrollmentLimit}
              onValueChange={setEnrollmentLimit}
              type="number"
            />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="space-y-1">
              <label className="text-sm font-medium">
                Lecturer ID (from login)
              </label>
              <input
                value={lecturerIdFromLogin ?? ""}
                disabled
                className="w-full rounded-lg border px-3 py-2 bg-gray-50"
              />
            </div>

            <InputField
              label="Study program ID"
              value={studyProgramId}
              onValueChange={setStudyProgramId}
              type="number"
            />

            <InputField
              label="Academic year ID"
              value={academicYearId}
              onValueChange={setAcademicYearId}
              type="number"
            />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <label className="flex items-center gap-3 rounded-xl border p-4">
              <input
                type="checkbox"
                checked={mandatory}
                onChange={(e) => setMandatory(e.target.checked)}
              />
              <span className="font-medium">Mandatory</span>
            </label>

            <label className="flex items-center gap-3 rounded-xl border p-4">
              <input
                type="checkbox"
                checked={active}
                onChange={(e) => setActive(e.target.checked)}
              />
              <span className="font-medium">Active</span>
            </label>
          </div>

          <div className="space-y-1">
            <label className="text-sm font-medium">
              Prerequisite course IDs (optional, comma-separated)
            </label>
            <input
              type="text"
              value={prereqCsv}
              onChange={(e) => setPrereqCsv(e.target.value)}
              className="w-full rounded-lg border px-3 py-2"
              placeholder="e.g. 10, 12, 15"
            />
          </div>

          <div className="flex items-center justify-end gap-3">
            <button
              type="button"
              onClick={() => navigate(-1)}
              className="px-4 py-2 rounded-lg border bg-white hover:bg-gray-50"
              disabled={saving}
            >
              Cancel
            </button>

            <button
              type="submit"
              disabled={saving}
              className="px-4 py-2 rounded-lg border bg-gray-900 text-white hover:bg-gray-800 disabled:opacity-60"
            >
              {saving ? "Creating..." : "Create course"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
