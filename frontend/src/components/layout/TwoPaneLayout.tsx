import type { ReactNode } from "react";

type Props = {
  title: string;
  right: ReactNode;
  children: ReactNode;
};

export default function TwoPaneLayout({ title, right, children }: Props) {
  return (
    <div className="min-h-screen bg-gray-100 p-6">
      <div className="mx-auto w-full max-w-6xl">
        <div className="mb-4 flex items-center justify-between">
          <h1 className="text-2xl font-bold">{title}</h1>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <div className="lg:col-span-2 bg-white border rounded-2xl p-6 shadow-sm">
            {children}
          </div>

          <div className="bg-white border rounded-2xl p-4 shadow-sm">
            {right}
          </div>
        </div>
      </div>
    </div>
  );
}
