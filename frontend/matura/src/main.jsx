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
import {FinishedTasksWithAuth} from "../FinishedTasks.jsx";
import {ErrorBoundary} from "react-error-boundary";

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
        path: '/finishedTasks',
        element: <FinishedTasksWithAuth/>
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
                <ErrorBoundary
                    FallbackComponent={() => <Navigate to={'/'}/>}
                    onError={(error, info) => {
                        if(import.meta.env.DEV)
                            console.log(error, info)
                    }}
                >
                    <RouterProvider router={router}/>
                </ErrorBoundary>
            </ChakraProvider>
        </React.StrictMode>,
    )
