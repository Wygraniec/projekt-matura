import React from 'react'
import ReactDOM from 'react-dom/client'

import {createBrowserRouter, Navigate, RouterProvider, useParams} from "react-router-dom";
import LoginForm from "./LoginForm.jsx";
import {ChakraProvider} from "@chakra-ui/react";
import {DashboardWithAuth} from "./Dashboard.jsx";
import theme from './theme'
import {TaskListWithAuth} from "./TaskList.jsx";

const TemplateDetails = () => {
    const {taskId} = useParams()
    return <p>{taskId}</p>
}

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
    },
    {
        path: '/tasks',
        element: <TaskListWithAuth/>
    },
    {
        path: "/tasks/:taskId",
        element: <TemplateDetails/>
    },
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
