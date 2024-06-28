import {withAuthentication} from "./routeAuthentication.jsx";
import {useNavigate, useSearchParams} from "react-router-dom";
import {Subpage} from "./components/Subpage.jsx";
import {useEffect, useState} from "react";
import {
    Button,
    Card,
    CardBody, Flex,
    Menu,
    MenuButton, MenuDivider, MenuGroup, MenuItem, MenuList,
    Spinner,
    Stack,
    Text, useToast, VStack
} from "@chakra-ui/react";
import {Task} from "./services/taskService.js";
import {CodeEditor} from "./components/CodeEditor.jsx";
import {RenderMarkdown} from "./components/RenderMarkdown.jsx";

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
        for (let i = 1; i <= task.numberOfSubtasks; i++)
            verificationTypes.push(
                <div key={i}>
                    {i !== 1 && <MenuDivider/>}
                    <MenuGroup title={`Podzadanie ${i}`}>
                        <MenuItem><i className="fa-fw fa-solid fa-forward"/> <Text marginLeft='5px'>Sprawdzenie
                            szybkie</Text></MenuItem>
                        <MenuItem><i className="fa-fw fa-solid fa-check"/> <Text marginLeft='5px'>Sprawdzenie
                            pełne</Text></MenuItem>
                    </MenuGroup>
                </div>
            )
    }

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
                                        {verificationTypes.map((verificationType) => (
                                            verificationType
                                        ))}
                                    </MenuList>
                                </Menu>

                                <Button
                                    onClick={() => task.saveFile(fileContents)
                                        .then(success => {
                                            success ?
                                                toast({
                                                    title: 'Zapisano',
                                                    description: 'Zmiany zostały zapisane pomyślnie',
                                                    status: 'success',
                                                    duration: 4000,
                                                    isClosable: true
                                                }) :
                                                toast({
                                                    title: 'Wystąpił błąd',
                                                    description: 'Zapisywanie nie powiodło się',
                                                    status: 'error',
                                                    duration: 3000,
                                                    isClosable: true
                                                })
                                        })}
                                >
                                    <i className="fa-solid fa-fw fa-cloud-arrow-up"/>
                                    <Text marginLeft='5px'>Zapisz</Text>
                                </Button>

                                <Button>
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
