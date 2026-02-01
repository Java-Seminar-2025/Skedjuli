import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

import { getLecturerId } from "../../helpers/StoredUserHelper";
import { getErrorMessage } from "../../helpers/courseFormHelpers";
import {
  isAlreadyEnrolled,
  matchesStudent,
  toIntOrNull,
  todayIso,
} from "../../helpers/courseListHelpers";

import type { CoursesMineItemDto } from "../../data/dto/lecturerCourses.dto";
import type { CourseStudentDto } from "../../data/dto/courseStudents.dto";
import type { StudentDto } from "../../data/dto/studentsList.dto";

import { getMyCourses } from "../../data/services/GetLecturerCoursesApi";
import { deleteCourse } from "../../data/services/DeleteCourse.api";
import { getCourseStudents } from "../../data/services/CourseStudents.api";
import { gradeStudent } from "../../data/services/GradeStudent.api";
import { getStudents } from "../../data/services/studentsList.api";
import { getAvailableSemesterCourses } from "../../data/services/getAvailableSemesterCourses.api";
import { addStudentToCourse } from "../../data/services/addStudentToCourse";

import AppButton from "../AppButton";
import CourseStatCard from "../CourseStatCard";

type GradeMap = Record<number, Record<number, number>>;
type EligMap = Record<number, Record<number, boolean>>;
type AddBlockMap = Record<number, Record<number, string>>;

export default function CourseList() {
  const navigate = useNavigate();
  const lecturerId = useMemo(() => getLecturerId(), []);

  const [items, setItems] = useState<CoursesMineItemDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [q, setQ] = useState("");
  const [deletingId, setDeletingId] = useState<number | null>(null);

  const [expandedId, setExpandedId] = useState<number | null>(null);
  const [studentsByCourseId, setStudentsByCourseId] = useState<
    Record<number, CourseStudentDto[]>
  >({});
  const [studentsLoadingId, setStudentsLoadingId] = useState<number | null>(
    null
  );
  const [studentsErrorByCourseId, setStudentsErrorByCourseId] = useState<
    Record<number, string>
  >({});

  const [gradeInput, setGradeInput] = useState<Record<number, string>>({});
  const [gradingId, setGradingId] = useState<number | null>(null);
  const [gradeOk, setGradeOk] = useState<Record<number, boolean>>({});
  const [gradeErr, setGradeErr] = useState<Record<number, string>>({});
  const [gradedByCourseStudent, setGradedByCourseStudent] = useState<GradeMap>(
    {}
  );

  const [addOpenForCourseId, setAddOpenForCourseId] = useState<number | null>(
    null
  );
  const [studentsCatalog, setStudentsCatalog] = useState<StudentDto[]>([]);
  const [studentsCatalogLoading, setStudentsCatalogLoading] = useState(false);
  const [studentsCatalogError, setStudentsCatalogError] = useState<
    string | null
  >(null);
  const [studentsCatalogPage, setStudentsCatalogPage] = useState(0);
  const [studentsCatalogHasMore, setStudentsCatalogHasMore] = useState(true);
  const [studentSearch, setStudentSearch] = useState("");

  const [eligibleByCourseStudent, setEligibleByCourseStudent] =
    useState<EligMap>({});
  const [eligLoadingStudentId, setEligLoadingStudentId] = useState<
    number | null
  >(null);

  const [addingStudentId, setAddingStudentId] = useState<number | null>(null);
  const [addOkByStudentId, setAddOkByStudentId] = useState<
    Record<number, boolean>
  >({});
  const [addErrByStudentId, setAddErrByStudentId] = useState<
    Record<number, string>
  >({});
  const [addBlockedByCourseStudent, setAddBlockedByCourseStudent] =
    useState<AddBlockMap>({});

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

  async function fetchCourseStudents(courseId: number) {
    setStudentsLoadingId(courseId);
    setStudentsErrorByCourseId((p) => {
      const { [courseId]: _, ...rest } = p;
      return rest;
    });

    try {
      const res = await getCourseStudents({ courseId });
      setStudentsByCourseId((p) => ({ ...p, [courseId]: res }));
    } catch (err) {
      setStudentsErrorByCourseId((p) => ({
        ...p,
        [courseId]: getErrorMessage(err),
      }));
    } finally {
      setStudentsLoadingId(null);
    }
  }

  async function toggleDetails(courseId: number) {
    const next = expandedId === courseId ? null : courseId;
    setExpandedId(next);

    if (next === null) {
      setAddOpenForCourseId(null);
      return;
    }

    if (!studentsByCourseId[courseId]) {
      await fetchCourseStudents(courseId);
    }
  }

  async function onDelete(courseId: number) {
    if (!lecturerId) return;

    const ok = window.confirm("Delete this course?");
    if (!ok) return;

    setDeletingId(courseId);
    setError(null);

    try {
      await deleteCourse({ courseId, lecturerId });
      setItems((p) => p.filter((x) => x.id !== courseId));
      setExpandedId((p) => (p === courseId ? null : p));
      setAddOpenForCourseId((p) => (p === courseId ? null : p));
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setDeletingId(null);
    }
  }

  async function onGrade(courseId: number, student: CourseStudentDto) {
    if (!lecturerId) return;

    const key = student.id;
    const gradeStr = gradeInput[key] ?? "";
    const grade = toIntOrNull(gradeStr);

    setGradeOk((p) => ({ ...p, [key]: false }));
    setGradeErr((p) => {
      const { [key]: _, ...rest } = p;
      return rest;
    });

    if (grade === null) {
      setGradeErr((p) => ({ ...p, [key]: "Grade is required" }));
      return;
    }
    if (grade < 1 || grade > 5) {
      setGradeErr((p) => ({ ...p, [key]: "Grade must be 1-5" }));
      return;
    }

    setGradingId(key);

    try {
      const saved = await gradeStudent({
        lecturerId,
        studentId: student.id,
        courseId,
        grade,
        completionDate: todayIso(),
      });

      setGradedByCourseStudent((p) => ({
        ...p,
        [courseId]: { ...(p[courseId] ?? {}), [student.id]: saved.grade },
      }));

      setGradeOk((p) => ({ ...p, [key]: true }));
      setGradeInput((p) => ({ ...p, [key]: "" }));
    } catch (err) {
      setGradeErr((p) => ({ ...p, [key]: getErrorMessage(err) }));
    } finally {
      setGradingId(null);
      setTimeout(() => {
        setGradeOk((p) => ({ ...p, [key]: false }));
      }, 2000);
    }
  }

  async function loadStudentsCatalog(page: number, reset: boolean) {
    setStudentsCatalogLoading(true);
    setStudentsCatalogError(null);

    try {
      const res = await getStudents({ page, size: 50, sort: "id,asc" });
      setStudentsCatalog((p) => (reset ? res.content : [...p, ...res.content]));
      setStudentsCatalogPage(res.number);
      setStudentsCatalogHasMore(!res.last);
    } catch (err) {
      setStudentsCatalogError(getErrorMessage(err));
    } finally {
      setStudentsCatalogLoading(false);
    }
  }

  async function toggleAddStudent(courseId: number) {
    const next = addOpenForCourseId === courseId ? null : courseId;
    setAddOpenForCourseId(next);
    setStudentSearch("");
    setAddErrByStudentId({});
    setAddOkByStudentId({});

    if (next === null) return;

    if (studentsCatalog.length === 0) {
      await loadStudentsCatalog(0, true);
    }
  }

  const filteredCatalog = useMemo(() => {
    return studentsCatalog.filter((s) => matchesStudent(s, studentSearch));
  }, [studentsCatalog, studentSearch]);

  async function ensureEligibility(courseId: number, studentId: number) {
    if (eligibleByCourseStudent[courseId]?.[studentId] !== undefined) return;

    setEligLoadingStudentId(studentId);

    try {
      const res = await getAvailableSemesterCourses({ studentId });
      const courseIds = new Set<number>();

      for (const g of res) {
        for (const it of g.courses) {
          if (it.status === "AVAILABLE") courseIds.add(it.course.id);
        }
      }

      const ok = courseIds.has(courseId);

      setEligibleByCourseStudent((p) => ({
        ...p,
        [courseId]: { ...(p[courseId] ?? {}), [studentId]: ok },
      }));
    } catch {
      setEligibleByCourseStudent((p) => ({
        ...p,
        [courseId]: { ...(p[courseId] ?? {}), [studentId]: false },
      }));
    } finally {
      setEligLoadingStudentId(null);
    }
  }

  async function onAddStudent(courseId: number, studentId: number) {
    if (!lecturerId) return;

    setAddingStudentId(studentId);
    setAddOkByStudentId((p) => ({ ...p, [studentId]: false }));
    setAddErrByStudentId((p) => {
      const { [studentId]: _, ...rest } = p;
      return rest;
    });

    try {
      await addStudentToCourse({ lecturerId, courseId, studentId });
      setAddOkByStudentId((p) => ({ ...p, [studentId]: true }));
      await fetchCourseStudents(courseId);
    } catch (err) {
      const msg = getErrorMessage(err);

      setAddErrByStudentId((p) => ({ ...p, [studentId]: msg }));
      setAddBlockedByCourseStudent((p) => ({
        ...p,
        [courseId]: { ...(p[courseId] ?? {}), [studentId]: msg },
      }));
    } finally {
      setAddingStudentId(null);
      setTimeout(() => {
        setAddOkByStudentId((p) => ({ ...p, [studentId]: false }));
      }, 2000);
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
            <AppButton
              label="Create"
              variant="primary"
              onClick={() => navigate("/courses/create")}
            />
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
          {filtered.map((c) => {
            const isExpanded = expandedId === c.id;
            const students = studentsByCourseId[c.id] ?? [];
            const studentsError = studentsErrorByCourseId[c.id];
            const studentsLoading = studentsLoadingId === c.id;

            const addOpen = addOpenForCourseId === c.id;

            const enrolledIds = students.map((x) => x.id);

            return (
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
                      label={isExpanded ? "Hide" : "Details"}
                      onClick={() => toggleDetails(c.id)}
                      disabled={deletingId === c.id}
                      className="px-3 py-2 text-sm"
                    />
                    <AppButton
                      label="Edit"
                      onClick={() => navigate(`/courses/${c.id}/edit`)}
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

                {isExpanded && (
                  <div className="mt-5 rounded-2xl border bg-gray-50 p-4">
                    <div className="flex items-center justify-between">
                      <h3 className="font-semibold">Students</h3>
                      <span className="text-sm text-gray-500">
                        {studentsLoading
                          ? "Loading..."
                          : `${students.length} student(s)`}
                      </span>
                    </div>

                    {studentsError && (
                      <div className="mt-3 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800">
                        {studentsError}
                      </div>
                    )}

                    <div className="mt-3 space-y-3">
                      {students.map((s) => {
                        const key = s.id;
                        const ok = gradeOk[key];
                        const err = gradeErr[key];
                        const isSaving = gradingId === key;
                        const savedGrade =
                          gradedByCourseStudent[c.id]?.[s.id] ?? null;

                        return (
                          <div
                            key={s.id}
                            className="rounded-xl border bg-white p-4"
                          >
                            <div className="flex flex-col gap-4 lg:flex-row lg:items-start lg:justify-between">
                              <div>
                                <div className="font-semibold">
                                  {s.user.firstName} {s.user.lastName}
                                </div>
                                <div className="text-sm text-gray-600">
                                  {s.user.email}
                                </div>
                                <div className="text-xs text-gray-500 mt-1">
                                  Username: {s.user.username}
                                </div>

                                <div className="mt-3 flex items-center gap-2">
                                  {savedGrade !== null ? (
                                    <div className="flex items-center gap-2">
                                      <span className="text-sm text-gray-700">
                                        Grade:
                                      </span>
                                      <span className="inline-flex items-center rounded-lg border bg-white px-2.5 py-1 text-sm font-semibold">
                                        {savedGrade}
                                      </span>
                                    </div>
                                  ) : (
                                    <>
                                      <input
                                        value={gradeInput[key] ?? ""}
                                        onChange={(e) =>
                                          setGradeInput((p) => ({
                                            ...p,
                                            [key]: e.target.value,
                                          }))
                                        }
                                        className="w-24 rounded-lg border px-3 py-2 text-sm"
                                        inputMode="numeric"
                                        placeholder="1-5"
                                        disabled={isSaving}
                                      />

                                      <button
                                        type="button"
                                        onClick={() => onGrade(c.id, s)}
                                        disabled={isSaving}
                                        className="px-3 py-2 rounded-lg border bg-white hover:bg-gray-50 text-sm disabled:opacity-60"
                                      >
                                        {isSaving ? "Saving..." : "✓"}
                                      </button>

                                      {ok && (
                                        <span className="text-sm text-green-700">
                                          Saved
                                        </span>
                                      )}
                                    </>
                                  )}
                                </div>

                                {err && (
                                  <div className="mt-2 text-sm text-red-700">
                                    {err}
                                  </div>
                                )}
                              </div>

                              <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
                                <CourseStatCard
                                  label="Year"
                                  value={s.currentYear}
                                />
                                <CourseStatCard
                                  label="Enroll year"
                                  value={s.enrollmentYear}
                                />
                                <CourseStatCard
                                  label="Avg grade"
                                  value={
                                    s.averageGrade === null
                                      ? "N/A"
                                      : s.averageGrade
                                  }
                                />
                                <CourseStatCard
                                  label="ECTS"
                                  value={s.totalEctsEarned}
                                />
                              </div>
                            </div>

                            <div className="mt-3 grid grid-cols-1 sm:grid-cols-3 gap-3">
                              <CourseStatCard label="Student ID" value={s.id} />
                              <CourseStatCard
                                label="User ID"
                                value={s.userId}
                              />
                              <CourseStatCard
                                label="Active"
                                value={s.isActive ? "Yes" : "No"}
                              />
                            </div>
                          </div>
                        );
                      })}
                    </div>

                    <div className="mt-4 flex items-center justify-end">
                      <AppButton
                        label={addOpen ? "Close add student" : "Add student"}
                        onClick={() => toggleAddStudent(c.id)}
                        className="px-3 py-2 text-sm"
                      />
                    </div>

                    {addOpen && (
                      <div className="mt-4 rounded-2xl border bg-white p-4">
                        <div className="flex items-center justify-between gap-3">
                          <h4 className="font-semibold">
                            Add student to course
                          </h4>
                          <span className="text-sm text-gray-500">
                            {studentsCatalogLoading
                              ? "Loading..."
                              : `${filteredCatalog.length} result(s)`}
                          </span>
                        </div>

                        <div className="mt-3 flex items-center gap-3">
                          <input
                            value={studentSearch}
                            onChange={(e) => setStudentSearch(e.target.value)}
                            className="w-full rounded-lg border px-3 py-2"
                            placeholder="Search by name, email, username..."
                          />
                          <AppButton
                            label="Refresh"
                            onClick={() => loadStudentsCatalog(0, true)}
                            disabled={studentsCatalogLoading}
                            className="px-3 py-2 text-sm"
                          />
                        </div>

                        {studentsCatalogError && (
                          <div className="mt-3 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800">
                            {studentsCatalogError}
                          </div>
                        )}

                        <div className="mt-3 space-y-3">
                          {filteredCatalog.slice(0, 25).map((s) => {
                            const enrolled = isAlreadyEnrolled(
                              enrolledIds,
                              s.id
                            );
                            const blockedMsg =
                              addBlockedByCourseStudent[c.id]?.[s.id];
                            const blocked = !!blockedMsg;

                            const elig = eligibleByCourseStudent[c.id]?.[s.id];
                            const eligLoading = eligLoadingStudentId === s.id;

                            const canAdd =
                              !enrolled && !blocked && elig === true;

                            const addOk = addOkByStudentId[s.id];
                            const addErr = addErrByStudentId[s.id];
                            const adding = addingStudentId === s.id;

                            return (
                              <div
                                key={s.id}
                                className="rounded-xl border bg-gray-50 p-4"
                              >
                                <div className="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
                                  <div>
                                    <div className="font-semibold">
                                      {s.user.firstName} {s.user.lastName}
                                    </div>
                                    <div className="text-sm text-gray-600">
                                      {s.user.email}
                                    </div>
                                    <div className="text-xs text-gray-500 mt-1">
                                      Username: {s.user.username}
                                    </div>
                                  </div>

                                  <div className="flex items-center gap-2">
                                    <AppButton
                                      label={
                                        enrolled
                                          ? "Already enrolled"
                                          : blocked
                                          ? "Cannot add"
                                          : eligLoading
                                          ? "Checking..."
                                          : elig === false
                                          ? "Not available"
                                          : addOk
                                          ? "Added"
                                          : adding
                                          ? "Adding..."
                                          : "Add"
                                      }
                                      onClick={async () => {
                                        if (enrolled || blocked) return;

                                        if (elig === undefined) {
                                          await ensureEligibility(c.id, s.id);
                                          return;
                                        }

                                        if (!canAdd) return;
                                        await onAddStudent(c.id, s.id);
                                      }}
                                      disabled={
                                        enrolled ||
                                        blocked ||
                                        studentsCatalogLoading ||
                                        adding ||
                                        (elig !== undefined && !canAdd)
                                      }
                                      className="px-3 py-2 text-sm"
                                    />

                                    {!enrolled &&
                                      !blocked &&
                                      elig === undefined && (
                                        <AppButton
                                          label="Check"
                                          onClick={() =>
                                            ensureEligibility(c.id, s.id)
                                          }
                                          disabled={eligLoading}
                                          className="px-3 py-2 text-sm"
                                        />
                                      )}
                                  </div>
                                </div>

                                {blockedMsg && (
                                  <div className="mt-2 text-sm text-red-700">
                                    {blockedMsg}
                                  </div>
                                )}
                                {addErr && (
                                  <div className="mt-2 text-sm text-red-700">
                                    {addErr}
                                  </div>
                                )}
                              </div>
                            );
                          })}
                        </div>

                        <div className="mt-3 flex items-center justify-between">
                          <span className="text-xs text-gray-500">
                            Showing first 25 results. Use search to narrow down.
                          </span>

                          {studentsCatalogHasMore && (
                            <AppButton
                              label={
                                studentsCatalogLoading
                                  ? "Loading..."
                                  : "Load more"
                              }
                              onClick={() =>
                                loadStudentsCatalog(
                                  studentsCatalogPage + 1,
                                  false
                                )
                              }
                              disabled={studentsCatalogLoading}
                              className="px-3 py-2 text-sm"
                            />
                          )}
                        </div>
                      </div>
                    )}
                  </div>
                )}
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
}
