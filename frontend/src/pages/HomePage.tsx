import { useMemo } from "react";
import { useNavigate } from "react-router-dom";
import CenteredCardLayout from "../components/layout/CenteredCardLayout";
import HomeActions from "../components/home/HomeActions";
import HomeHeader from "../components/home/HomeHeader";
import { useNow } from "../hooks/useNow";
import type {
  LoginUserDetailsDto,
  LoginResponseDto,
} from "../data/dto/auth.dto";

function readUser(): LoginResponseDto | null {
  const raw = localStorage.getItem("user");
  if (!raw) return null;
  try {
    return JSON.parse(raw) as LoginResponseDto;
  } catch {
    return null;
  }
}

export default function HomePage() {
  const navigate = useNavigate();
  const now = useNow(1000);

  const userPayload = useMemo(() => readUser(), []);
  const u: LoginUserDetailsDto | null = userPayload?.user ?? null;

  const signedInAs = u ? `${u.firstName} ${u.lastName}`.trim() : "";
  const email = u?.email ?? "";

  function logout() {
    localStorage.removeItem("user");
    localStorage.removeItem("currentUserEmail");
    localStorage.removeItem("lecturerId");
    localStorage.removeItem("studyProgramId");
    navigate("/login");
  }

  return (
    <CenteredCardLayout>
      <HomeHeader
        email={email}
        now={now}
        onLogout={logout}
        signedInAs={signedInAs}
      />
      <hr className="my-6" />
      <HomeActions navigate={navigate} />
    </CenteredCardLayout>
  );
}
