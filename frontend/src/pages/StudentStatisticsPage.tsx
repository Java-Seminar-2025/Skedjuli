import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import { StudentStatisticsApi } from "../data/services/StudentStatistics.api";

import type { StudentStatisticsResponseDto } from "../data/dto/studentStatistics.dto";
import { getStudentId } from "../helpers/StoredUserHelper";

export default function StudentStatisticsPage() {
  const navigate = useNavigate();
  const studentId = getStudentId();

  const [data, setData] = useState<StudentStatisticsResponseDto | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!studentId) {
      setError("Student not logged in");
      setLoading(false);
      return;
    }

    (async () => {
      try {
        const res = await StudentStatisticsApi.get(studentId);
        setData(res);
      } catch (e: any) {
        setError(e?.message ?? "Failed to load statistics");
      } finally {
        setLoading(false);
      }
    })();
  }, [studentId]);

  if (loading) {
    return <div className="p-6">Loading...</div>;
  }

  if (error || !data) {
    return <div className="p-6 text-red-600">{error}</div>;
  }

  return (
    <div className="min-h-screen bg-gray-100 p-6 flex justify-center">
      <div className="w-full max-w-5xl bg-white border rounded-2xl p-6 shadow-sm space-y-6">
        <div className="flex justify-between items-center">
          <h1 className="text-xl font-bold">Your statistics</h1>
          <button
            onClick={() => navigate(-1)}
            className="px-4 py-2 rounded-lg border bg-white hover:bg-gray-50"
          >
            Back
          </button>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <Stat
            label="Average (this year)"
            value={data.averageThisAcademicYear}
          />
          <Stat label="Average (overall)" value={data.averageOverall} />
          <Stat
            label="Cohort avg (this year)"
            value={data.cohortAverageThisAcademicYear}
          />
          <Stat
            label="Cohort avg (overall)"
            value={data.cohortAverageOverall}
          />
          <Stat
            label="Δ vs cohort (year)"
            value={data.deltaVsCohortThisAcademicYear}
          />
          <Stat
            label="Δ vs cohort (overall)"
            value={data.deltaVsCohortOverall}
          />
        </div>

        <Section title="Graded courses">
          {data.gradedCourses.map((c) => (
            <div
              key={c.courseId}
              className="flex justify-between border rounded-lg p-3"
            >
              <div>
                <div className="font-medium">
                  {c.courseCode} – {c.courseName}
                </div>
                <div className="text-sm text-gray-500">
                  {c.academicYearCode}
                </div>
              </div>
              <div className="font-bold text-lg">{c.grade}</div>
            </div>
          ))}
        </Section>

        <Section title="Not passed courses">
          {data.notPassedCourses.map((c) => (
            <div key={c.id} className="border rounded-lg p-3">
              <div className="font-medium">
                {c.code} – {c.name}
              </div>
              <div className="text-sm text-gray-500">
                Semester {c.semester} · {c.ects} ECTS
              </div>
            </div>
          ))}
        </Section>
      </div>
    </div>
  );
}

function Stat({
  label,
  value,
}: {
  label: string;
  value: number | null | undefined;
}) {
  const text =
    typeof value === "number" && Number.isFinite(value)
      ? value.toFixed(2)
      : "N/A";

  return (
    <div className="rounded-xl border bg-gray-50 px-4 py-3">
      <div className="text-xs text-gray-500">{label}</div>
      <div className="font-semibold">{text}</div>
    </div>
  );
}

function Section({
  title,
  children,
}: {
  title: string;
  children: React.ReactNode;
}) {
  return (
    <div className="space-y-3">
      <h2 className="font-semibold text-lg">{title}</h2>
      <div className="space-y-2">{children}</div>
    </div>
  );
}
