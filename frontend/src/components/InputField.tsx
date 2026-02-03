type InputFieldProps = {
  name?: string;
  label: string;
  value: string;
  type?: string;
  placeholder?: string;
  onValueChange?: (value: string) => void;
  onChange?: (value: string) => void;
};

export default function InputField({
  name,
  label,
  value,
  type = "text",
  placeholder,
  onValueChange,
  onChange,
}: InputFieldProps) {
  const handler = onValueChange ?? onChange;

  return (
    <div>
      <label className="block text-sm font-medium text-gray-700 mb-1">{label}</label>
      <input
        name={name}
        type={type}
        value={value}
        placeholder={placeholder}
        onChange={(e) => handler?.(e.target.value)}
        className="w-full rounded-lg border px-3 py-2 outline-none focus:ring-2 focus:ring-gray-200"
      />
    </div>
  );
}
