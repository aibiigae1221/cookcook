import React, {Suspense} from "react";
import ReactDOM from "react-dom/client";
import {createBrowserRouter, RouterProvider} from "react-router-dom";

const IndexPage = React.lazy(() => import("./page/index/index"));
const CreateRecipePage = React.lazy(() => import("./page/create-recipe-page/index"));
const FindJobPage = React.lazy(() => import("./page/find-job-page/index"));
const TipSharingPage = React.lazy(() => import("./page/tip-sharing-page/index"));
const AboutThisAppPage = React.lazy(() => import("./page/about-this-app/index"));

const router  = createBrowserRouter([
  {
    path:"/",
    element:<IndexPage />
  },

  {
    path:"/create-recipe-page",
    element:<CreateRecipePage />
  },

  {
    path:"/find-job-page",
    element:<FindJobPage />
  },

  {
    path:"/tip-sharing-page",
    element:<TipSharingPage />
  },

  {
    path:"/about-this-app",
    element:<AboutThisAppPage />
  }
]);



function App() {
  return (
    <React.StrictMode>
      <Suspense fallback={<div>로딩...</div>}>
        <RouterProvider router={router} />
      </Suspense>
    </React.StrictMode>
  );
}

export default App;
