import React from 'react'
import ReactDOM from 'react-dom/client'

import { createBrowserRouter, Navigate, RouterProvider } from "react-router-dom";
import Login from "./Login.jsx";
import { ChakraProvider } from "@chakra-ui/react";

const router = createBrowserRouter([
    {
        path: "/",
        element: <Navigate to={'/login'}></Navigate>
    },
    {
        path: "/login",
        element: <Login/>,
    },
]);

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <ChakraProvider>
            <RouterProvider router={router}/>
        </ChakraProvider>
    </React.StrictMode>,
)
