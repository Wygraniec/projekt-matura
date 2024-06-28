import {withAuthentication} from "./routeAuthentication.jsx";
import {Navigate, useLocation} from "react-router-dom";
import {Subpage} from "./components/Subpage.jsx";
import {useEffect, useState} from "react";
import {
    Card,
    CardBody,
    Spinner,
    Text, useToast
} from "@chakra-ui/react";
import {getTasks, TaskPage} from "./services/taskService.js";
import {User} from "./services/userService.js";

const ActiveTaskList = () => {
    const location = useLocation();
    const toast = useToast();
    const [taskPage, setTaskPage] = useState(new TaskPage());
    const [loading, setLoading] = useState(true);

    let page = Number((new URLSearchParams(location.search)).get('page') || 0);

    if (page < 0)
        window.location = '/activeTasks?page=0';

    useEffect(() => {
        setLoading(true);
        const userId = User.fromLocalStorage().id;
        getTasks(page, 5, userId, ["CREATED", "PROCESSING"]).then(
            taskPage => {
                setTaskPage(taskPage);
                setLoading(false);
            }
        );
    }, [page]);

    useEffect(() => {
        if(taskPage.totalElements === 0) {
            toast({
                title: 'Brak wyników',
                description: 'Nie znaleziono zadań spełniających określone kryteria',
                status: 'error',
                duration: 4000,
                isClosable: false
            })
        }
    }, [taskPage, toast]);

    return (
        <Subpage>
            {page >= taskPage.totalPages ? <Navigate to="/activeTasks"/> : null}

            {loading && (
                <Card display="flex" alignItems="center" justifyContent="center">
                    <CardBody textAlign="center">
                        <Spinner size="xl"/>

                        <Text>Ładowanie zadań</Text>
                    </CardBody>
                </Card>
            )}

            {!loading && (
                <>
                    {taskPage.tasks.map((task) => (
                        <Text key={task.id}>
                            Task: {task.id}   Template: {task.templateId}   Created at: {task.createdAt.toString()}
                        </Text>
                    ))}
                </>
            )}
        </Subpage>
    );
};

export const ActiveTaskListWithAuth = withAuthentication(ActiveTaskList)
