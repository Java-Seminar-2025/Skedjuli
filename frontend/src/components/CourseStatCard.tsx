type Props = {
  label: string;
  value: React.ReactNode;
};

export default function CourseStatCard({ label, value }: Props) {
  return (
    <div className="rounded-xl border bg-gray-50 px-4 py-3">
      <div className="text-xs text-gray-500">{label}</div>
      <div className="font-semibold">{value}</div>
    </div>
  );
}
