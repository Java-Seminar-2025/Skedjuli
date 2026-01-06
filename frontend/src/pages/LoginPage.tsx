import { useEffect, useState } from "react";
import InputField from "../components/InputField";
import { LoginRequestDto, LoginResponseDto } from "../data/dto/auth.dto";
import { LoginApi } from "../data/services/LoginApi";
import { useNavigate } from "react-router-dom";
export default function LoginPage() {
  const Navigation = useNavigate();

  const [error, setError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const set = <K extends keyof LoginRequestDto>(
    key: K,
    value: LoginRequestDto[K]
  ) => {
    setForm((prev) => ({ ...prev, [key]: value }));
  };

  const [rememberMe, setRememberMe] = useState<boolean>(() => {
    return Boolean(localStorage.getItem("token"));
  });

  useEffect(() => {
    const savedEmail =
      localStorage.getItem("email") ?? sessionStorage.getItem("email");
    if (savedEmail) {
      setForm((prev) => ({ ...prev, email: savedEmail }));
    }
  }, []);

  const [form, setForm] = useState<LoginRequestDto>({
    email: "",
    password: "",
  });

  async function handleLogin() {
    console.log("handleLogin START", form);
    setError(null);

    if (!form.email.trim() || !form.password.trim()) {
      setError("Email and password are required to login...");
      return;
    }

    if (isSubmitting) return; // !doubleckick

    try {
      setIsSubmitting(true);
      console.log("calling LoginApi.login...");

      const data = await LoginApi.login(form);

      console.log("login OK, data:", data);

      localStorage.removeItem("token");
      localStorage.removeItem("email");
      sessionStorage.removeItem("token");
      sessionStorage.removeItem("email");

      const storage = rememberMe ? localStorage : sessionStorage;

      storage.setItem("token", data.token);
      storage.setItem("email", data.email);

      Navigation("/Home");
    } catch (err: any) {
      console.error("login FAILED:", err);

      const msg =
        err?.message ??
        (typeof err === "string" ? err : "Error while logging in");

      setError(msg);
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <div className="bg-gray-100 flex-1 flex flex-row">
      <div className="justify-center flex w-full bg-white ml-4 my-4 rounded-l-lg border">
        <h1 className="self-start p-4 font-semibold">Skedjuli</h1>

        <div className="w-[32%] h-[82%] self-center bg-white rounded-2xl flex flex-col items-center justify-center">
          <h1 className="text-3xl font-bold">Welcome back</h1>
          <p className="text-sm font-light mb-4">Get some work done today!</p>

          <div className="w-full flex flex-col items-center justify-center">
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
            <div className="w-[58%] flex items-center justify-between">
              <label className="flex items-center gap-2 text-sm text-gray-700 select-none">
                <input
                  type="checkbox"
                  checked={rememberMe}
                  onChange={(e) => setRememberMe(e.target.checked)}
                  className="h-4 w-4"
                />
                Remember me
              </label>
            </div>
            <button
              type="button"
              onClick={() => {
                handleLogin();
              }}
              disabled={isSubmitting}
              className="bg-blue-500 border w-[58%] self-center mt-4 py-2 rounded-lg hover:bg-blue-700 disabled:opacity-50"
            >
              <span className="text-white">
                {isSubmitting ? "Signing in..." : "Sign in"}
              </span>
            </button>
          </div>
        </div>

        <div className="bg-primary justify-end w-[52%] rounded-2xl my-8 ml-8" />
      </div>
    </div>
  );
}
