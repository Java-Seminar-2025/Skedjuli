import { useState, useEffect } from "react";
useEffect(() => {
  const id = setInterval(
    () => setCurrentTme(new Date().toLocaleString()),
    1000
  );
  return () => clearInterval(id);
}, []);
export const [currentTme, setCurrentTme] = useState<string>(() =>
  new Date().toLocaleString()
);
