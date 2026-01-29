type Props = {
  title: string;
  description: string;
  onClick: () => void;
};

export default function ActionCard({ title, description, onClick }: Props) {
  return (
    <button
      type="button"
      onClick={onClick}
      className="p-4 rounded-xl border bg-white hover:bg-gray-50 text-left"
    >
      <div className="font-semibold">{title}</div>
      <div className="text-sm text-gray-600">{description}</div>
    </button>
  );
}
