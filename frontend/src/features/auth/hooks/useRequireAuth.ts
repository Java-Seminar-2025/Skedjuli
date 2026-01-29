export type AuthSession = {
  email: string | null;
  isAuthenticated: boolean;
};

export function getSession(): AuthSession {
  const email = localStorage.getItem("currentUserEmail");
  return { email, isAuthenticated: !!email };
}

export function clearSession() {
  localStorage.removeItem("currentUserEmail");
  localStorage.removeItem("lecturerId");
  localStorage.removeItem("studyProgramId");
}
