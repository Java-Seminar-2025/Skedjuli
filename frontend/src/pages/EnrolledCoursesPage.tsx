import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

import { getStudentId } from "../helpers/StoredUserHelper";
import { StudentEnrollmentCoursesApi } from "../data/services/StudentEnrollmentCourses.api";
import { StudentEnrollmentCourseDto } from "../data/dto/studentEnrollmentCourse.dto";
import { getErrorMessage } from "../helpers/courseFormHelpers";
export default function EnrolledCoursesPage() {
  const navigate = useNavigate();
  const studentId = useMemo(() => getStudentId(), []);

  const [items, setItems] = useState<StudentEnrollmentCourseDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [q, setQ] = useState("");

  async function load() {
    if (!studentId) {
      setError("StudentId missing (login required)");
      setLoading(false);
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const res = await StudentEnrollmentCoursesApi.list(studentId);
      setItems(res);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, [studentId]);

  const filtered = useMemo(() => {
    const t = q.trim().toLowerCase();
    if (!t) return items;

    return items.filter(
      (x) =>
        x.code.toLowerCase().includes(t) ||
        x.name.toLowerCase().includes(t) ||
        (x.description ?? "").toLowerCase().includes(t)
    );
  }, [items, q]);

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-end p-6">
      <div className="w-[80%] bg-white border rounded-2xl p-6 shadow-sm h-fit max-h-[calc(100vh-48px)] overflow-auto">
        <div className="flex items-start justify-between gap-4">
          <div>
            <h1 className="font-bold text-xl">Enrolled courses</h1>
            <p className="text-sm text-gray-500 mt-1">
              {loading ? "Loading..." : `${filtered.length} course(s)`}
            </p>
          </div>

          <button
            type="button"
            onClick={() => navigate(-1)}
            className="px-4 py-2 rounded-lg border bg-white hover:bg-gray-50"
          >
            Back
          </button>
        </div>

        <div className="mt-6 flex items-center gap-3">
          <input
            value={q}
            onChange={(e) => setQ(e.target.value)}
            className="w-full rounded-lg border px-3 py-2"
            placeholder="Search by code, name, description..."
          />
        </div>

        {error && (
          <div className="mt-4 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800">
            {error}
          </div>
        )}

        {!loading && !error && filtered.length === 0 && (
          <div className="mt-6 rounded-xl border bg-gray-50 px-4 py-6 text-sm text-gray-700">
            No courses found.
          </div>
        )}

        <div className="mt-6 space-y-4">
          {filtered.map((c) => (
            <div
              key={c.id}
              className="rounded-2xl border p-5 hover:shadow-sm transition-shadow"
            >
              <div className="flex items-start justify-between gap-4">
                <div className="min-w-0">
                  <div className="flex items-center gap-3">
                    <span className="inline-flex items-center rounded-lg border bg-white px-2.5 py-1 text-xs font-semibold">
                      {c.code}
                    </span>
                    <h2 className="font-semibold text-lg truncate">{c.name}</h2>
                  </div>

                  {c.description ? (
                    <p className="mt-2 text-sm text-gray-600">
                      {c.description}
                    </p>
                  ) : null}
                </div>

                <div className="shrink-0 flex gap-2"></div>
              </div>

              <div className="mt-4 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-3">
                <div className="rounded-xl border bg-gray-50 px-4 py-3">
                  <div className="text-xs text-gray-500">ECTS</div>
                  <div className="font-semibold">{c.ects}</div>
                </div>

                <div className="rounded-xl border bg-gray-50 px-4 py-3">
                  <div className="text-xs text-gray-500">Semester</div>
                  <div className="font-semibold">{c.semester}</div>
                </div>

                <div className="rounded-xl border bg-gray-50 px-4 py-3">
                  <div className="text-xs text-gray-500">Mandatory</div>
                  <div className="font-semibold">
                    {c.mandatory ? "Yes" : "No"}
                  </div>
                </div>

                <div className="rounded-xl border bg-gray-50 px-4 py-3">
                  <div className="text-xs text-gray-500">Active</div>
                  <div className="font-semibold">{c.active ? "Yes" : "No"}</div>
                </div>
              </div>

              <div className="mt-3 grid grid-cols-1 sm:grid-cols-3 gap-3">
                <div className="rounded-xl border px-4 py-3">
                  <div className="text-xs text-gray-500">Enrollment limit</div>
                  <div className="font-semibold">{c.enrollmentLimit}</div>
                </div>

                <div className="rounded-xl border px-4 py-3">
                  <div className="text-xs text-gray-500">Study program ID</div>
                  <div className="font-semibold">{c.studyProgramId}</div>
                </div>

                <div className="rounded-xl border px-4 py-3">
                  <div className="text-xs text-gray-500">Academic year ID</div>
                  <div className="font-semibold">{c.academicYearId}</div>
                </div>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
