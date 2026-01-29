import type { NavigateFunction } from "react-router-dom";
import ActionCard from "../ui/ActionCard";

type Props = {
  navigate: NavigateFunction;
};

export default function HomeActions({ navigate }: Props) {
  const actions = [
    { title: "Profile", description: "View account details", path: "/profile" },
    {
      title: "Create a new Course",
      description: "A fresh start....",
      path: "/newCourse",
    },
    { title: "Settings", description: "App preferences", path: "/settings" },
    { title: "About", description: "Project info", path: "/about" },
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
