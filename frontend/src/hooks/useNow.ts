import { useEffect, useState } from "react";

export function useNow(intervalMs = 1000) {
  const [now, setNow] = useState<string>(() => new Date().toLocaleString());

  useEffect(() => {
    const id = setInterval(() => setNow(new Date().toLocaleString()), intervalMs);
    return () => clearInterval(id);
  }, [intervalMs]);

  return now;
}
