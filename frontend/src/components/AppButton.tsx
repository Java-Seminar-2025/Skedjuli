type Props = {
  label: string;
  onClick?: () => void;
  type?: "button" | "submit";
  variant?: "primary" | "secondary";
  disabled?: boolean;
  className?: string;
};

export default function AppButton({
  label,
  onClick,
  type = "button",
  variant = "secondary",
  disabled,
  className,
}: Props) {
  const base =
    "px-4 py-2 rounded-lg border disabled:opacity-60 disabled:cursor-not-allowed";
  const styles =
    variant === "primary"
      ? "bg-gray-900 text-white hover:bg-gray-800"
      : "bg-white hover:bg-gray-50";

  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={`${base} ${styles} ${className ?? ""}`}
    >
      {label}
    </button>
  );
}
