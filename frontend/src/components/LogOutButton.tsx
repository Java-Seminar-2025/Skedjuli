import { useNavigate } from "react-router-dom";
import { handleLogout } from "../HelperFunctions/HandleLogoutHelper";

type LogOutButtonProps = {
  label?: string;
  className?: string;
};

export const LogOutButton = ({
  label = "Log out",
  className,
}: LogOutButtonProps) => {
  const navigate = useNavigate();

  return (
    <button
      type="button"
      onClick={() => handleLogout(navigate)}
      className={`px-4 py-2 rounded-lg border bg-red-600/90 hover:bg-red-600/50 ${
        className ?? ""
      }`}
    >
      {label}
    </button>
  );
};
