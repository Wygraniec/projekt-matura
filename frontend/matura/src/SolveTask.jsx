import {withAuthentication} from "./routeAuthentication.jsx";
import {useNavigate, useSearchParams} from "react-router-dom";
import {Subpage} from "./components/Subpage.jsx";
import {useEffect, useState} from "react";
import {
    Button,
    Flex,
    Menu,
    MenuButton, MenuDivider, MenuGroup, MenuItem, MenuList,
    Stack,
    Text, useToast, VStack
} from "@chakra-ui/react";
import {Task} from "./services/taskService.js";
import {CodeEditor} from "./components/CodeEditor.jsx";
import {RenderMarkdown} from "./components/RenderMarkdown.jsx";
import {Result} from "./services/resultService.js";
import * as PropTypes from "prop-types";
import {LoadingCard} from "./components/LoadingCard.jsx";

function CheckSubtaskMenuItem({subtaskNumber, onFastCheck, onFullCheck}) {
    return <div>
        {subtaskNumber !== 1 && <MenuDivider/>}
        <MenuGroup title={`Podzadanie ${subtaskNumber}`}>
            <MenuItem onClick={onFastCheck}>
                <i className="fa-fw fa-solid fa-forward"/>
                <Text marginLeft="5px">Sprawdzenie szybkie</Text>
            </MenuItem>

            <MenuItem onClick={onFullCheck}>
                <i className="fa-fw fa-solid fa-check"/>
                <Text marginLeft="5px">Sprawdzenie pełne</Text>
            </MenuItem>
        </MenuGroup>
    </div>;
}
CheckSubtaskMenuItem.propTypes = {
    subtaskNumber: PropTypes.number,
    onFastCheck: PropTypes.func,
    onFullCheck: PropTypes.func
}

const SolveTask = () => {
    const [searchParams] = useSearchParams()
    const taskId = Number(searchParams.get('task'))
    const navigate = useNavigate()
    const toast = useToast()

    const [loading, setLoading] = useState(true)
    const [task, setTask] = useState(null)
    const [template, setTemplate] = useState(null)
    const [fileContents, setFileContents] = useState('')

    const editorLanguageMapping = {
        'PYTHON': 'python',
        'C_SHARP': 'csharp',
        'CPP': 'cpp',
        'JAVA': 'java'
    }

    let verificationTypes = []

    useEffect(() => {
        setLoading(true);

        const fetchData = async () => {
            const task = await Task.findById(taskId);
            setTask(task);

            const template = await task.getTemplate();
            setTemplate(template);

            const file = await task.getFile()
            setFileContents(file)
        }

        fetchData().finally(() => setLoading(false));
    }, [taskId]);

    if (!loading && task === null)
        navigate(-1)

    if (!loading) {
        for (let i = 1; i <= template.numberOfSubtasks; i++)
            verificationTypes.push(
                <CheckSubtaskMenuItem key={i} subtaskNumber={i} onFastCheck={() => {
                    const promise = task.checkSubtask(fileContents, i, 'fast')

                    toast.promise(promise, {
                        success: {
                            title: 'Sprawdzanie zakończone',
                            description: 'Sprawdzanie dobiegło już końca! Wkrótce zobaczysz wynik.'
                        },
                        loading: {
                            title: 'Sprawdzanie w trakcie',
                            description: `Szybkie sprawdzanie podzadania ${i} w toku.`
                        },
                        error: {
                            title: 'Wystąpił błąd',
                            description: 'Coś poszło nie tak przy sprawdzaniu. Spróbuj ponownie później!'
                        }
                    })

                    //TODO show some feedback
                    promise.then(results => console.log(results))
                }} onFullCheck={() => {
                    const promise = task.checkSubtask(fileContents, i, 'full')

                    toast.promise(promise, {
                        success: {
                            title: 'Sprawdzanie zakończone',
                            description: 'Sprawdzanie dobiegło już końca! Wkrótce zobaczysz wynik.'
                        },
                        loading: {
                            title: 'Sprawdzanie w trakcie',
                            description: `Pełne sprawdzanie podzadania ${i} w toku.`
                        },
                        error: {
                            title: 'Wystąpił błąd',
                            description: 'Coś poszło nie tak przy sprawdzaniu. Spróbuj ponownie później!'
                        }
                    })

                    //TODO show some feedback
                    promise.then(results => console.log(results))
                }}/>
            )
    }

    return (
        <>
            <Subpage>
                {loading && <LoadingCard/>}

                {!loading && (
                    <Stack direction='row' height='80vh' maxWidth='98dvw'>
                        <CodeEditor language={editorLanguageMapping[template.language]} startingCode={fileContents}
                                    onChangeCallback={setFileContents}/>

                        <VStack width='60dvw'>
                            <Flex as='nav' direction='row' justifyContent='space-around' width='90%'
                                  marginBottom='10px'>
                                <Menu>
                                    <MenuButton as={Button}>
                                        Sprawdź podzadanie <i className="fa-solid fa-fw fa-chevron-down"/>
                                    </MenuButton>

                                    <MenuList paddingTop='0'>
                                        {verificationTypes}
                                    </MenuList>
                                </Menu>

                                <Button onClick={() => {
                                    toast.promise(task.saveFile(fileContents), {
                                        success: {
                                            title: 'Zapisano',
                                            description: 'Zmiany zostały zapisane pomyślnie',
                                            duration: 4000,
                                            isClosable: true
                                        },
                                        loading: {
                                            title: 'Zapisywanie'
                                        },
                                        error: {
                                            title: 'Wystąpił błąd',
                                            description: 'Zapisywanie nie powiodło się',
                                            duration: 3000,
                                            isClosable: true
                                        }
                                    })
                                }}>
                                    <i className="fa-solid fa-fw fa-cloud-arrow-up"/>
                                    <Text marginLeft='5px'>Zapisz</Text>
                                </Button>

                                <Button onClick={() => {
                                    const promise = task.check(fileContents)
                                    toast.promise(promise, {
                                        success: {
                                            title: 'Sprawdzanie zakończone',
                                            description: 'Sprawdzanie dobiegło już końca! Wkrótce zobaczysz wynik.'
                                        },
                                        loading: {
                                            title: 'Sprawdzanie w trakcie',
                                            description: `Pełne sprawdzanie zadania w toku.`
                                        },
                                        error: {
                                            title: 'Wystąpił błąd',
                                            description: 'Coś poszło nie tak przy sprawdzaniu. Spróbuj ponownie później!'
                                        }
                                    })

                                    //TODO show some feedback
                                    promise.then(submission => {
                                        Result.getBySubmissionId(submission)
                                            .then((result) => {
                                                console.log(result)
                                            })
                                    })
                                }}>
                                    <i className="fa-fw fa-solid fa-check"/>
                                    <Text marginLeft='5px'>Sprawdź</Text>
                                </Button>
                            </Flex>

                            <RenderMarkdown document={template.statement}/>
                        </VStack>
                    </Stack>
                )}
            </Subpage>
        </>
    )
}

const SolveTaskWithAuth = withAuthentication(SolveTask)

export default SolveTaskWithAuth;
