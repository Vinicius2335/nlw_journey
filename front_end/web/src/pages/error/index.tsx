import { useRouteError } from "react-router-dom";

export default function ErrorPage() {
  const error = useRouteError();
  console.error(error);

  return (
    <div id="error-page" className="h-screen flex flex-col items-center justify-center gap-3">
      <h1 className="text-zinc-300 text-lg">Oops!</h1>
      <p>Sorry, an unexpected error has occurred.</p>
      <p>404 Error</p>
      <p>Page Not Found</p>
    </div>
  );
}