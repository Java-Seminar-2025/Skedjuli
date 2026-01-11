export const handleLogout = (navigate: (to: string) => void) => {
  localStorage.removeItem("token");
  localStorage.removeItem("email");
  navigate("/login");
};
