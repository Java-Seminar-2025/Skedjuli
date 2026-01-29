export default function SideNavigationBar() {
  return (
    <div className="space-y-2">
      <div className="text-sm font-semibold text-gray-800">Navigation</div>

      <button type="button" className="w-full text-left px-3 py-2 rounded-lg border bg-white hover:bg-gray-50">
        Courses
      </button>

      <button type="button" className="w-full text-left px-3 py-2 rounded-lg border bg-white hover:bg-gray-50">
        Study Programs
      </button>

      <button type="button" className="w-full text-left px-3 py-2 rounded-lg border bg-white hover:bg-gray-50">
        Academic Years
      </button>
    </div>
  );
}
