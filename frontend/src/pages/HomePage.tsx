import { useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";

export default function HomePage() {
  const navigate = useNavigate();

  const token = useMemo(() => localStorage.getItem("token"), []);
  const email = useMemo(() => localStorage.getItem("email"), []);

  const [now, setNow] = useState<string>(() => new Date().toLocaleString());

  useEffect(() => {
    if (!token) navigate("/login");
  }, [token, navigate]);

  useEffect(() => {
    const id = setInterval(() => setNow(new Date().toLocaleString()), 1000);
    return () => clearInterval(id);
  }, []);

  function handleLogout() {
    localStorage.removeItem("token");
    localStorage.removeItem("email");
    navigate("/login");
  }

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center p-6">
      <div className="w-full max-w-2xl bg-white border rounded-2xl p-6 shadow-sm">
        <div className="flex items-start justify-between gap-4">
          <div>
            <h1 className="text-2xl font-bold">Home</h1>
            <p className="text-sm text-gray-600 mt-1">
              Signed in as{" "}
              <span className="font-medium">{email ?? "Unknown"}</span>
            </p>
            <p className="text-sm text-gray-600 mt-1">Now: {now}</p>
          </div>

          <button
            type="button"
            onClick={handleLogout}
            className="px-4 py-2 rounded-lg border bg-red-600/90 hover:bg-red-600/50"
          >
            Logout
          </button>
        </div>

        <hr className="my-6" />

        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <button
            type="button"
            onClick={() => navigate("/profile")}
            className="p-4 rounded-xl border bg-white hover:bg-gray-50 text-left"
          >
            <div className="font-semibold">Profile</div>
            <div className="text-sm text-gray-600">View account details</div>
          </button>

          <button
            type="button"
            onClick={() => navigate("/tasks")}
            className="p-4 rounded-xl border bg-white hover:bg-gray-50 text-left"
          >
            <div className="font-semibold">Create a new Course</div>
            <div className="text-sm text-gray-600">A fresh start...</div>
          </button>

          <button
            type="button"
            onClick={() => navigate("/settings")}
            className="p-4 rounded-xl border bg-white hover:bg-gray-50 text-left"
          >
            <div className="font-semibold">Settings</div>
            <div className="text-sm text-gray-600">App preferences</div>
          </button>

          <button
            type="button"
            onClick={() => navigate("/about")}
            className="p-4 rounded-xl border bg-white hover:bg-gray-50 text-left"
          >
            <div className="font-semibold">About</div>
            <div className="text-sm text-gray-600">Project info</div>
          </button>
        </div>
      </div>
    </div>
  );
}
