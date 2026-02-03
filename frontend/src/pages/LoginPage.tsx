import { useState } from "react";
import { useNavigate } from "react-router-dom";
import InputField from "../components/InputField";
import type { LoginRequestDto } from "../data/dto/auth.dto";
import { LoginApi } from "../data/services/LoginApi";
import { setStoredUser } from "../helpers/StoredUserHelper";
function getErrorMessage(err: unknown) {
  const e = err as any;
  return (
    e?.response?.data?.message ||
    e?.response?.data?.error ||
    (typeof e?.response?.data === "string" ? e.response.data : null) ||
    e?.message ||
    "Login failed"
  );
}

export default function LoginPage() {
  const navigate = useNavigate();
  const [form, setForm] = useState<LoginRequestDto>({
    email: "",
    password: "",
  });
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setError(null);
    setSaving(true);

    try {
      const res = await LoginApi.login(form);
      setStoredUser(res);

      navigate("/home");
    } catch (err) {
      setError(getErrorMessage(err));
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center p-6">
      <div className="w-full max-w-md bg-white border rounded-2xl p-6 shadow-sm">
        <h1 className="text-2xl font-bold">Login</h1>

        {error && (
          <div className="mt-4 rounded-xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-800">
            {error}
          </div>
        )}

        <form className="mt-6 space-y-4" onSubmit={handleSubmit}>
          <InputField
            label="Email"
            value={form.email}
            onValueChange={(v) => setForm((p) => ({ ...p, email: v }))}
            type="text"
          />
          <InputField
            label="Password"
            value={form.password}
            onValueChange={(v) => setForm((p) => ({ ...p, password: v }))}
            type="password"
          />

          <button
            type="submit"
            disabled={saving}
            className="w-full px-4 py-2 rounded-lg border bg-gray-900 text-white hover:bg-gray-800 disabled:opacity-60"
          >
            {saving ? "Signing in..." : "Login"}
          </button>
        </form>
      </div>
    </div>
  );
}
