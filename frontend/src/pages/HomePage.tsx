import { useNavigate } from "react-router-dom";
import CenteredCardLayout from "../components/layout/CenteredCardLayout";
import HomeActions from "../components/home/HomeActions";
import HomeHeader from "../components/home/HomeHeader";
import { useNow } from "../hooks/useNow";

export default function HomePage() {
  const navigate = useNavigate();
  const now = useNow(1000);

  const email = localStorage.getItem("currentUserEmail");

  function logout() {
    localStorage.removeItem("currentUserEmail");
    localStorage.removeItem("lecturerId");
    localStorage.removeItem("studyProgramId");
    navigate("/login");
  }

  return (
    <CenteredCardLayout>
      <HomeHeader email={email ?? ""} now={now} onLogout={logout} />
      <hr className="my-6" />
      <HomeActions navigate={navigate} />
    </CenteredCardLayout>
  );
}
