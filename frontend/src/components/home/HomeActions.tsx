import type { NavigateFunction } from "react-router-dom";
import ActionCard from "../ui/ActionCard";

type Props = {
  navigate: NavigateFunction;
};

export default function HomeActions({ navigate }: Props) {
  const actions = [
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
