import { Link } from "react-router-dom";

export default function Header() {
  return (
    <header className="bg-white h-20 w-full flex items-center px-8 shadow-md">
      <Link to="/" className="text-2xl font-bold text-blue-700">
        Skedjuli
      </Link>
    </header>
  );
}
