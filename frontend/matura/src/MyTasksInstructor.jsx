import {withAuthentication} from "./routeAuthentication.jsx";
import {Subpage} from "./components/Subpage.jsx";
import {Heading} from "@chakra-ui/react";

const TasksInstructor = () => {
    return (
        <Subpage>
            <Heading>Tu będą zadania w wersji dla instruktorów i administratorów</Heading>
        </Subpage>
    )
}

export const MyTasksInstructor = withAuthentication(TasksInstructor);
