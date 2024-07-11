import {
  createBrowserRouter,
  RouterProvider,
} from "react-router-dom";
import { CreateTripPage } from "./pages/create-trip";
import { TripDetailsPage } from "./pages/trip-details";
import ErrorPage from "./pages/error";

const router = createBrowserRouter([
  {
    path: "/",
    element: <CreateTripPage />,
  },
  {
    path: "/trips/:tripId",
    element: <TripDetailsPage />
  },
  {
    path: "/*",
    element: <ErrorPage />,
  }
]);

// name export -> evita importações com nomes errados
export function App() {
  return (
    <RouterProvider router={router} />
  )
}
