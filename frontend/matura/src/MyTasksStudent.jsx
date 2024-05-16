import {withAuthentication} from "./routeAuthentication.jsx";
import {Subpage} from "./components/Subpage.jsx";
import {Heading} from "@chakra-ui/react";

const TasksStudent = () => {
    return (
        <Subpage>
            <Heading>Tu będą zadania w wersji dla ucznia</Heading>
        </Subpage>
    )
}

export const MyTasksStudent = withAuthentication(TasksStudent);
