import {withAuthentication} from "./routeAuthentication.jsx";
import {Navigate, useLocation, useNavigate} from "react-router-dom";
import {Subpage} from "./components/Subpage.jsx";
import {getAvailableLanguages, getTemplates, Template, TemplatePage} from "./services/templateService.js";
import {useEffect, useState} from "react";
import {
    Box,
    Button,
    Card,
    CardBody,
    CardHeader, Flex,
    Input, Select,
    Spinner,
    Text, useToast
} from "@chakra-ui/react";
import PropTypes from "prop-types";
import {motion} from 'framer-motion'
import {Formik, Form} from "formik";
import {PaginationLinks} from "./components/PaginationLinks.jsx";
import {RenderMarkdown} from "./components/RenderMarkdown.jsx";
import {Task} from "./services/taskService.js";
import {LanguageIcon} from "./components/LanguageIcon.jsx";

const TemplateCard = ({template, ...props}) => {
    const toast = useToast()
    const navigate = useNavigate()
    const [isExpanded, setIsExpanded] = useState(false);

    return (
        <Card {...props} marginY='4px'>
            <CardHeader>
                <Flex justifyContent='space-between' alignItems='center'>
                    <Flex alignItems='center'>
                        <LanguageIcon language={template.language} boxSize='50px'/>
                        <Text marginX='5px'>{template.source}</Text>
                    </Flex>

                    <Flex flexDirection='column'>
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
                            <i className="fa-solid fa-code fa-fw"/>
                            <Text marginLeft='5px'>Rozwiąż</Text>
                        </Button>
                        <Button onClick={() => setIsExpanded(!isExpanded)} marginY='5px'>
                            <i className={`fa-solid fa-arrow-${isExpanded ? "up" : "down"} fa-fw`}/>
                            <Text marginLeft='5px'>Polecenie</Text>
                        </Button>
                    </Flex>
                </Flex>
            </CardHeader>
            {isExpanded && (
                <motion.div
                    initial={{height: '0'}}
                    animate={{height: '100%'}}
                    transition={{
                        duration: '0.5',
                    }}
                >
                    <CardBody display='flex' justifyContent='center'>
                        <RenderMarkdown document={template.statement}/>
                    </CardBody>
                </motion.div>
            )}
        </Card>
    )
}
TemplateCard.propTypes = {
    template: PropTypes.instanceOf(Template)
}

const languagesText = {
    'C_SHARP': 'C#',
    'PYTHON': 'Python',
    'JAVA': 'Java',
    'CPP': 'C++'
}

const TaskList = () => {
    const location = useLocation();
    const toast = useToast();
    const [templatePage, setTemplatePage] = useState(new TemplatePage());
    const [languages, setLanguages] = useState([]);
    const [loading, setLoading] = useState(true);

    const [templateLanguage, setTemplateLanguage] = useState('')
    const [templateSource, setTemplateSource] = useState('')

    let page = Number((new URLSearchParams(location.search)).get('page') || 0);

    if (page < 0)
        window.location = '/tasks?page=0';

    useEffect(() => {
        setLoading(true);
        getTemplates(page, 5, templateLanguage, templateSource).then(
            templatePage => {
                setTemplatePage(templatePage);
                setLoading(false);
            }
        );
    }, [page, templateLanguage, templateSource]);

    useEffect(() => {
        if (templatePage.totalElements === 0) {
            toast({
                title: 'Brak wyników',
                description: 'Nie znaleziono zadań spełniających określone kryteria',
                status: 'error',
                duration: 4000,
                isClosable: false
            })
        }
    }, [templatePage, toast]);

    useEffect(() => {
        getAvailableLanguages().then(languages => {
            setLanguages(languages)
        })
    }, [])

    return (
        <Subpage>
            {page >= templatePage.totalPages ? <Navigate to="/tasks"/> : null}

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
                    <Box marginBottom='15px'>
                        <Formik initialValues={{language: '', source: ''}} onSubmit={(values, {setSubmitting}) => {
                            setSubmitting(true);
                            setTemplateLanguage(values.language);
                            setTemplateSource(values.source);
                            setSubmitting(false);
                        }}>
                            {({values, isSubmitting, handleChange, handleBlur}) => (
                                <Form>
                                    <Flex flexDirection='row'>
                                        <Select
                                            placeholder='Wybierz język'
                                            borderRightRadius='0'
                                            width='33%'
                                            name='language'
                                            value={values.language}
                                            onChange={handleChange}
                                            onBlur={handleBlur}
                                        >
                                            {languages.map(language => (
                                                <option key={language} value={language}>
                                                    {languagesText[language]}
                                                </option>
                                            ))}
                                        </Select>
                                        <Input
                                            type='text'
                                            borderRadius='0'
                                            placeholder='Zadanie'
                                            name='source'
                                            value={values.source}
                                            onChange={handleChange}
                                            onBlur={handleBlur}
                                        />
                                        <Button type='submit' borderLeftRadius='0' disabled={isSubmitting}>
                                            <i className="fa-solid fa-magnifying-glass fa-fw"/>
                                        </Button>
                                    </Flex>
                                </Form>
                            )}
                        </Formik>
                    </Box>

                    {templatePage.templates.map((template) => (
                        <TemplateCard template={template} id={template.id} key={template.id}/>
                    ))}

                    <Flex justifyContent='center'>
                        <PaginationLinks totalPages={templatePage.totalPages} currentPage={page}/>
                    </Flex>
                </>
            )
            }
        </Subpage>
    )
};

export const TaskListWithAuth = withAuthentication(TaskList)
