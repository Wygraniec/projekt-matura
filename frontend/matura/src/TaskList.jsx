import {withAuthentication} from "./routeAuthentication.jsx";
import {Link, Navigate, useLocation} from "react-router-dom";
import {Subpage} from "./components/Subpage.jsx";
import {getAvailableLanguages, getTemplates, Template, TemplatePage} from "./services/templateService.js";
import {useEffect, useState} from "react";
import {
    Box,
    Button,
    Card,
    CardBody,
    CardHeader, Divider,
    Flex,
    Image, Input, Select,
    Spinner,
    Text, useToast
} from "@chakra-ui/react";
import PropTypes from "prop-types";
import {motion} from 'framer-motion'
import ReactMarkdown from "react-markdown";
import {Formik, Form} from "formik";
import {PaginationLinks} from "./components/PaginationLinks.jsx";

const LanguageIcon = ({language, ...props}) => {
    switch (language) {
        case "CPP":
            return <Image
                src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/cplusplus/cplusplus-original.svg"
                {...props}
            />
        case "PYTHON":
            return <Image
                src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/python/python-original.svg"
                {...props}
            />
        case "C_SHARP":
            return <Image
                src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/csharp/csharp-original.svg"
                {...props}
            />
        case "JAVA":
            return <Image
                src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/java/java-original.svg"
                {...props}
            />
    }
}
LanguageIcon.propTypes = {
    language: PropTypes.string.isRequired
}


const TemplateCard = ({template, ...props}) => {
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
                        {/* TODO handle opening tasks reasonably (user whom it was assigned should be displayed the task with an appropriate id, whereas the others should have a new task created by them - for themselves)*/}
                        <Link to={``}>
                            <Button marginY='5px'>
                                <i className="fa-solid fa-code fa-fw"/>
                                <Text marginLeft='5px'>Rozwiąż</Text>
                            </Button>
                        </Link>
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
                        <Box
                            borderRadius="md"
                            maxWidth="95%"
                            width="full"
                            marginX="25px"
                            borderColor='gray'
                            borderWidth="1px"
                            borderStyle="solid"
                            paddingX="2.5em"
                            paddingY='1.5em'
                        >
                            <ReactMarkdown components={{
                                paragraph: (props) => <Text as='p' {...props}/>,

                                h1: (props) => <Text as='h1' fontSize='3xl' fontWeight='bold' {...props} />,
                                h2: (props) => <Text as='h2' fontSize='2xl' fontWeight='bold' {...props} />,
                                h3: (props) => <Text as='h3' fontSize='xl' fontWeight='bold' {...props} />,
                                h4: (props) => <Text as='h4' fontSize='lg' fontWeight='bold' {...props} />,
                                h5: (props) => <Text as='h5' fontSize='md' fontWeight='bold' {...props} />,
                                h6: (props) => <Text as='h6' fontSize='sm' fontWeight='bold' {...props} />,

                                list: (props) => <Box as='ul' {...props} />,
                                listItem: (props) => <Box as='li' {...props} />,
                                orderedList: (props) => <Box as='ol' {...props} />,

                                code: (props) => <Text as='code' {...props} />,
                                inlineCode: (props) => <Text as='code' fontSize='sm' {...props} />,
                                blockquote: (props) => <Box as='blockquote' borderLeft='4px solid gray.300'
                                                            p={4} {...props} />,

                                strong: (props) => <Text as='strong' fontWeight='bold' {...props} />,
                                em: (props) => <Text as='em' fontStyle='italic' {...props} />,

                                hr: (props) => <Divider {...props}/>,

                                link: (props) => (
                                    <Text as='a' color='blue.500'
                                          _hover={{textDecoration: 'underline'}} {...props} />
                                )
                            }}>
                                {template.statement}
                            </ReactMarkdown>
                        </Box>
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
        if(templatePage.totalElements === 0) {
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
        ;
};

export const TaskListWithAuth = withAuthentication(TaskList)
