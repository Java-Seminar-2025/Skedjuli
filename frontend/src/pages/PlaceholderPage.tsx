type Props = {
  title: string;
};

export default function PlaceholderPage({ title }: Props) {
  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center p-6">
      <div className="w-full max-w-2xl bg-white border rounded-2xl p-6 shadow-sm">
        <h1 className="text-2xl font-bold">{title}</h1>
      </div>
    </div>
  );
}
