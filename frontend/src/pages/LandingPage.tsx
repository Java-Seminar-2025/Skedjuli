import { useNavigate } from "react-router-dom";

export default function LandingPage() {
  const navigate = useNavigate();

  return (
    <div
      className="flex-1 flex flex-col
      bg-gradient-to-br from-blue-400 via-blue-600 to-blue-700"
    >
      <div className="flex-1 flex items-center justify-center p-8">
        <div className="text-center">
          <h1 className="text-4xl md:text-5xl font-bold text-white mb-6">
            Welcome to Skedjuli!
          </h1>

          <div className="flex flex-col sm:flex-row gap-6 justify-center mt-8">
            <button
              onClick={() => navigate("/login")}
              className="bg-orange-500 hover:bg-orange-600 text-white font-bold text-lg rounded-xl py-3 px-8 transition-all duration-300 shadow-lg hover:shadow-xl"
            >
              Login
            </button>

            <button
              onClick={() => navigate("/register")}
              className="bg-white hover:bg-gray-100 text-blue-700 font-bold text-lg rounded-xl py-3 px-8 transition-all duration-300 shadow-lg hover:shadow-xl"
            >
              Register
            </button>
          </div>
        </div>
      </div>

      <div className="bg-black/20 text-white/70 text-center py-4 text-sm">
        © 2025 Skedjuli. No rights reserved.
      </div>
    </div>
  );
}
