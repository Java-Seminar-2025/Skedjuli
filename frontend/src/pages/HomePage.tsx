import HomePageNavigation from "../components/HomePageNavigation";
import { useEffect, useMemo } from "react";
import { useNavigate } from "react-router-dom";

export default function HomePage() {
  const navigate = useNavigate();
  const token = useMemo(() => localStorage.getItem("token"), []);

  useEffect(() => {
    if (!token) navigate("/login");
  }, [token, navigate]);

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center p-6">
      <HomePageNavigation />
    </div>
  );
}
