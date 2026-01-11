import { useMutation } from "@tanstack/react-query";
import { useNavigate } from "react-router-dom";
import { registerApi } from "./RegisterApi";
export function useRegister() {
  const navigate = useNavigate();

  return useMutation({
    mutationFn: registerApi,
    onSuccess: (data) => {
      //  { token, email }
      localStorage.setItem("token", JSON.stringify(data.token));
      navigate("/", { replace: true });
    },
  });
}
