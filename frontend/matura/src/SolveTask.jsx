import {withAuthentication} from "./routeAuthentication.jsx";
import {useSearchParams} from "react-router-dom";
import {Subpage} from "./components/Subpage.jsx";
import {useEffect, useState} from "react";
import {Card, CardBody, Spinner, Text} from "@chakra-ui/react";
import {Task} from "./services/taskService.js";

const SolveTask = () => {
    const [searchParams] = useSearchParams()
    const taskId = Number(searchParams.get('task'))

    const [loading, setLoading] = useState(true)
    const [task, setTask] = useState(null)

    useEffect(() => {
        setLoading(true)

        Task
            .findById(taskId)
            .then(task => setTask(task))

        setLoading(false)
    }, [taskId]);

    if(!loading)
        console.log(task)

    return (
        <>
            <Subpage>
                {loading && (
                    <Card display="flex" alignItems="center" justifyContent="center">
                        <CardBody textAlign="center">
                            <Spinner size="xl"/>

                            <Text>Ładowanie zadania</Text>
                        </CardBody>
                    </Card>
                )}

                {!loading && (
                    "t"
                )}
            </Subpage>
        </>
    )
}

const SolveTaskWithAuth = withAuthentication(SolveTask)

export default SolveTaskWithAuth;
