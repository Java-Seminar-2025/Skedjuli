import { useNavigate } from "react-router-dom";

type NavButtonProps = {
  navLocation: string;
  title: string;
  subtitle?: string;
  className?: string;
};

export const NavButton = ({
  navLocation,
  title,
  subtitle,
  className,
}: NavButtonProps) => {
  const navigate = useNavigate();

  return (
    <button
      type="button"
      onClick={() => navigate(navLocation)}
      className={`p-4 rounded-xl border bg-white hover:bg-gray-50 text-left ${
        className ?? ""
      }`}
    >
      <div className="font-semibold">{title}</div>
      {subtitle ? (
        <div className="text-sm text-gray-600">{subtitle}</div>
      ) : null}
    </button>
  );
};
