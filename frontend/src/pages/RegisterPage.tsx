import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import InputField from "../components/InputField";
import { StudyProgramApi } from "../data/services/StudyProgramsApi";
import { registerApi } from "../data/services/RegisterApi";
import type {
  RegisterRequestDto,
  StudyProgram,
  UserRole,
} from "../data/dto/auth.dto";

function getErrorMessage(err: unknown) {
  const e = err as any;
  return (
    e?.response?.data?.message ||
    e?.response?.data?.error ||
    (typeof e?.response?.data === "string" ? e.response.data : null) ||
    e?.message ||
    "Register failed"
  );
}

export default function RegisterPage() {
  const navigate = useNavigate();

  const [programs, setPrograms] = useState<StudyProgram[]>([]);
  const [programsLoading, setProgramsLoading] = useState(false);
  const [programsError, setProgramsError] = useState<string | null>(null);

  const [step, setStep] = useState<1 | 2>(1);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [form, setForm] = useState<RegisterRequestDto>({
    firstName: "",
    lastName: "",
    email: "",
    role: "STUDENT",
    password: "",
    confirmPassword: "",
    studyProgramId: 0,
    department: undefined,
    academicTitle: undefined,
    officeLocation: undefined,
    phoneNumber: undefined,
    enrollmentYear: undefined,
    currentYear: undefined,
    dateOfBirth: undefined,
  });

  const set = <K extends keyof RegisterRequestDto>(
    key: K,
    value: RegisterRequestDto[K]
  ) => {
    setForm((prev) => ({ ...prev, [key]: value }));
  };

  useEffect(() => {
    let mounted = true;

    (async () => {
      try {
        setProgramsLoading(true);
        setProgramsError(null);

        const list = await StudyProgramApi.list();
        if (!mounted) return;

        setPrograms(list);
        if (list.length > 0) set("studyProgramId", list[0].id);
      } catch (e: any) {
        if (!mounted) return;
        setProgramsError(
          e?.message ?? "Greška pri dohvaćanju studijskih programa."
        );
      } finally {
        if (mounted) setProgramsLoading(false);
      }
    })();

    return () => {
      mounted = false;
    };
  }, []);

  const passwordsMismatch =
    !!form.password &&
    !!form.confirmPassword &&
    form.password !== form.confirmPassword;

  const canNext =
    (form.firstName ?? "").trim() &&
    (form.lastName ?? "").trim() &&
    form.email.trim() &&
    form.password.trim() &&
    (form.confirmPassword ?? "").trim() &&
    !passwordsMismatch;

  function handleNext() {
    if (!canNext) return;
    setError(null);
    setStep(2);
  }

  const setRole = (role: UserRole) => {
    set("role", role);

    if (role === "STUDENT") {
      set("department", undefined);
      set("academicTitle", undefined);
      set("officeLocation", undefined);
      set("phoneNumber", undefined);
    } else {
      set("enrollmentYear", undefined);
      set("currentYear", undefined);
      set("dateOfBirth", undefined);
    }
  };

  async function handleRegister() {
    if (!form.studyProgramId || form.studyProgramId <= 0) {
      setError("Study program is required");
      return;
    }

    setSaving(true);
    setError(null);

    try {
      const payload: RegisterRequestDto = {
        ...form,
        firstName: form.firstName?.trim() ?? "",
        lastName: form.lastName?.trim() ?? "",
        email: form.email.trim(),
        department: form.department?.trim() || undefined,
        academicTitle: form.academicTitle?.trim() || undefined,
        officeLocation: form.officeLocation?.trim() || undefined,
        phoneNumber: form.phoneNumber?.trim() || undefined,
        dateOfBirth: form.dateOfBirth?.trim() || undefined,
        enrollmentYear: form.enrollmentYear ?? undefined,
        currentYear: form.currentYear ?? undefined,
      };

      await registerApi(payload);
      navigate("/login");
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="min-h-screen bg-gray-100 p-6 flex items-center justify-center">
      <div className="w-full max-w-4xl bg-white border rounded-2xl p-6 shadow-sm">
        <div className="flex items-start justify-between gap-4">
          <div>
            <div className="text-sm text-gray-500">Skedjuli</div>
            <h1 className="font-bold text-xl mt-1">Create an account</h1>
            <p className="text-sm text-gray-500 mt-1">Step {step} of 2</p>
          </div>

          <div className="flex items-center gap-3">
            {step === 2 && (
              <button
                type="button"
                onClick={() => setStep(1)}
                className="px-4 py-2 rounded-lg border bg-white hover:bg-gray-50"
                disabled={saving}
              >
                Back
              </button>
            )}

            <button
              type="button"
              onClick={() => navigate("/login")}
              className="px-4 py-2 rounded-lg border bg-white hover:bg-gray-50"
              disabled={saving}
            >
              Login
            </button>
          </div>
        </div>

        {error && (
          <div className="mt-4 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800">
            {error}
          </div>
        )}

        {step === 1 && (
          <div className="mt-6 space-y-4">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <InputField
                name="firstName"
                label="Name"
                placeholder="John"
                value={form.firstName ?? ""}
                onValueChange={(v) => set("firstName", v)}
              />
              <InputField
                name="lastName"
                label="Last Name"
                placeholder="Doe"
                value={form.lastName ?? ""}
                onValueChange={(v) => set("lastName", v)}
              />
            </div>

            <InputField
              name="email"
              label="Email"
              placeholder="john.doe@example.com"
              value={form.email}
              onValueChange={(v) => set("email", v)}
            />

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <InputField
                name="password"
                label="Password"
                placeholder="..."
                type="password"
                value={form.password}
                onValueChange={(v) => set("password", v)}
              />
              <InputField
                name="confirmPassword"
                label="Confirm Password"
                placeholder="..."
                type="password"
                value={form.confirmPassword ?? ""}
                onValueChange={(v) => set("confirmPassword", v)}
              />
            </div>

            {passwordsMismatch && (
              <div className="text-sm text-red-700">
                Password i confirmPassword moraju biti isti.
              </div>
            )}

            <div className="pt-2 flex items-center justify-end gap-3">
              <button
                type="button"
                onClick={() => navigate("/login")}
                className="px-4 py-2 rounded-lg border bg-white hover:bg-gray-50"
              >
                Cancel
              </button>

              <button
                type="button"
                onClick={handleNext}
                disabled={!canNext}
                className="px-4 py-2 rounded-lg border bg-gray-900 text-white hover:bg-gray-800 disabled:opacity-60"
              >
                Next
              </button>
            </div>
          </div>
        )}

        {step === 2 && (
          <div className="mt-6 space-y-4">
            <div className="rounded-2xl border bg-gray-50 p-4">
              <div className="text-sm font-medium mb-2">Role</div>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-2">
                <button
                  type="button"
                  onClick={() => setRole("STUDENT")}
                  className={[
                    "px-4 py-2 rounded-lg border text-sm",
                    form.role === "STUDENT"
                      ? "bg-gray-900 text-white border-gray-900"
                      : "bg-white hover:bg-gray-50",
                  ].join(" ")}
                  disabled={saving}
                >
                  STUDENT
                </button>

                <button
                  type="button"
                  onClick={() => setRole("PROFESSOR")}
                  className={[
                    "px-4 py-2 rounded-lg border text-sm",
                    form.role === "PROFESSOR"
                      ? "bg-gray-900 text-white border-gray-900"
                      : "bg-white hover:bg-gray-50",
                  ].join(" ")}
                  disabled={saving}
                >
                  PROFESSOR
                </button>
              </div>
            </div>

            <div className="space-y-1">
              <label className="text-sm font-medium">Study program</label>

              {programsLoading && (
                <div className="text-sm text-gray-600">
                  Učitavanje programa...
                </div>
              )}
              {programsError && (
                <div className="text-sm text-red-700">{programsError}</div>
              )}

              {!programsLoading && !programsError && (
                <select
                  className="w-full rounded-lg border px-3 py-2 bg-white disabled:opacity-60"
                  value={form.studyProgramId || ""}
                  onChange={(e) =>
                    set("studyProgramId", Number(e.target.value))
                  }
                  disabled={saving || programs.length === 0}
                >
                  {programs.length === 0 ? (
                    <option value="">Nema dostupnih programa</option>
                  ) : (
                    programs.map((p) => (
                      <option key={p.id} value={p.id}>
                        {p.name}
                      </option>
                    ))
                  )}
                </select>
              )}
            </div>

            {form.role === "STUDENT" && (
              <div className="rounded-2xl border bg-gray-50 p-4 space-y-4">
                <div className="text-sm font-medium">
                  Student details (optional)
                </div>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <InputField
                    name="enrollmentYear"
                    label="Enrollment Year"
                    placeholder="2023"
                    value={
                      form.enrollmentYear ? String(form.enrollmentYear) : ""
                    }
                    onValueChange={(v) =>
                      set("enrollmentYear", v ? Number(v) : undefined)
                    }
                  />
                  <InputField
                    name="currentYear"
                    label="Current Year"
                    placeholder="2"
                    value={form.currentYear ? String(form.currentYear) : ""}
                    onValueChange={(v) =>
                      set("currentYear", v ? Number(v) : undefined)
                    }
                  />
                </div>

                <InputField
                  name="dateOfBirth"
                  label="Date of Birth"
                  placeholder="2004-05-10"
                  value={form.dateOfBirth ?? ""}
                  onValueChange={(v) => set("dateOfBirth", v || undefined)}
                />
              </div>
            )}

            {form.role === "PROFESSOR" && (
              <div className="rounded-2xl border bg-gray-50 p-4 space-y-4">
                <div className="text-sm font-medium">
                  Professor details (optional)
                </div>
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <InputField
                    name="department"
                    label="Department"
                    placeholder="Computer Science"
                    value={form.department ?? ""}
                    onValueChange={(v) => set("department", v || undefined)}
                  />
                  <InputField
                    name="academicTitle"
                    label="Academic Title"
                    placeholder="Assistant"
                    value={form.academicTitle ?? ""}
                    onValueChange={(v) => set("academicTitle", v || undefined)}
                  />
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <InputField
                    name="officeLocation"
                    label="Office Location"
                    placeholder="Room 203"
                    value={form.officeLocation ?? ""}
                    onValueChange={(v) => set("officeLocation", v || undefined)}
                  />
                  <InputField
                    name="phoneNumber"
                    label="Phone Number"
                    placeholder="+123456789"
                    value={form.phoneNumber ?? ""}
                    onValueChange={(v) => set("phoneNumber", v || undefined)}
                  />
                </div>
              </div>
            )}

            <div className="pt-2 flex items-center justify-end gap-3">
              <button
                type="button"
                onClick={() => setStep(1)}
                className="px-4 py-2 rounded-lg border bg-white hover:bg-gray-50"
                disabled={saving}
              >
                Back
              </button>

              <button
                type="button"
                onClick={handleRegister}
                disabled={saving}
                className="px-4 py-2 rounded-lg border bg-gray-900 text-white hover:bg-gray-800 disabled:opacity-60"
              >
                {saving ? "Registering..." : "Register"}
              </button>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
