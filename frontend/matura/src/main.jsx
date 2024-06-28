import React from 'react'
import ReactDOM from 'react-dom/client'

import {createBrowserRouter, Navigate, RouterProvider} from "react-router-dom";
import LoginForm from "./LoginForm.jsx";
import {ChakraProvider} from "@chakra-ui/react";
import {DashboardWithAuth} from "./Dashboard.jsx";
import theme from './theme'
import {TaskListWithAuth} from "./TaskList.jsx";
import {ActiveTaskListWithAuth} from "./ActiveTasks.jsx";
import SolveTaskWithAuth from "./SolveTask.jsx";

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
        path: '/activeTasks',
        element: <ActiveTaskListWithAuth/>
    },
    {
        path: '/solve',
        element: <SolveTaskWithAuth/>
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
