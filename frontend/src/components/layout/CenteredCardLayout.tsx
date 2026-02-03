import type { ReactNode } from "react";

type Props = {
  children: ReactNode;
  maxWidthClassName?: string;
};

export default function CenteredCardLayout({
  children,
  maxWidthClassName = "max-w-2xl",
}: Props) {
  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center p-6">
      <div
        className={`w-full ${maxWidthClassName} bg-white border rounded-2xl p-6 shadow-sm`}
      >
        {children}
      </div>
    </div>
  );
}
