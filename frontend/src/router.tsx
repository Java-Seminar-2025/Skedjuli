import { createBrowserRouter } from "react-router-dom";

import AppLayout from "./components/layout/AppLayout";

import LandingPage from "./pages/LandingPage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import HomePage from "./pages/HomePage";
import PlaceholderPage from "./pages/PlaceholderPage";
import CreateNewCourse from "./pages/courses/CreateNewCourse";
import CourseList from "./components/courses/CourseList";

export const router = createBrowserRouter([
  {
    element: <AppLayout />,
    children: [
      { path: "/", element: <LandingPage /> },
      { path: "/login", element: <LoginPage /> },
      { path: "/register", element: <RegisterPage /> },

      { path: "/home", element: <HomePage /> },

      {
        path: "/newCourse",
        element: <CreateNewCourse />,
      },
      {
        path: "/courseList",
        element: <CourseList />,
      },
      { path: "/profile", element: <PlaceholderPage title="Profile" /> },
      { path: "/settings", element: <PlaceholderPage title="Settings" /> },
      { path: "/about", element: <PlaceholderPage title="About" /> },
    ],
  },
]);
