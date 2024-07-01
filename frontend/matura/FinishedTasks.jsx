import {withAuthentication} from "./src/routeAuthentication.jsx";
import {Subpage} from "./src/components/Subpage.jsx";
import {Navigate, useLocation, useNavigate} from "react-router-dom";
import {
    Box, Button,
    Card,
    CardBody,
    CardFooter,
    CardHeader,
    Flex,
    Grid,
    Heading,
    HStack,
    Spinner,
    Text,
    useToast
} from "@chakra-ui/react";
import {useEffect, useState} from "react";
import {getTasks, Task, TaskPage} from "./src/services/taskService.js";
import {User} from "./src/services/userService.js";
import {PaginationLinks} from "./src/components/PaginationLinks.jsx";
import {LanguageIcon} from "./src/components/LanguageIcon.jsx";
import PropTypes from "prop-types";

const TaskCard = ({ task }) => {
    const [loading, setLoading] = useState(true)
    const [template, setTemplate] = useState({})
    const [assigningUsername, setAssigningUsername] = useState('')

    const navigate = useNavigate()
    const toast = useToast()

    useEffect(() => {
        setLoading(true)

        const fetch = async () => {
            const template = await task.getTemplate()
            setTemplate(template)

            const username = await task.getAssigningUsername()
            setAssigningUsername(username)
        }

        fetch().then(() => {
            setLoading(false)
        })
    }, [task]);


    return (
        <Card maxWidth='md' marginX='10px' marginY='25px'>
            {loading && (
                <CardBody textAlign="center">
                    <Spinner size="xl"/>
                    <Text>Wczytywanie</Text>
                </CardBody>
            )}

            {!loading && (
                <>
                    <CardHeader>
                        <Heading size='md' textAlign='center'>{template.source}</Heading>
                    </CardHeader>

                    <CardBody>
                        <HStack>
                            <LanguageIcon language={template.language} boxSize='100px'/>

                            <Box>
                                <Text>Przypisał: {assigningUsername}</Text>
                                <Text>
                                    Przypisano {task.createdAt.toLocaleDateString().replaceAll('/', '.')}
                                </Text>
                            </Box>
                        </HStack>
                    </CardBody>

                    <CardFooter justifyContent='center'>
                        <Button marginY='5px' onClick={() => {
                            const id = Task.createOrGet(template.id)

                            toast.promise(id, {
                                success: {
                                    title: 'Ładowanie zakończone',
                                    description: 'Wkrótce nastąpi przekierowanie',
                                    isClosable: false
                                },
                                error: {
                                    title: 'Wystąpił błąd',
                                    description: 'Zadanie nie mogło zostać przypisane',
                                    isClosable: true
                                },
                                loading: {
                                    title: 'Ładowanie',
                                    isClosable: false
                                },
                            })

                            id.then(value => {
                                navigate(`/solve?task=${value}`)
                            })
                        }}>
                            <i className="fa-solid fa-arrow-rotate-left"/>
                            <Text marginLeft='5px'>Rozwiąż ponownie</Text>
                        </Button>
                    </CardFooter>
                </>
            )}

        </Card>
    )
}
TaskCard.propTypes = {
    task: PropTypes.instanceOf(Task).isRequired,
}

const FinishedTasks = () => {
    const location = useLocation();
    const toast = useToast();
    const [taskPage, setTaskPage] = useState(new TaskPage());
    const [loading, setLoading] = useState(true);

    let page = Number((new URLSearchParams(location.search)).get('page') || 0);

    if (page < 0)
        window.location = '/activeTasks?page=0';

    useEffect(() => {
        setLoading(true);
        getTasks(page, 10, User.fromLocalStorage().id, ["FINISHED"]).then(
            taskPage => {
                setTaskPage(taskPage);
                setLoading(false);
            }
        );
    }, [page]);

    useEffect(() => {
        if (taskPage.totalElements === 0) {
            toast({
                title: 'Brak wyników',
                description: 'Nie masz żadnych rozwiązanych zadań. Czas się wziąć do roboty!',
                status: 'info',
                duration: 4000,
                isClosable: true
            })
        }
    }, [taskPage, toast]);

    return (
        <>
            <Subpage>
                {page >= taskPage.totalPages ? <Navigate to="/finishedTasks"/> : null}

                {loading && (
                    <Card display="flex" alignItems="center" justifyContent="center">
                        <CardBody textAlign="center">
                            <Spinner size="xl"/>

                            <Text>Ładowanie zadań</Text>
                        </CardBody>
                    </Card>
                )}

                <Grid templateColumns='repeat(5, auto)' templateRows='repeat(2, auto)'>
                    {!loading && (
                        <>
                            {taskPage.tasks.map((task) => (
                                <TaskCard task={task} key={task.id}/>
                            ))}
                        </>
                    )}
                </Grid>

                <Flex justifyContent='center'>
                    <PaginationLinks totalPages={taskPage.totalPages} currentPage={taskPage.currentPage - 1}/>
                </Flex>
            </Subpage>
        </>
    )
}

export const FinishedTasksWithAuth = withAuthentication(FinishedTasks)

