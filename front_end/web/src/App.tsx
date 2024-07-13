import { createBrowserRouter, RouterProvider } from "react-router-dom"
import { Slide, ToastContainer } from "react-toastify"
import "react-toastify/dist/ReactToastify.css"
import { CreateTripPage } from "./pages/create-trip"
import ErrorPage from "./pages/error"
import { TripDetailsPage } from "./pages/trip-details"

const router = createBrowserRouter([
  {
    path: "/",
    element: <CreateTripPage />
  },
  {
    path: "/trips/:tripId",
    element: <TripDetailsPage />
  },
  {
    path: "/*",
    element: <ErrorPage />
  }
])

// name export -> evita importações com nomes errados
export function App() {
  return (
    <>
      <ToastContainer
        position="top-center"
        autoClose={5000}
        limit={1}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick
        rtl={false}
        pauseOnFocusLoss
        draggable={false}
        pauseOnHover
        theme="dark"
        transition={Slide}
      />
      <RouterProvider router={router} />
    </>
  )
}
