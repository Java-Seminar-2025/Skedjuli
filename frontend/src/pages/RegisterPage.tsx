import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import InputField from "../components/InputField";
import { StudyProgramApi } from "../data/services/StudyProgramsApi";

import { registerApi } from "../data/services/RegisterApi";
import type {
  StudyProgram,
  RegisterRequestDto,
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

  const canNext =
    (form.firstName ?? "").trim() &&
    (form.lastName ?? "").trim() &&
    form.email.trim() &&
    form.password.trim() &&
    (form.confirmPassword ?? "").trim() &&
    form.password === form.confirmPassword;

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
    if (!form.studyProgramId || form.studyProgramId <= 0) return;

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
    <div className="bg-gray-100 flex-1 flex flex-row">
      <div className="justify-center flex w-full bg-white ml-4 my-4 rounded-l-lg border">
        <h1 className="self-start p-4 font-semibold">Skedjuli</h1>

        <div className="w-[32%] h-[82%] self-center bg-white rounded-2xl flex flex-col items-center justify-center">
          <h1 className="text-3xl font-bold">Create an Account</h1>
          <p className="text-sm font-light mb-4">
            Start your academic journey today
          </p>

          {error && (
            <div className="text-red-600 text-sm mb-3 w-[58%]">{error}</div>
          )}

          {step === 1 && (
            <div className="w-full flex flex-col items-center justify-center">
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
              <InputField
                name="email"
                label="Email"
                placeholder="john.doe@example.com"
                value={form.email}
                onValueChange={(v) => set("email", v)}
              />
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

              {form.password &&
                form.confirmPassword &&
                form.password !== form.confirmPassword && (
                  <div className="text-red-600 text-sm w-[58%]">
                    Password i confirmPassword moraju biti isti.
                  </div>
                )}

              <button
                type="button"
                onClick={handleNext}
                disabled={!canNext}
                className="bg-blue-500 border w-[58%] self-center mt-4 py-2 rounded-lg hover:bg-blue-700 disabled:opacity-50"
              >
                <span className="text-white">Next</span>
              </button>
            </div>
          )}

          {step === 2 && (
            <div className="w-full flex flex-col items-center justify-center">
              <label className="w-[58%] text-sm mb-1">Role</label>
              <div className="w-[58%] flex gap-2 mb-3">
                <button
                  type="button"
                  onClick={() => setRole("STUDENT")}
                  className={[
                    "w-1/2 py-2 rounded-lg border",
                    form.role === "STUDENT"
                      ? "bg-blue-500 text-white border-blue-500"
                      : "bg-white text-gray-700 hover:bg-gray-50",
                  ].join(" ")}
                >
                  STUDENT
                </button>

                <button
                  type="button"
                  onClick={() => setRole("PROFESSOR")}
                  className={[
                    "w-1/2 py-2 rounded-lg border",
                    form.role === "PROFESSOR"
                      ? "bg-blue-500 text-white border-blue-500"
                      : "bg-white text-gray-700 hover:bg-gray-50",
                  ].join(" ")}
                >
                  PROFESSOR
                </button>
              </div>

              <div className="w-[58%] mb-2">
                <label className="text-sm">Study program</label>

                {programsLoading && (
                  <p className="text-sm mt-1">Učitavanje programa...</p>
                )}
                {programsError && (
                  <p className="text-sm mt-1 text-red-600">{programsError}</p>
                )}

                {!programsLoading && !programsError && (
                  <select
                    className="border rounded p-2 w-full bg-white disabled:opacity-50 disabled:cursor-not-allowed"
                    value={form.studyProgramId || ""}
                    onChange={(e) =>
                      set("studyProgramId", Number(e.target.value))
                    }
                    disabled={programs.length === 0}
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
                <>
                  <InputField
                    name="enrollmentYear"
                    label="Enrollment Year (optional)"
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
                    label="Current Year (optional)"
                    placeholder="2"
                    value={form.currentYear ? String(form.currentYear) : ""}
                    onValueChange={(v) =>
                      set("currentYear", v ? Number(v) : undefined)
                    }
                  />
                  <InputField
                    name="dateOfBirth"
                    label="Date of Birth (optional)"
                    placeholder="2004-05-10"
                    value={form.dateOfBirth ?? ""}
                    onValueChange={(v) => set("dateOfBirth", v || undefined)}
                  />
                </>
              )}

              {form.role === "PROFESSOR" && (
                <>
                  <InputField
                    name="department"
                    label="Department (optional)"
                    placeholder="Computer Science"
                    value={form.department ?? ""}
                    onValueChange={(v) => set("department", v || undefined)}
                  />
                  <InputField
                    name="academicTitle"
                    label="Academic Title (optional)"
                    placeholder="Assistant"
                    value={form.academicTitle ?? ""}
                    onValueChange={(v) => set("academicTitle", v || undefined)}
                  />
                  <InputField
                    name="officeLocation"
                    label="Office Location (optional)"
                    placeholder="Room 203"
                    value={form.officeLocation ?? ""}
                    onValueChange={(v) => set("officeLocation", v || undefined)}
                  />
                  <InputField
                    name="phoneNumber"
                    label="Phone Number (optional)"
                    placeholder="+123456789"
                    value={form.phoneNumber ?? ""}
                    onValueChange={(v) => set("phoneNumber", v || undefined)}
                  />
                </>
              )}

              <div className="w-[58%] flex gap-2 mt-4">
                <button
                  type="button"
                  onClick={() => setStep(1)}
                  className="border w-1/2 py-2 rounded-lg"
                >
                  Back
                </button>

                <button
                  type="button"
                  onClick={handleRegister}
                  disabled={saving}
                  className="bg-blue-500 border w-1/2 py-2 rounded-lg hover:bg-blue-700 disabled:opacity-50"
                >
                  <span className="text-white">
                    {saving ? "Registering..." : "Register"}
                  </span>
                </button>
              </div>
            </div>
          )}
        </div>

        <div className="bg-primary justify-end w-[52%] rounded-2xl my-8 ml-8" />
      </div>
    </div>
  );
}
