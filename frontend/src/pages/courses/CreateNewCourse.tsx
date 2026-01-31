import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

import InputField from "../../components/InputField";
import type { CreateCourseRequestDto } from "../../data/dto/course.dto";
import { createCourse } from "../../data/services/CoursesApi";
import { getLecturerId } from "../../helpers/StoredUserHelper";
import { lecturerProgramsList } from "../../data/services/lecturerProgramsApi";
import type { StudyProgramDto } from "../../helpers/courseFormHelpers";
import {
  getErrorMessage,
  parseIdsCsv,
  toNumberRequired,
  toNumberOrNull,
} from "../../helpers/courseFormHelpers";

export default function CreateNewCourse() {
  const navigate = useNavigate();
  const lecturerIdFromLogin = useMemo(() => getLecturerId(), []);

  const [code, setCode] = useState("");
  const [name, setName] = useState("");
  const [description, setDescription] = useState("");

  const [ects, setEcts] = useState("0");
  const [semester, setSemester] = useState("1");
  const [enrollmentLimit, setEnrollmentLimit] = useState("");

  const [mandatory, setMandatory] = useState(true);
  const [active, setActive] = useState(true);

  const [prereqCsv, setPrereqCsv] = useState("");

  const [programs, setPrograms] = useState<StudyProgramDto[]>([]);
  const [studyProgramId, setStudyProgramId] = useState<number | "">("");

  const [saving, setSaving] = useState(false);
  const [loadingPrograms, setLoadingPrograms] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!lecturerIdFromLogin) return;

    (async () => {
      setLoadingPrograms(true);
      try {
        const resp = await lecturerProgramsList({
          lecturerId: lecturerIdFromLogin,
        } as any);

        const list: StudyProgramDto[] = Array.isArray(resp)
          ? resp
          : (resp as any)?.items ?? [];

        setPrograms(list);

        if (list.length > 0) setStudyProgramId(list[0].id);
      } catch (err) {
        setError(getErrorMessage(err));
      } finally {
        setLoadingPrograms(false);
      }
    })();
  }, [lecturerIdFromLogin]);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError(null);

    const ectsNum = toNumberRequired(ects);
    const semNum = toNumberRequired(semester);

    if (!code.trim()) return setError("Code is required");
    if (!name.trim()) return setError("Name is required");
    if (!lecturerIdFromLogin)
      return setError("LecturerId missing (login required)");
    if (!ectsNum || ectsNum <= 0)
      return setError("ECTS must be a positive number");
    if (!semNum || semNum <= 0)
      return setError("Semester must be a positive number");
    if (!studyProgramId) return setError("Study program is required");

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
      active,
      prerequisiteCourseIds: parseIdsCsv(prereqCsv),

      studyProgramId: Number(studyProgramId),
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
          <div className="space-y-1">
            <label className="text-sm font-medium">Study program</label>
            <select
              value={studyProgramId}
              onChange={(e) =>
                setStudyProgramId(e.target.value ? Number(e.target.value) : "")
              }
              disabled={loadingPrograms || programs.length === 0}
              className="w-full rounded-lg border px-3 py-2 bg-white disabled:opacity-60"
            >
              {loadingPrograms && <option>Loading...</option>}

              {!loadingPrograms && programs.length === 0 && (
                <option value="">No programs found</option>
              )}

              {!loadingPrograms &&
                programs.map((p) => (
                  <option key={p.id} value={p.id}>
                    {p.name}
                  </option>
                ))}
            </select>

            {!loadingPrograms && programs.length > 0 && (
              <p className="text-xs text-gray-500">
                Selected program ID: {studyProgramId}
              </p>
            )}
          </div>

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
