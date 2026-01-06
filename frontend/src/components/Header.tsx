import { Navigate, useNavigate } from "react-router-dom";

export default function Header() {
  const Navigation = useNavigate();
  return (
    <button
      onClick={() => Navigation("/")}
      className="bg-white h-20 w-full flex items-center justify-start px-8 shadow-md"
    >
      <h1 className="text-2xl font-bold text-blue-700">Skedjuli</h1>
    </button>
  );
}
