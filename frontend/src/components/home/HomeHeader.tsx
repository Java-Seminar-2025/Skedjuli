type Props = {
  email: string;
  now: string;
  onLogout: () => void;
  signedInAs?: string;
};

export default function HomeHeader({
  email,
  now,
  onLogout,
  signedInAs,
}: Props) {
  return (
    <div className="flex items-center justify-between gap-4">
      <div className="min-w-0">
        <div className="text-sm text-gray-500">{now}</div>

        {signedInAs ? (
          <div className="mt-1 text-lg font-semibold truncate">
            Signed in as {signedInAs}
          </div>
        ) : null}

        {email ? (
          <div className="text-sm text-gray-600 truncate">{email}</div>
        ) : null}
      </div>

      <button
        type="button"
        onClick={onLogout}
        className="shrink-0 px-4 py-2 rounded-lg border bg-red-600 text-white hover:bg-red-700"
      >
        Logout
      </button>
    </div>
  );
}
