import type { NavigateFunction } from "react-router-dom";
import ActionCard from "../ui/ActionCard";
import type { LoginResponseDto, Role } from "../../data/dto/auth.dto";

type Props = {
  navigate: NavigateFunction;
};

function readRole(): Role | null {
  const raw = localStorage.getItem("user");
  if (!raw) return null;

  try {
    const parsed = JSON.parse(raw) as LoginResponseDto;
    return parsed?.user?.role ?? null;
  } catch {
    return null;
  }
}

export default function HomeActions({ navigate }: Props) {
  const role = readRole();

  const professorActions = [
    {
      title: "Create a new Course",
      description: "A fresh start....",
      path: "/newCourse",
    },
    {
      title: "Your courses",
      description: "Click for more!",
      path: "/courseList",
    },
    {
      title: "Enrollment forms",
      description: "forms are waiting...",
      path: "/enrollmentForms",
    },
  ] as const;

  const studentActions = [
    {
      title: "Create enrollment form",
      description: "Create and submit your enrollment form",
      path: "/student/enrollment/create",
    },
    {
      title: "Your Stats",
      description: "..",
      path: "/student/statistics",
    },
    {
      title: "Enrolled courses",
      description: "List of your enrolled courses",
      path: "/student/courses/enrolled",
    },

    {
      title: "Available courses",
      description: "Courses you can enroll in",
      path: "/student/courses/available",
    },
  ] as const;

  const actions = role === "STUDENT" ? studentActions : professorActions;

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
      {actions.map((a) => (
        <ActionCard
          key={a.path}
          title={a.title}
          description={a.description}
          onClick={() => navigate(a.path)}
        />
      ))}
    </div>
  );
}
