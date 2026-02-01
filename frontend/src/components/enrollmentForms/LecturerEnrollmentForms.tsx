import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import AppButton from "../../components/AppButton";
import CourseStatCard from "../../components/CourseStatCard";
import { getLecturerId } from "../../helpers/StoredUserHelper";
import { getErrorMessage } from "../../helpers/courseFormHelpers";
import type { LockedEnrollmentFormDto } from "../../data/dto/lockedEnrollmentForms.dto";
import { getLockedEnrollmentForms } from "../../data/services/getLockedEnrollmentForms.api";
import {
  approveEnrollmentForm,
  rejectEnrollmentForm,
} from "../../data/services/enrollmentFormsApproval.api";

export default function LecturerEnrollmentForms() {
  const navigate = useNavigate();
  const lecturerId = useMemo(() => getLecturerId(), []);

  const [items, setItems] = useState<LockedEnrollmentFormDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [q, setQ] = useState("");

  const [actingFormId, setActingFormId] = useState<number | null>(null);

  async function load() {
    if (!lecturerId) {
      setError("LecturerId missing (login required)");
      setLoading(false);
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const res = await getLockedEnrollmentForms({ lecturerId });
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

    return items.filter((f) => {
      const full = `${f.studentFirstName} ${f.studentLastName}`.toLowerCase();
      return (
        full.includes(t) ||
        f.studentUsername.toLowerCase().includes(t) ||
        String(f.formId).includes(t) ||
        String(f.semester).includes(t) ||
        String(f.academicYearId).includes(t) ||
        f.items.some(
          (it) =>
            it.courseCode.toLowerCase().includes(t) ||
            it.courseName.toLowerCase().includes(t)
        )
      );
    });
  }, [items, q]);

  async function onApprove(formId: number) {
    if (!lecturerId) return;

    const ok = window.confirm("Approve this enrollment form?");
    if (!ok) return;

    setActingFormId(formId);
    setError(null);

    try {
      await approveEnrollmentForm({ approverUserId: lecturerId, formId });
      setItems((p) => p.filter((x) => x.formId !== formId));
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setActingFormId(null);
    }
  }

  async function onReject(formId: number) {
    if (!lecturerId) return;

    const ok = window.confirm("Reject this enrollment form?");
    if (!ok) return;

    setActingFormId(formId);
    setError(null);

    try {
      await rejectEnrollmentForm({ approverUserId: lecturerId, formId });
      setItems((p) => p.filter((x) => x.formId !== formId));
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setActingFormId(null);
    }
  }

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-end p-6">
      <div className="w-[80%] min-h-screen bg-white border rounded-2xl p-6 shadow-sm">
        <div className="flex items-center justify-between gap-4">
          <div>
            <h1 className="font-bold text-xl">Enrollment forms</h1>
            <p className="text-sm text-gray-500 mt-1">
              {loading ? "Loading..." : `${filtered.length} form(s)`}
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
            placeholder="Search by student, username, course, semester..."
          />
        </div>

        {error && (
          <div className="mt-4 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800">
            {error}
          </div>
        )}

        {!loading && !error && filtered.length === 0 && (
          <div className="mt-6 rounded-xl border bg-gray-50 px-4 py-6 text-sm text-gray-700">
            No locked enrollment forms.
          </div>
        )}

        <div className="mt-6 space-y-4">
          {filtered.map((f) => {
            const disabled = actingFormId === f.formId;

            return (
              <div
                key={f.formId}
                className="rounded-2xl border p-5 hover:shadow-sm transition-shadow"
              >
                <div className="flex items-start justify-between gap-4">
                  <div>
                    <div className="flex items-center gap-3">
                      <span className="inline-flex items-center rounded-lg border bg-white px-2.5 py-1 text-xs font-semibold">
                        Form #{f.formId}
                      </span>
                      <h2 className="font-semibold text-lg">
                        {f.studentFirstName} {f.studentLastName}
                      </h2>
                      <span className="text-sm text-gray-500">
                        @{f.studentUsername}
                      </span>
                    </div>

                    <p className="mt-2 text-sm text-gray-600">
                      Submitted at: {f.submittedAt.replace("T", " ")}
                    </p>
                  </div>

                  <div className="flex gap-2">
                    <AppButton
                      label={disabled ? "Approving..." : "Approve"}
                      onClick={() => onApprove(f.formId)}
                      disabled={disabled}
                      className="px-3 py-2 text-sm"
                    />
                    <AppButton
                      label={disabled ? "Rejecting..." : "Reject"}
                      onClick={() => onReject(f.formId)}
                      disabled={disabled}
                      className="px-3 py-2 text-sm"
                    />
                  </div>
                </div>

                <div className="mt-4 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-3">
                  <CourseStatCard
                    label="Academic year ID"
                    value={f.academicYearId}
                  />
                  <CourseStatCard label="Semester" value={f.semester} />
                  <CourseStatCard label="Items" value={f.items.length} />
                  <CourseStatCard label="Status" value="LOCKED" />
                </div>

                <div className="mt-4 rounded-2xl border bg-gray-50 p-4">
                  <div className="font-semibold mb-3">Courses</div>

                  <div className="space-y-3">
                    {f.items.map((it, idx) => (
                      <div
                        key={`${it.courseCode}-${idx}`}
                        className="rounded-xl border bg-white p-4"
                      >
                        <div className="flex items-start justify-between gap-3">
                          <div>
                            <div className="flex items-center gap-3">
                              <span className="inline-flex items-center rounded-lg border bg-white px-2.5 py-1 text-xs font-semibold">
                                {it.courseCode}
                              </span>
                              <div className="font-semibold">
                                {it.courseName}
                              </div>
                            </div>
                          </div>

                          <div className="grid grid-cols-2 gap-3">
                            <CourseStatCard label="ECTS" value={it.ects} />
                            <CourseStatCard label="Status" value={it.status} />
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
}
