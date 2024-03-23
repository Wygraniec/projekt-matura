import React from 'react'
import ReactDOM from 'react-dom/client'

import {createBrowserRouter, Navigate, RouterProvider} from "react-router-dom";
import LoginForm from "./LoginForm.jsx";
import {ChakraProvider} from "@chakra-ui/react";
import {DashboardWithAuth} from "./Dashboard.jsx";
import theme from './theme'


const router = createBrowserRouter([
    {
        path: '/',
        element: <Navigate to={'/login'}/>
    },
    {
        path: '/login',
        element: <LoginForm/>
    },
    {
        path: '/dashboard',
        element: <DashboardWithAuth/>
    }
])

ReactDOM
    .createRoot(document.getElementById('root'))
    .render(
        <React.StrictMode>
            <ChakraProvider theme={theme}>
                <RouterProvider router={router}/>
            </ChakraProvider>
        </React.StrictMode>,
    )
