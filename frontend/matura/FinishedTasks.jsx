import {withAuthentication} from "./src/routeAuthentication.jsx";

const FinishedTasks = () => {


    return (
        <>

        </>
    )
}

export const FinishedTasksWithAuth = withAuthentication(FinishedTasks)

