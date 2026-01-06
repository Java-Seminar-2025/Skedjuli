type InputFieldProps = {
  name: string;
  label: string;
  placeholder?: string;
  value?: string;
  type?: string;
  onValueChange?: (value: string) => void;
};

export default function InputField({
  name,
  label,
  placeholder,
  value,
  type = "text",
  onValueChange,
}: InputFieldProps) {
  return (
    <div className="w-[58%] mb-2">
      <label className="text-sm">{label}</label>
      <input
        className="border rounded p-2 w-full"
        name={name}
        placeholder={placeholder}
        value={value ?? ""}
        type={type}
        onChange={(e) => onValueChange?.(e.target.value)}
      />
    </div>
  );
}
