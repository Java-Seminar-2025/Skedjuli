import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getLecturerId } from "../../helpers/StoredUserHelper";
import { getMyCourses } from "../../data/services/GetLecturerCoursesApi";
import type { CoursesMineItemDto } from "../../data/dto/lecturerCourses.dto";
import { deleteCourse } from "../../data/services/DeleteCourse.api";
import { getErrorMessage } from "../../helpers/courseFormHelpers";
import CourseStatCard from "../CourseStatCard";
import AppButton from "../AppButton";

export default function MyCourses() {
  const navigate = useNavigate();
  const lecturerId = useMemo(() => getLecturerId(), []);

  const [items, setItems] = useState<CoursesMineItemDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [q, setQ] = useState("");
  const [deletingId, setDeletingId] = useState<number | null>(null);

  async function load() {
    if (!lecturerId) {
      setError("LecturerId missing (login required)");
      setLoading(false);
      return;
    }

    setLoading(true);
    setError(null);
    try {
      const res = await getMyCourses({ lecturerId });
      setItems(res);
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, [lecturerId]);

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

  async function onDelete(courseId: number) {
    if (!lecturerId) return;

    const ok = window.confirm("Delete this course?");
    if (!ok) return;

    setDeletingId(courseId);
    setError(null);

    try {
      await deleteCourse({ courseId, lecturerId });
      setItems((p) => p.filter((x) => x.id !== courseId));
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setDeletingId(null);
    }
  }

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-end p-6">
      <div className="w-[80%] min-h-screen bg-white border rounded-2xl p-6 shadow-sm">
        <div className="flex items-center justify-between gap-4">
          <div>
            <h1 className="font-bold text-xl">My courses</h1>
            <p className="text-sm text-gray-500 mt-1">
              {loading ? "Loading..." : `${filtered.length} course(s)`}
            </p>
          </div>

          <div className="flex items-center gap-3">
            <AppButton label="Back" onClick={() => navigate(-1)} />
          </div>
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
                <div>
                  <div className="flex items-center gap-3">
                    <span className="inline-flex items-center rounded-lg border bg-white px-2.5 py-1 text-xs font-semibold">
                      {c.code}
                    </span>
                    <h2 className="font-semibold text-lg">{c.name}</h2>
                  </div>

                  {c.description && (
                    <p className="mt-2 text-sm text-gray-600">
                      {c.description}
                    </p>
                  )}
                </div>

                <div className="flex gap-2">
                  <AppButton
                    label="Details"
                    onClick={() => navigate(`/courses/${c.id}`)}
                    disabled={deletingId === c.id}
                    className="px-3 py-2 text-sm"
                  />

                  <AppButton
                    label={deletingId === c.id ? "Deleting..." : "Delete"}
                    onClick={() => onDelete(c.id)}
                    disabled={deletingId === c.id}
                    className="px-3 py-2 text-sm"
                  />
                </div>
              </div>

              <div className="mt-4 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-3">
                <CourseStatCard label="ECTS" value={c.ects} />
                <CourseStatCard label="Semester" value={c.semester} />
                <CourseStatCard
                  label="Enrollment limit"
                  value={c.enrollmentLimit}
                />
                <CourseStatCard
                  label="Mandatory"
                  value={c.mandatory ? "Yes" : "No"}
                />
              </div>

              <div className="mt-3 grid grid-cols-1 sm:grid-cols-3 gap-3">
                <CourseStatCard
                  label="Study program ID"
                  value={c.studyProgramId}
                />
                <CourseStatCard
                  label="Academic year ID"
                  value={c.academicYearId}
                />
                <CourseStatCard
                  label="Active"
                  value={c.active === null ? "N/A" : c.active ? "Yes" : "No"}
                />
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
