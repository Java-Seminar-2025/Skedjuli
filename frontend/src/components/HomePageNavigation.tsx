import { useMemo, useEffect, useState } from "react";
import { LogOutButton } from "./LogOutButton";
import { NavButton } from "./NavButton";

export default function HomePageNavigation() {
  const email = useMemo(() => localStorage.getItem("email"), []);

  const [now, setNow] = useState(() => new Date().toLocaleString());
  useEffect(() => {
    const id = setInterval(() => setNow(new Date().toLocaleString()), 1000);
    return () => clearInterval(id);
  }, []);
  return (
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

        <LogOutButton />
      </div>

      <hr className="my-6" />

      <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
        <NavButton
          navLocation="/profile"
          title="Profile"
          subtitle="View account details"
        />
        <NavButton
          navLocation="/Course"
          title="Create a new Course"
          subtitle="A fresh start..."
        />
        <NavButton
          navLocation="/settings"
          title="Settings"
          subtitle="App preferences"
        />
        <NavButton navLocation="/about" title="About" subtitle="Project info" />
      </div>
    </div>
  );
}
