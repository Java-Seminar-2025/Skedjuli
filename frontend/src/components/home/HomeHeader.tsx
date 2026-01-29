type Props = {
  email: string | null;
  now: string;
  onLogout: () => void;
};

export default function HomeHeader({ email, now, onLogout }: Props) {
  return (
    <div className="flex items-start justify-between gap-4">
      <div>
        <h1 className="text-2xl font-bold">Home</h1>
        <p className="text-sm text-gray-600 mt-1">
          Signed in as <span className="font-medium">{email ?? "Unknown"}</span>
        </p>
        <p className="text-sm text-gray-600 mt-1">Now: {now}</p>
      </div>

      <button
        type="button"
        onClick={onLogout}
        className="px-4 py-2 rounded-lg border bg-red-600 text-white hover:bg-red-700"
      >
        Logout
      </button>
    </div>
  );
}
